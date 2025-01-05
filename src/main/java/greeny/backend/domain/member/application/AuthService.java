package greeny.backend.domain.member.application;

import greeny.backend.config.jwt.JwtProvider;
import greeny.backend.domain.member.entity.*;
import greeny.backend.domain.member.presentation.dto.*;
import greeny.backend.exception.situation.member.GeneralMemberNotFoundException;
import greeny.backend.exception.situation.member.MemberNotFoundException;
import greeny.backend.exception.situation.member.EmailAlreadyExistsException;
import greeny.backend.exception.situation.member.LoginFailureException;
import greeny.backend.exception.situation.member.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final AgreementRepository agreementRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public void validateSignUpInfoWithGeneral(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private Long validateSignUpInfoWithSocial(String email) {
        Long foundMemberId = getMember(email).getId();
        if(generalMemberRepository.existsByMemberId(foundMemberId)) {
            throw new EmailAlreadyExistsException(email);
        }
        return foundMemberId;
    }

    public GetTokenStatusInfoResponseDto getTokenStatusInfo(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            if(jwtProvider.validateToken(bearerToken.substring(7))) {
                return GetTokenStatusInfoResponseDto.builder()
                        .isValid(true)
                        .build();
            }
        }
        return GetTokenStatusInfoResponseDto.builder()
                .isValid(false)
                .build();
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        validateSignUpInfoWithGeneral(signUpRequestDto.getEmail());
        saveGeneralMember(signUpRequestDto);
    }

    @Transactional
    public TokenResponseDto signInWithGeneral(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        GeneralMember foundGeneralMember = getGeneralMember(getMember(email).getId());
        boolean foundIsAuto = foundGeneralMember.isAuto();
        changeIsAutoByGeneralSignIn(loginRequestDto.getIsAuto(), foundGeneralMember, foundIsAuto);
        return authorize(email, loginRequestDto.getPassword());
    }

    public TokenResponseDto signInWithSocial(String email, Provider provider) {
        if(memberRepository.existsByEmail(email)) {
            Long validatedMemberId = validateSignUpInfoWithSocial(email);
            Long foundMemberId = getMember(email).getId();
            if(!agreementRepository.existsByMemberId(foundMemberId)) {
                agreementRepository.save(toAgreement(foundMemberId, false, false));
            }

            TokenResponseDto authorizedToken = publishToken(new UsernamePasswordAuthenticationToken(email, validatedMemberId.toString()));
            return TokenResponseDto.from(authorizedToken.getAccessToken(), authorizedToken.getRefreshToken());
        }

        saveSocialMemberExceptAgreement(email, provider);
        return TokenResponseDto.excludeTokenInDto(email);
    }

    public TokenResponseDto agreementInSignUp(AgreementRequestDto agreementRequestDto) {
        String email = agreementRequestDto.getEmail();
        Long foundMemberId = getMember(email).getId();

        if (!memberRepository.existsByEmail(email)) {
            throw new MemberNotFoundException();
        }
        else {
            if (generalMemberRepository.existsByMemberId(foundMemberId)) {
                saveAgreement(foundMemberId, agreementRequestDto);
                return TokenResponseDto.excludeEmailInDto("nothing", "nothing");
            }
        }

        saveAgreement(foundMemberId, agreementRequestDto);
        return authorize(email, foundMemberId.toString());
    }

    @Transactional
    public void findPassword(FindPasswordRequestDto findPasswordRequestDto) {
        getGeneralMember(getMember(findPasswordRequestDto.getEmail()).getId())
                .changePassword(passwordEncoder.encode(findPasswordRequestDto.getPassword()));
    }

    public GetIsAutoInfoResponseDto getIsAutoInfo(Long memberId) {
        return GetIsAutoInfoResponseDto.builder()
                .isAuto(getGeneralMember(memberId).isAuto())
                .build();
    }

    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        String refreshToken = tokenRequestDto.getRefreshToken();
        Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());
        String email = authentication.getName();

        if (!jwtProvider.validateToken(refreshToken)) {
            refreshTokenRepository.deleteById(email);
            return publishToken(authentication);
        }
        else {
            validateRefreshTokenOwner(email, refreshToken);
            return TokenResponseDto.excludeEmailInDto(
                    jwtProvider.generateAccessToken(authentication, (new Date()).getTime()),
                    refreshToken
            );
        }
    }

    private void saveGeneralMember(SignUpRequestDto signUpRequestDto) {
        Long savedMemberId = memberRepository.save(toMember(signUpRequestDto.getEmail())).getId();
        generalMemberRepository.save(toGeneralMember(savedMemberId, signUpRequestDto.getPassword()));
        memberInfoRepository.save(
                toMemberInfo(
                        savedMemberId,
                        signUpRequestDto.getName(),
                        signUpRequestDto.getPhone(),
                        signUpRequestDto.getBirth()
                )
        );
    }

    private void saveSocialMemberExceptAgreement(String email, Provider provider) {
        Member savedMember = memberRepository.save(toMember(email));
        socialMemberRepository.save(toSocialMember(provider, savedMember.getId()));
    }

    private void saveAgreement(Long memberId, AgreementRequestDto agreementRequestDto) {
        agreementRepository.save(
                toAgreement(
                        memberId,
                        agreementRequestDto.getPersonalInfo(),
                        agreementRequestDto.getThirdParty()
                )
        );
    }

    private Member toMember(String email) {
        return Member.builder()
                .email(email)
                .role(Role.ROLE_USER)
                .build();
    }

    private GeneralMember toGeneralMember(Long memberId, String password) {
        return GeneralMember.builder()
                .memberId(memberId)
                .password(passwordEncoder.encode(password))
                .isAuto(false)
                .build();
    }

    private SocialMember toSocialMember(Provider provider, Long memberId) {
        return SocialMember.builder()
                .memberId(memberId)
                .provider(provider)
                .build();
    }

    private MemberInfo toMemberInfo(Long memberId, String name, String phone, String birth) {
        return MemberInfo.builder()
                .memberId(memberId)
                .name(name)
                .phone(phone)
                .birth(birth)
                .build();
    }

    private Agreement toAgreement(Long memberId, boolean personalInfo, boolean thirdParty) {
        return Agreement.builder()
                .memberId(memberId)
                .personalInfo(personalInfo)
                .thirdParty(thirdParty)
                .build();
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    public GeneralMember getGeneralMember(Long memberId) {
        return generalMemberRepository.findByMemberId(memberId).orElseThrow(GeneralMemberNotFoundException::new);
    }

    private void changeIsAutoByGeneralSignIn(boolean isAutoToCheck, GeneralMember foundGeneralMember, boolean foundIsAuto) {
        if (!isAutoToCheck) {
            if (foundIsAuto) {
                foundGeneralMember.changeIsAuto(false);
            }
        }
        else {
            if (!foundIsAuto) {
                foundGeneralMember.changeIsAuto(true);
            }
        }
    }

    private TokenResponseDto authorize(String email, String password) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if (refreshTokenRepository.existsById(email)) {
            RefreshToken foundRefreshToken = getRefreshToken(email);
            String foundRefreshTokenValue = foundRefreshToken.getValue();

            if (jwtProvider.validateToken(foundRefreshTokenValue)) {
                return TokenResponseDto.excludeEmailInDto(
                        jwtProvider.generateAccessToken(authentication, (new Date()).getTime()),
                        foundRefreshTokenValue
                );
            }
            else {
                refreshTokenRepository.delete(foundRefreshToken);
            }
        }

        return publishToken(authentication);
    }

    private RefreshToken getRefreshToken(String key) {
        return refreshTokenRepository.findById(key)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    private RefreshToken toRefreshToken(String key, String value) {
        return RefreshToken.builder()
                .key(key)
                .value(value)
                .build();
    }

    private TokenResponseDto publishToken(Authentication authentication) {
        TokenDto generatedTokenDto = jwtProvider.generateTokenDto(authentication);
        String generatedRefreshToken = generatedTokenDto.getRefreshToken();
        refreshTokenRepository.save(toRefreshToken(authentication.getName(), generatedRefreshToken));
        return TokenResponseDto.excludeEmailInDto(generatedTokenDto.getAccessToken(), generatedRefreshToken);
    }

    private void validateRefreshTokenOwner(String email, String refreshToken) {
        if (!refreshTokenRepository.findById(email).orElseThrow(RefreshTokenNotFoundException::new)
                .getValue()
                .equals(refreshToken)
        ) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }
}
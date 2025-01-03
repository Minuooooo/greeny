package greeny.backend.domain.member.application;

import greeny.backend.domain.member.entity.*;
import greeny.backend.domain.wishlist.entity.ProductWishlistRepository;
import greeny.backend.domain.wishlist.entity.StoreWishlistRepository;
import greeny.backend.exception.situation.common.TypeDoesntExistException;
import greeny.backend.exception.situation.member.*;
import greeny.backend.domain.member.presentation.dto.CancelWishlistRequestDto;
import greeny.backend.domain.member.presentation.dto.EditMemberInfoRequestDto;
import greeny.backend.domain.member.presentation.dto.GetMemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import static greeny.backend.domain.Target.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final AgreementRepository agreementRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StoreWishlistRepository storeWishlistRepository;
    private final ProductWishlistRepository productWishlistRepository;
    private final AuthService authService;

    public Member getCurrentMember() {
        return memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(MemberNotFoundException::new);
    }

    public GetMemberInfoResponseDto getMemberInfo() {
        Member currentMember = getCurrentMember();
        Long currentMemberId = currentMember.getId();

        if(generalMemberRepository.existsByMemberId(currentMemberId)) {
            MemberInfo currentMemberInfo = getMemberProfile(currentMemberId);
            return GetMemberInfoResponseDto.toGeneralMemberDto(
                    currentMember.getEmail(),
                    currentMemberInfo.getName(),
                    currentMemberInfo.getPhone(),
                    currentMemberInfo.getBirth()
            );
        }

        return GetMemberInfoResponseDto.toSocialMemberDto(currentMember.getEmail(), getMemberSocial(currentMemberId).getProvider().getName());
    }

    public void deleteMember() {
        Member currentMember = getCurrentMember();
        String key = currentMember.getEmail();
        if(refreshTokenRepository.existsById(key)) {
            refreshTokenRepository.deleteById(key);
        }
        checkAndDeleteGeneralOrSocialMember(currentMember, currentMember.getId());
    }

    @Transactional
    public void editMemberInfo(EditMemberInfoRequestDto editMemberRequestDto) {
        GeneralMember currentGeneralMember = authService.getGeneralMember(getCurrentMember().getId());
        if(!passwordEncoder.matches(editMemberRequestDto.getPasswordToCheck(), currentGeneralMember.getPassword())) {
            throw new MemberNotEqualsException();
        }
        currentGeneralMember.changePassword(passwordEncoder.encode(editMemberRequestDto.getPasswordToChange()));
    }

    @Transactional
    public void cancelBookmark(String type, CancelWishlistRequestDto cancelWishlistRequestDto) {
        checkAndCancelWishlist(type, cancelWishlistRequestDto.getIdsToDelete());
    }

    private MemberInfo getMemberProfile(Long memberId) {
        return memberInfoRepository.findByMemberId(memberId).orElseThrow(MemberInfoNotFoundException::new);
    }

    private Agreement getMemberAgreement(Long memberId) {
        return agreementRepository.findByMemberId(memberId).orElseThrow(AgreementNotFoundException::new);
    }

    private SocialMember getMemberSocial(Long memberId) {
        return socialMemberRepository.findByMemberId(memberId).orElseThrow(SocialMemberNotFoundException::new);
    }

    private void checkAndDeleteGeneralOrSocialMember(Member currentMember, Long currentMemberId) {
        if(generalMemberRepository.existsByMemberId(currentMemberId)) {
            generalMemberRepository.delete(authService.getGeneralMember(currentMemberId));
            memberInfoRepository.delete(getMemberProfile(currentMemberId));
        }
        else {
            socialMemberRepository.delete(getMemberSocial(currentMemberId));
        }

        agreementRepository.delete(getMemberAgreement(currentMemberId));
        memberRepository.delete(currentMember);
    }

    private void checkAndCancelWishlist(String type, List<Long> idsToDelete) {
        if (type.equals(STORE.toString())) {
            storeWishlistRepository.deleteStoreWishlistsByIds(idsToDelete);
        }
        else if (type.equals(PRODUCT.toString())) {
            productWishlistRepository.deleteProductWishlistsByIds(idsToDelete);
        }
        else {
            throw new TypeDoesntExistException();
        }
    }
}
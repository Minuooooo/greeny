package greeny.backend.application.member;


import greeny.backend.application.member.auth.AuthService;
import greeny.backend.domain.product.ProductBookmarkRepository;
import greeny.backend.domain.store.StoreBookmarkRepository;
import greeny.backend.domain.member.*;
import greeny.backend.exception.situation.common.TypeDoesntExistException;
import greeny.backend.exception.situation.member.*;
import greeny.backend.presentation.dto.member.request.CancelBookmarkRequestDto;
import greeny.backend.presentation.dto.member.request.EditMemberInfoRequestDto;
import greeny.backend.presentation.dto.member.response.GetMemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    private final StoreBookmarkRepository storeBookmarkRepository;
    private final ProductBookmarkRepository productBookmarkRepository;
    private final AuthService authService;

    public Member getCurrentMember() {  // 스프링 시큐리티 컨텍스트에서 사용자 정보 가져오기
        return memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(MemberNotFoundException::new);
    }

    public GetMemberInfoResponseDto getMemberInfo() {  //회원 정보 가져오기

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

        if(refreshTokenRepository.existsById(key)) {  // 리프레쉬 토큰이 남아있는지
            refreshTokenRepository.deleteById(key);
        }

        checkAndDeleteGeneralOrSocialMember(currentMember, currentMember.getId());  // 일반, 소셜 회원인지 확인 후 삭제
    }

    @Transactional
    public void editMemberInfo(EditMemberInfoRequestDto editMemberRequestDto) {  // 비밀번호 변경

        GeneralMember currentGeneralMember = authService.getMemberGeneral(getCurrentMember().getId());

        //현재 비밀번호를 입력받아서 회원 맞는지 체크 하기
        if(!passwordEncoder.matches(editMemberRequestDto.getPasswordToCheck(), currentGeneralMember.getPassword())) {
            throw new MemberNotEqualsException();
        }

        currentGeneralMember.changePassword(passwordEncoder.encode(editMemberRequestDto.getPasswordToChange()));  // 맞으면 변경
    }

    @Transactional
    public void cancelBookmark(String type, CancelBookmarkRequestDto cancelBookmarkRequestDto) {  // 현재 사용자가 찜한 store or product 목록에서 삭제
        checkAndCancelBookmark(type, cancelBookmarkRequestDto.getIdsToDelete());
    }

    private MemberInfo getMemberProfile(Long memberId) {
        return memberInfoRepository.findByMemberId(memberId)
                .orElseThrow(MemberInfoNotFoundException::new);
    }

    private Agreement getMemberAgreement(Long memberId) {
        return agreementRepository.findByMemberId(memberId)
                .orElseThrow(MemberAgreementNotFoundException::new);
    }
    private SocialMember getMemberSocial(Long memberId) {
        return socialMemberRepository.findByMemberId(memberId)
                .orElseThrow(SocialMemberNotFoundException::new);
    }
    private void checkAndDeleteGeneralOrSocialMember(Member currentMember, Long currentMemberId) {  // 일반, 소셜 회원인지 확인 후 삭제
        if(generalMemberRepository.existsByMemberId(currentMemberId)) {  // 일반 회원일 경우
            generalMemberRepository.delete(authService.getMemberGeneral(currentMemberId));
            memberInfoRepository.delete(getMemberProfile(currentMemberId));
        } else {  // 소셜 회원일 경우
            socialMemberRepository.delete(getMemberSocial(currentMemberId));
        }

        agreementRepository.delete(getMemberAgreement(currentMemberId));
        memberRepository.delete(currentMember);
    }
    private void checkAndCancelBookmark(String type, List<Long> idsToDelete) {
        if(type.equals("store")) {  // 타입이 store 일 경우
            for(Long id : idsToDelete) {
                storeBookmarkRepository.deleteStoreBookmarksByIds(idsToDelete);
            }
        } else if(type.equals("product")) {  // 타입이 product 일 경우
            for(Long id : idsToDelete) {
                productBookmarkRepository.deleteProductBookmarksByIds(idsToDelete);
            }
        } else {  // 타입이 존재하지 않을 경우
            throw new TypeDoesntExistException();
        }
    }
}

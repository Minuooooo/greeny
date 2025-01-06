package greeny.backend.domain.member.presentation.controller;

import greeny.backend.domain.member.presentation.dto.*;
import greeny.backend.infrastructure.mail.SimpleMailSender;
import greeny.backend.infrastructure.oauth.OAuthService;
import greeny.backend.domain.member.entity.Provider;
import greeny.backend.domain.member.application.AuthService;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import static greeny.backend.response.Response.*;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API")
@Slf4j
public class AuthController {

    private final SimpleMailSender simpleMailSender;
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(OK)
    @Operation(summary = "이메일 인증", description = "이메일을 입력해주세요")
    public Response sendEmail(@Valid @RequestBody AuthEmailRequestDto authEmailRequestDto)
            throws MessagingException, UnsupportedEncodingException {
        String email = authEmailRequestDto.getEmail();
        authService.validateSignUpInfoWithGeneral(email);
        return success(SUCCESS_TO_SEND_EMAIL, simpleMailSender.sendSimpleMessage(email, authEmailRequestDto.getAuthorizationUrl()));
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "토큰 유효성 검증", description = "토큰을 포함해주세요")
    public Response getTokenStatusInfo(@RequestHeader("Authorization") String bearerToken) {
        return success(SUCCESS_TO_VALIDATE_TOKEN, authService.getTokenStatusInfo(bearerToken));
    }

    @PostMapping("/sign-up")
    @ResponseStatus(CREATED)
    @Operation(summary = "회원가입", description = "이메일과 비밀번호를 규정에 맞게 입력해주세요")
    public Response signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return success(SUCCESS_TO_SIGN_UP);
    }

    @PostMapping("/sign-up/agreement")
    @ResponseStatus(CREATED)
    @Operation(summary = "Social sign up agreement API", description = "Put your social sign up agreement info.")
    public Response agreementInSignUp(@Valid @RequestBody AgreementRequestDto agreementRequestDto) {
        return success(SUCCESS_TO_SIGN_UP_AGREEMENT, authService.agreementInSignUp(agreementRequestDto));
    }

    @PostMapping("/sign-in/general")
    @ResponseStatus(OK)
    @Operation(summary = "General sign in API", description = "Put your sign in info")
    public Response signInWithGeneral(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return success(SUCCESS_TO_SIGN_IN, authService.signInWithGeneral(loginRequestDto));
    }

    @PostMapping("/sign-in/kakao")
    @ResponseStatus(OK)
    @Operation(summary = "Kakao sign in API", description = "Put your kakao sign in info")
    public Response signInWithKakao(@RequestBody KakaoSignInRequestDto kakaoSignInRequestDto) {
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(
                        oAuthService.requestToKakao(kakaoSignInRequestDto.getAuthorizationCode()).getKakaoAccount().getEmail(),
                        Provider.KAKAO
                )
        );
    }

    @PostMapping("/sign-in/naver")
    @ResponseStatus(OK)
    @Operation(summary = "Naver sign in API", description = "Put your naver sign in info")
    public Response signInWithNaver(@RequestBody NaverSignInRequestDto naverSignInRequestDto) {
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(
                        oAuthService.requestToNaver(
                                naverSignInRequestDto.getAuthorizationCode(),
                                naverSignInRequestDto.getState()
                        ).getResponse().getEmail(),
                        Provider.NAVER
                )
        );
    }

    @PatchMapping("/password")
    @ResponseStatus(OK)
    @Operation(summary = "Find password API", description = "Put your email")
    public Response findPassword(@Valid @RequestBody FindPasswordRequestDto findPasswordRequestDto) {
        authService.findPassword(findPasswordRequestDto);
        return success(SUCCESS_TO_FIND_PASSWORD);
    }

    @GetMapping("/auto/sign-in")
    @ResponseStatus(OK)
    @Operation(summary = "Auto sign in API", description = "Please auto sign in")
    public Response getIsAutoInfo() {
        return success(SUCCESS_TO_GET_IS_AUTO, authService.getIsAutoInfo(memberService.getCurrentMember().getId()));
    }

    @PostMapping("/reissue")
    @ResponseStatus(OK)
    @Operation(summary = "Token reissue API", description = "Put your token info")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(SUCCESS_TO_REISSUE, authService.reissue(tokenRequestDto));
    }
}
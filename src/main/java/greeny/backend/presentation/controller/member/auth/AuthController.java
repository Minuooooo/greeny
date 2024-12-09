package greeny.backend.presentation.controller.member.auth;

import greeny.backend.application.member.auth.EmailSender;
import greeny.backend.application.member.auth.OAuthService;
import greeny.backend.presentation.dto.member.auth.request.AgreementRequestDto;
import greeny.backend.presentation.dto.member.auth.request.TokenRequestDto;
import greeny.backend.presentation.dto.member.auth.request.AuthEmailRequestDto;
import greeny.backend.presentation.dto.member.auth.request.FindPasswordRequestDto;
import greeny.backend.presentation.dto.member.auth.request.LoginRequestDto;
import greeny.backend.presentation.dto.member.auth.request.SignUpRequestDto;
import greeny.backend.domain.member.Provider;
import greeny.backend.application.member.auth.AuthService;
import greeny.backend.application.member.MemberService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API Document")
public class AuthController {

    private final EmailSender emailSender;
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final MemberService memberService;

    // 이메일 전송 API
    @Operation(summary = "Authenticate email API", description = "put your email to authenticate.")
    @ResponseStatus(OK)
    @PostMapping()
    public Response sendEmail(@Valid @RequestBody AuthEmailRequestDto authEmailRequestDto) throws MessagingException, UnsupportedEncodingException {
        String email = authEmailRequestDto.getEmail();
        authService.validateSignUpInfoWithGeneral(email);
        return success(SUCCESS_TO_SEND_EMAIL, emailSender.sendSimpleMessage(email, authEmailRequestDto.getAuthorizationUrl()));
    }

    // 토큰 유효성 검증 API
    @Operation(summary = "Valid token API", description = "put your token info to what you want to validate.")
    @ResponseStatus(OK)
    @GetMapping()
    public Response getTokenStatusInfo(@RequestHeader("Authorization") String bearerToken) {
        return success(SUCCESS_TO_VALIDATE_TOKEN, authService.getTokenStatusInfo(bearerToken));
    }

    @Operation(summary = "Sign up API", description = "put your sign up info.")
    @ResponseStatus(CREATED)
    @PostMapping("/sign-up")
    public Response signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return success(SUCCESS_TO_SIGN_UP);
    }

    // 회원가입 or 소셜 로그인 동의 항목 체크 여부 요청받는 API
    @Operation(summary = "Social sign up agreement API", description = "put your social sign up agreement info.")
    @ResponseStatus(CREATED)
    @PostMapping("/sign-up/agreement")
    public Response agreementInSignUp(@Valid @RequestBody AgreementRequestDto agreementRequestDto) {
        return success(SUCCESS_TO_SIGN_UP_AGREEMENT, authService.agreementInSignUp(agreementRequestDto));
    }

    @Operation(summary = "General sign in API", description = "put your sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/general")
    public Response signInWithGeneral(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return success(SUCCESS_TO_SIGN_IN, authService.signInWithGeneral(loginRequestDto));
    }

    // 카카오 로그인 API
    @Operation(summary = "Kakao sign in API", description = "put your kakao sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/kakao")
    public Response signInWithKakao(String authorizationCode) {  // Query parameter
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(oAuthService.requestToKakao(authorizationCode).getKakaoAccount().getEmail(), Provider.KAKAO));
    }

    // 네이버 로그인 API
    @Operation(summary = "Naver sign in API", description = "put your naver sign in info.")
    @ResponseStatus(OK)
    @PostMapping("/sign-in/naver")
    public Response signInWithNaver(String authorizationCode, String state) {  // Query parameter
        return success(
                SUCCESS_TO_SIGN_IN,
                authService.signInWithSocial(oAuthService.requestToNaver(authorizationCode, state).getResponse().getEmail(), Provider.NAVER));
    }

    @Operation(summary = "Find password API", description = "put your email.")
    @ResponseStatus(OK)
    @PatchMapping("/password")
    public Response findPassword(@Valid @RequestBody FindPasswordRequestDto findPasswordRequestDto) {
        authService.findPassword(findPasswordRequestDto);
        return success(SUCCESS_TO_FIND_PASSWORD);
    }

    @Operation(summary = "Auto sign in API", description = "please auto sign in.")
    @ResponseStatus(OK)
    @GetMapping("/auto/sign-in")
    public Response getIsAutoInfo() {
        return success(SUCCESS_TO_GET_IS_AUTO, authService.getIsAutoInfo(memberService.getCurrentMember().getId()));
    }

    @Operation(summary = "Token reissue API", description = "put your token info")
    @ResponseStatus(OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(SUCCESS_TO_REISSUE, authService.reissue(tokenRequestDto));
    }
}
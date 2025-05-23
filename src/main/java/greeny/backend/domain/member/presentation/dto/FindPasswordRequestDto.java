package greeny.backend.domain.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FindPasswordRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9]+@(gmail\\.com|naver\\.com)$", message = "이메일은 google, naver 메일만 사용 가능합니다.")
    @Schema(description = "이메일", defaultValue = "test@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,15}$", message = "비밀번호는 영숫자, 특수문자가 필수이고 8자리 이상 15이내여야 합니다.")
    @Schema(description = "비밀번호", defaultValue = "test5678@")
    private String password;
}
package greeny.backend.presentation.dto.member.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
}

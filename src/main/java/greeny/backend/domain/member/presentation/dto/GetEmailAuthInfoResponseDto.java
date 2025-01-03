package greeny.backend.domain.member.presentation.dto;

import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetEmailAuthInfoResponseDto {
    private String email;
    private String token;
}
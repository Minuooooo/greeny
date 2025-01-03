package greeny.backend.domain.member;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class RefreshToken {
    @Id
    @Column(name = "refresh_token_key")
    private String key;

    @Column(name = "refresh_token_value")
    private String value;
}
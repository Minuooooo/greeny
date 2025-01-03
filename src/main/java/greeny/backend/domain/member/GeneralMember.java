package greeny.backend.domain.member;

import greeny.backend.domain.AuditEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class GeneralMember extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "general_member_id")
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long memberId;

    public void changePassword(String password) {
        this.password = password;
    }
}
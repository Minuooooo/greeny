package greeny.backend.domain.member.entity;

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
    private Long memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isAuto;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }
}
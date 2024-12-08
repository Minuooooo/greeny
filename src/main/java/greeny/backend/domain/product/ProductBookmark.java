package greeny.backend.domain.product;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBookmark extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_bookmark_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "liker_id")
    private Member liker;
}
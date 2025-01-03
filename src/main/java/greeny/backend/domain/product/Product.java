package greeny.backend.domain.product;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.store.Store;
import lombok.*;
import org.hibernate.annotations.Formula;
import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Product extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Formula("(select count(*) from product_bookmark pb where pb.product_id = product_id)")
    private int bookmarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
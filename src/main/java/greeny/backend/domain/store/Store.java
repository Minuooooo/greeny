package greeny.backend.domain.store;

import greeny.backend.domain.AuditEntity;
import lombok.*;
import org.hibernate.annotations.Formula;
import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Store extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Formula("(select count(1) from store_bookmark sb where sb.store_id = store_id)")
    private int bookmarks;

    @Formula("(select count(1) from store_review sr where sr.store_id = store_id)")
    private int reviews;
}
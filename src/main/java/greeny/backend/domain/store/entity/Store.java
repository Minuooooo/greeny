package greeny.backend.domain.store.entity;

import greeny.backend.domain.AuditEntity;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.wishlist.entity.StoreWishlist;
import lombok.*;
import org.hibernate.annotations.Formula;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;

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

    @OneToMany(mappedBy = "store", cascade = ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL, orphanRemoval = true)
    private List<StoreReview> storeReviews = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL, orphanRemoval = true)
    private Set<StoreWishlist> storeWishlists = new HashSet<>();

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String webUrl;

    @Column(nullable = false)
    private String location;

    private String phone;

    private String imageUrl;

    private String runningTime;

    @Formula("(select count(1) from store_wishlist sw where sw.store_id = store_id)")
    private int wishlists;

    @Formula("(select count(1) from store_review sr where sr.store_id = store_id)")
    private int reviews;
}
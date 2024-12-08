package greeny.backend.domain.review;


import greeny.backend.domain.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewImage extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_review_image_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_review_id")
    private ProductReview productReview;

    private String imageUrl;

    public ProductReviewImage getEntity(ProductReview productReview, String url) {
        return ProductReviewImage.builder()
                .productReview(productReview)
                .imageUrl(url)
                .build();
    }
}

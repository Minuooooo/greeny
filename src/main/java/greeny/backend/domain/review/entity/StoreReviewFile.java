package greeny.backend.domain.review.entity;

import greeny.backend.domain.AuditEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class StoreReviewFile extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_review_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_review_id")
    private StoreReview storeReview;

    private String fileUrl;

    public static StoreReviewFile getEntity(StoreReview storeReview, String url) {
        return StoreReviewFile.builder()
                .storeReview(storeReview)
                .fileUrl(url)
                .build();
    }
}
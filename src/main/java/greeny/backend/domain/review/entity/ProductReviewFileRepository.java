package greeny.backend.domain.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductReviewFileRepository extends JpaRepository<ProductReviewFile, Long> {
    List<ProductReviewFile> findByProductReviewId(Long reviewId);
}
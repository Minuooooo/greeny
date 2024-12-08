package greeny.backend.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, Long> {
    public List<ProductReviewImage> findByProductReviewId(Long reviewId);
}

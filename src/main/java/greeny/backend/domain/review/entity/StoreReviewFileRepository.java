package greeny.backend.domain.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoreReviewFileRepository extends JpaRepository<StoreReviewFile, Long> {
    List<StoreReviewFile> findByStoreReviewId(Long reviewId);
}
package greeny.backend.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreReviewImageRepository extends JpaRepository<StoreReviewImage, Long> {
    public List<StoreReviewImage> findByStoreReviewId(Long reviewId);
}

package greeny.backend.domain.review;

import greeny.backend.domain.member.Member;
import greeny.backend.domain.store.Store;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
    Page<StoreReview> findStoreReviewsByStore(Pageable pageable, Store store);
    @EntityGraph(attributePaths = "store")
    Page<StoreReview> findAllByReviewer(Pageable pageable, Member member);
    @EntityGraph(attributePaths = "store")
    Page<StoreReview> findAllByContentContainingIgnoreCase(String content, Pageable pageable);
    @EntityGraph(attributePaths = "store")
    @NotNull
    Page<StoreReview> findAll(@NotNull Pageable pageable);
}

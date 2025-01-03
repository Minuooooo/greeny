package greeny.backend.domain.product;

import greeny.backend.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ProductBookmark pb where pb.id in :ids")
    void deleteProductBookmarksByIds(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"product"})
    List<ProductBookmark> findProductBookmarksByLiker(Member liker);

    @EntityGraph(attributePaths = {"product"})
    Page<ProductBookmark> findProductBookmarksByLiker(Pageable pageable , Member liker);

    Optional<ProductBookmark> findByProductIdAndLikerId(Long productId, Long likerId);
}
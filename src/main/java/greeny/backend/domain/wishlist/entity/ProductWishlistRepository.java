package greeny.backend.domain.wishlist.entity;

import greeny.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductWishlistRepository extends JpaRepository<ProductWishlist, Long> {

    Optional<ProductWishlist> findByProductIdAndMemberId(Long productId, Long memberId);

    @EntityGraph(attributePaths = {"product"})
    List<ProductWishlist> findProductWishlistsByMember(Member member);

    @EntityGraph(attributePaths = {"product"})
    Page<ProductWishlist> findProductWishlistsByMember(Pageable pageable , Member member);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ProductWishlist pw where pw.id in :ids")
    void deleteProductWishlistsByIds(@Param("ids") List<Long> ids);
}
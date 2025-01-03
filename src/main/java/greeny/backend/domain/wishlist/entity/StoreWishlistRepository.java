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

public interface StoreWishlistRepository extends JpaRepository<StoreWishlist, Long> {

    Optional<StoreWishlist> findByStoreIdAndMemberId(Long storeId, Long memberId);

    @EntityGraph(attributePaths = {"store"})
    List<StoreWishlist> findStoreWishlistsByMember(Member member);

    @EntityGraph(attributePaths = {"store"})
    Page<StoreWishlist> findStoreWishlistsByMember(Pageable pageable , Member member);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from StoreWishlist sw where sw.id in :ids")
    void deleteStoreWishlistsByIds(@Param("ids") List<Long> ids);
}
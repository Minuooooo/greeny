package greeny.backend.domain.post.entity;

import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndLiker(Long postId, Member liker);

    boolean existsByPostIdAndLikerId(Long postId, Long likerId);
}
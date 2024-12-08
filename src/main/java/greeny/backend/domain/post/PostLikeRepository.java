package greeny.backend.domain.post;

import greeny.backend.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PostLike> findByPostIdAndLiker(Long postId, Member liker);
    boolean existsByPostIdAndLikerId(Long postId, Long likerId);
}

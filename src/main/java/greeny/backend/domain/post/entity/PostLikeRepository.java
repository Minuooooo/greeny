package greeny.backend.domain.post.entity;

import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndLiker(Post post, Member liker);

    boolean existsByPostAndLiker(Post post, Member liker);
}
package greeny.backend.domain.post.entity;

import greeny.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndLiker(Post post, Member liker);
}
package greeny.backend.domain.post.application;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.post.entity.PostLike;
import greeny.backend.domain.post.entity.PostLikeRepository;
import greeny.backend.domain.post.entity.PostRepository;
import greeny.backend.exception.situation.post.PostNotFoundException;
import greeny.backend.exception.situation.post.SelfLikeNotAllowedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public void like(Long postId, Member liker) {
        toggle(postId, liker, findPostLike(postId, liker));
    }

    public Optional<PostLike> findPostLike(Long postId, Member liker) {
        return postLikeRepository.findByPostAndLiker(getPost(postId), liker);
    }

    public void toggle(Long postId, Member liker, Optional<PostLike> postLike) {
        if (postLike.isEmpty()) {
            create(postId, liker);
            return;
        }
        delete(postLike.get());
    }

    public void create(Long postId, Member liker) {
        Post post = getPostWithWriter(postId);
        if (post.getWriter().getId().equals(liker.getId())) {
            throw new SelfLikeNotAllowedException();
        }
        postLikeRepository.save(
                PostLike.builder()
                        .post(post)
                        .liker(liker)
                        .build()
        );
    }

    private void delete(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public Post getPostWithWriter(Long postId) {
        return postRepository.findByIdWithWriter(postId).orElseThrow(PostNotFoundException::new);
    }
}
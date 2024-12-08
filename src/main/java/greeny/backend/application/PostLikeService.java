package greeny.backend.application;

import greeny.backend.domain.post.Post;
import greeny.backend.domain.post.PostLike;
import greeny.backend.domain.post.PostLikeRepository;
import greeny.backend.domain.post.PostRepository;
import greeny.backend.domain.member.Member;
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
        log.info("like");
        toggle(postId, liker, findPostLike(postId, liker));
    }

    public Optional<PostLike> findPostLike(Long postId, Member liker) {
        log.info("find post like");
        return postLikeRepository.findByPostIdAndLiker(postId, liker);
    }

    public void toggle(Long postId, Member liker, Optional<PostLike> postLike) {
        log.info("toggle");
        if (postLike.isEmpty()) {
            create(postId, liker);
            return;
        }
        log.info("delete");
        delete(postLike.get());
    }

    public void create(Long postId, Member liker) {
        log.info("create");
        Post post = getPost(postId);
        if (post.getWriter().getId().equals(liker.getId()))
            throw new SelfLikeNotAllowedException();
        log.info("save post like");
        postLikeRepository.save(PostLike.builder()
                .post(post)
                .liker(liker)
                .build());
    }

    private void delete(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }

    public Post getPost(Long postId) {
        log.info("get post");
        return postRepository.findByIdWithWriter(postId).orElseThrow(PostNotFoundException::new);
    }
}

package greeny.backend.domain.post.application;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.post.entity.PostLike;
import greeny.backend.domain.post.entity.PostLikeRepository;
import greeny.backend.domain.post.entity.PostRepository;
import greeny.backend.exception.situation.post.PostLikeNotFoundException;
import greeny.backend.exception.situation.post.PostNotFoundException;
import greeny.backend.exception.situation.post.SelfLikeNotAllowedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

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

    @Transactional
    public void cancel(Long postId, Member liker) {
        Post foundPost = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        postLikeRepository.delete(
                postLikeRepository.findByPostAndLiker(foundPost, liker).orElseThrow(PostLikeNotFoundException::new)
        );
    }

    public Post getPostWithWriter(Long postId) {
        return postRepository.findByIdWithWriter(postId).orElseThrow(PostNotFoundException::new);
    }
}
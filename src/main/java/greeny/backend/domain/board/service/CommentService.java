package greeny.backend.domain.board.service;

import greeny.backend.domain.board.dto.CreateCommentRequestDto;
import greeny.backend.domain.board.dto.GetCommentListResponseDto;
import greeny.backend.domain.board.entity.Comment;
import greeny.backend.domain.board.entity.Post;
import greeny.backend.domain.board.repository.CommentRepository;
import greeny.backend.domain.board.repository.PostRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.exception.situation.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void creatComment(Long postId, CreateCommentRequestDto createCommentRequestDto, Member writer) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        commentRepository.save(createCommentRequestDto.toEntity(post ,writer));
    }

    @Transactional(readOnly = true)
    public List<GetCommentListResponseDto> getComments(Long postId) {
        return commentRepository.findAllByPostId(postId).stream().
                map(GetCommentListResponseDto::from).
                collect(Collectors.toList());
         
    }
}

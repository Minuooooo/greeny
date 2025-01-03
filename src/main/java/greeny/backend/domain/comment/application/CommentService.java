package greeny.backend.domain.comment.application;

import greeny.backend.domain.comment.presentation.dto.WriteCommentRequestDto;
import greeny.backend.domain.comment.entity.Comment;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.post.entity.Post;
import greeny.backend.domain.comment.entity.CommentRepository;
import greeny.backend.domain.post.entity.PostRepository;
import greeny.backend.domain.comment.presentation.dto.GetSimpleCommentInfosResponseDto;
import greeny.backend.exception.situation.comment.CommentNotFoundException;
import greeny.backend.exception.situation.member.MemberNotEqualsException;
import greeny.backend.exception.situation.post.PostNotFoundException;
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
    public void writeComment(Long postId, WriteCommentRequestDto writeCommentRequestDto, Member writer) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        commentRepository.save(writeCommentRequestDto.toEntity(post, writer));
    }

    @Transactional(readOnly = true)
    public List<GetSimpleCommentInfosResponseDto> getSimpleCommentInfos(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException();
        }
        return commentRepository.findAllByPostId(postId).stream()
                .map(comment -> GetSimpleCommentInfosResponseDto.from(comment, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetSimpleCommentInfosResponseDto> getSimpleCommentInfosWithAuthMember(Long postId, Member currentMember) {
        if(!postRepository.existsById(postId)) {
            throw new PostNotFoundException();
        }
        return commentRepository.findAllByPostId(postId).stream()
                .map(comment -> GetSimpleCommentInfosResponseDto.from(comment, isWriter(comment, currentMember)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Boolean isWriter(Comment comment, Member currentMember){
        return comment.getWriter().getId().equals(currentMember.getId());
    }

    @Transactional
    public void editCommentInfo(Long commentId, WriteCommentRequestDto editCommentInfoRequestDto, Member currentMember) {
        Comment comment = commentRepository.findByIdWithWriter(commentId).orElseThrow(CommentNotFoundException::new);
        if(!comment.getWriter().getId().equals(currentMember.getId())) {
            throw new MemberNotEqualsException();
        }
        comment.update(editCommentInfoRequestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, Member currentMember) {
        Comment comment = commentRepository.findByIdWithWriter(commentId).orElseThrow(CommentNotFoundException::new);
        if(!comment.getWriter().getId().equals(currentMember.getId())) {
            throw new MemberNotEqualsException();
        }
        commentRepository.delete(comment);
    }
}
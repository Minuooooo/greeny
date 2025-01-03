package greeny.backend.domain.comment.presentation.controller;

import greeny.backend.domain.comment.presentation.dto.WriteCommentRequestDto;
import greeny.backend.domain.comment.application.CommentService;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/comments")
@Tag(name = "Comment", description = "Comment API Document")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(OK)
    @Operation(summary = "Write comment API", description = "Put post id and comment info to write.")
    public Response writeComment(Long postId, @Valid @RequestBody WriteCommentRequestDto writeCommentRequestDto) {
        commentService.writeComment(postId, writeCommentRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_WRITE_COMMENT);
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Get simple comment infos API", description = "Put post id to get comment list.")
    public Response getSimpleCommentInfos(Long postId){
        return success(SUCCESS_TO_GET_COMMENT_LIST, commentService.getSimpleCommentInfos(postId));
    }

    @GetMapping("/auth")
    @ResponseStatus(OK)
    @Operation(summary = "Get simple comment infos with auth member API", description = "Put post id to get comment list.")
    public Response getSimpleCommentInfosWithAuthMember(Long postId){
        return success(SUCCESS_TO_GET_COMMENT_LIST, commentService.getSimpleCommentInfosWithAuthMember(postId, memberService.getCurrentMember()));
    }

    @PutMapping
    @ResponseStatus(OK)
    @Operation(summary = "Edit comment info API", description = "Put comment info to edit.")
    public Response editCommentInfo(Long commentId, @Valid @RequestBody WriteCommentRequestDto editCommentInfoRequestDto) {
        commentService.editCommentInfo(commentId, editCommentInfoRequestDto, memberService.getCurrentMember());
        return success(SUCCESS_TO_EDIT_COMMENT);
    }

    @DeleteMapping
    @ResponseStatus(OK)
    @Operation(summary = "Delete comment API", description = "Put comment info to delete")
    public Response deleteComment(@RequestParam Long commentId){
        commentService.deleteComment(commentId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_DELETE_COMMENT);
    }
}
package greeny.backend.domain.post.presentation.controller;

import greeny.backend.domain.post.application.PostLikeService;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts/like")
@Tag(name = "Post Like", description = "Post Like API Document")
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(OK)
    @Operation(summary = "Like post API", description = "Put post id what you want to like")
    public Response create(Long postId) {
        postLikeService.create(postId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_CREATE_POST_LIKE);
    }

    @DeleteMapping
    @ResponseStatus(OK)
    @Operation(summary = "Cancel post like API", description = "Put post id what you want to cancel")
    public Response cancel(Long postId) {
        postLikeService.cancel(postId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_CANCEL_POST_LIKE);
    }
}
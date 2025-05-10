package greeny.backend.domain.post.presentation.controller;

import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.post.presentation.dto.WritePostRequestDto;
import greeny.backend.domain.post.application.PostService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts")
@Tag(name = "Post", description = "Post API Document")
@Slf4j
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Write post API", description = "Put your post info to write. you can skip files.")
    public Response writePost(
            @Valid @RequestPart(name = "body(json)") WritePostRequestDto writePostRequestDto,
            @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        List<MultipartFile> postFiles = multipartFiles != null ? multipartFiles : Collections.emptyList();
        postService.writePost(writePostRequestDto, postFiles, memberService.getCurrentMember());
        return success(SUCCESS_TO_WRITE_POST);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @Operation(summary = "Search simple post infos API",
            description = "Put keyword and page info what you want, you can skip parameters. " +
                    " sort=id,desc : 일반 게시판 (최신순). " +
                    " sort=likes,desc&sort=hits,desc : 베스트 게시판 (좋아요순 + 조회수순).")
    public Response searchSimplePostInfos(
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    ) {
        return Response.success(SUCCESS_TO_SEARCH_POST_LIST, postService.searchSimplePostInfos(keyword, pageable));
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Get post info API", description = "Put post id what you want to see.")
    public Response getPostInfo(Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPostInfo(postId));

    }

    @GetMapping("/auth")
    @ResponseStatus(OK)
    @Operation(summary = "Get post info with auth member API", description = "Put post id what you want to see.")
    public Response getPostInfoWithAuthMember(Long postId){
        return Response.success(SUCCESS_TO_GET_POST, postService.getPostInfoWithAuthMember(postId, memberService.getCurrentMember()));
    }

    @DeleteMapping
    @ResponseStatus(OK)
    @Operation(summary = "Delete post API", description = "Put post id what you want to delete.")
    public Response deletePost(Long postId){
        postService.deletePost(postId, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_DELETE_POST);
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Edit post info API", description = "Put post info what you want to edit.")
    public Response editPostInfo(
            Long postId,
            @Valid @RequestPart(name = "body(json)") WritePostRequestDto editPostInfoRequestDto,
            @RequestPart(name = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        List<MultipartFile> postFiles = multipartFiles != null ? multipartFiles : Collections.emptyList();
        postService.editPostInfo(postId, editPostInfoRequestDto, postFiles, memberService.getCurrentMember());
        return Response.success(SUCCESS_TO_EDIT_POST);
    }
}
package greeny.backend.domain.member.presentation.controller;

import greeny.backend.domain.post.application.PostService;
import greeny.backend.domain.member.presentation.dto.CancelWishlistRequestDto;
import greeny.backend.domain.member.presentation.dto.EditMemberInfoRequestDto;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.review.application.ReviewService;
import greeny.backend.domain.wishlist.application.WishlistService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members")
@Tag(name = "Member", description = "Member API Document")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final WishlistService wishlistService;

    @PatchMapping
    @ResponseStatus(OK)
    @Operation(summary = "Edit member info API", description = "Put info what you want to change")
    public Response editMemberInfo(@Valid @RequestBody EditMemberInfoRequestDto editMemberRequestDto) {
        memberService.editMemberInfo(editMemberRequestDto);
        return success(SUCCESS_TO_EDIT_MEMBER_PASSWORD);
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Get member info API")
    public Response getMemberInfo() {
        return success(SUCCESS_TO_GET_MEMBER_INFO, memberService.getMemberInfo());
    }

    @DeleteMapping
    @ResponseStatus(OK)
    @Operation(summary = "Delete member API", description = "This is to delete member")
    public Response deleteMember() {
        memberService.deleteMember();
        return success(SUCCESS_TO_DELETE_MEMBER);
    }

    @DeleteMapping("/wishlist")
    @ResponseStatus(OK)
    @Operation(summary = "Delete store or product wishlist API", description = "Put your store or product id what you want to delete")
    public Response cancelBookmark(String type, @RequestBody CancelWishlistRequestDto cancelWishlistRequestDto) {
        memberService.cancelBookmark(type, cancelWishlistRequestDto);
        return success(SUCCESS_TO_CANCEL_WISHLIST);
    }

    @GetMapping("/post")
    @ResponseStatus(OK)
    @Operation(summary = "Get my post simple infos API", description = "Put page info what you want, you can skip parameters")
    public Response getMySimplePostInfos(@ParameterObject Pageable pageable) {
        return Response.success(
                SUCCESS_TO_GET_POST_LIST, postService.getMySimplePostInfos(pageable, memberService.getCurrentMember())
        );
    }

    @GetMapping("/review")
    @ResponseStatus(OK)
    @Operation(summary = "Get my review list api", description = "Put page info what you want. you can skip parameters")
    public Response getMyReviewList(String type,@ParameterObject Pageable pageable) {
        return Response.success(
                SUCCESS_TO_GET_REVIEW_LIST, reviewService.getMemberReviewList(type, pageable, memberService.getCurrentMember())
        );
    }

    @GetMapping("/simple/store-wishlist")
    @ResponseStatus(OK)
    @Operation(summary = "Get my store wishlists api")
    public Response getSimpleStoreBookmarkInfos(@ParameterObject Pageable pageable) {
        return Response.success(
                SUCCESS_TO_GET_STORE_WISHLIST, wishlistService.getSimpleStoreWishlistsInfo(pageable, memberService.getCurrentMember())
        );
    }

    @GetMapping("/simple/product-wishlist")
    @ResponseStatus(OK)
    @Operation(summary = "Get my product wishlists api")
    public Response getSimpleProductBookmarkInfos(@ParameterObject Pageable pageable) {
        return Response.success(
                SUCCESS_TO_GET_PRODUCT_WISHLIST, wishlistService.getSimpleProductWishlistsInfo(pageable, memberService.getCurrentMember())
        );
    }
}
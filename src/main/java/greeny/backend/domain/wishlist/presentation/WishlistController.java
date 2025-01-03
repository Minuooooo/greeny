package greeny.backend.domain.wishlist.presentation;

import greeny.backend.domain.wishlist.application.WishlistService;
import greeny.backend.domain.member.application.MemberService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.SUCCESS_TO_TOGGLE_WISHLIST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlists")
@Tag(name = "Wishlist", description = "Wishlist API Document")
@Slf4j
public class WishlistController {

    private final MemberService memberService;
    private final WishlistService wishlistService;

    @PostMapping
    @ResponseStatus(OK)
    @Operation(summary = "Toggle store or product wishlist API", description = "Put type info and store or product id what you want to toggle.")
    public Response toggleBookmark(String type, Long id) {
        wishlistService.toggleStoreWishlist(type, id, memberService.getCurrentMember());
        return success(SUCCESS_TO_TOGGLE_WISHLIST);
    }
}
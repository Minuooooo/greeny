package greeny.backend.domain.store.presentation.controller;

import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.store.application.StoreService;
import greeny.backend.domain.wishlist.application.WishlistService;
import greeny.backend.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@Tag(name = "Store", description = "Store API Document")
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final WishlistService wishlistService;
    private final MemberService memberService;

    @GetMapping("/simple")
    @ResponseStatus(OK)
    @Operation(summary = "Get simple store infos API", description = "Put keyword if you want to search and page info what you want to see.")
    public Response getSimpleStoreInfos(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @ParameterObject Pageable pageable
    ) {
        return success(SUCCESS_TO_GET_SIMPLE_STORE_INFOS, storeService.getSimpleStoreInfos(keyword, location, category, pageable));
    }

    @GetMapping("/auth/simple")
    @ResponseStatus(OK)
    @Operation(
            summary = "Get simple store infos with auth member API",
            description = "Put keyword if you want to search and page info what you want to see."
    )
    public Response getSimpleStoreInfosWithAuthMember(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @ParameterObject Pageable pageable
    ) {
        return success(
                SUCCESS_TO_GET_SIMPLE_STORE_INFOS,
                storeService.getSimpleStoreInfosWithAuthMember(
                        keyword,
                        location,
                        category,
                        wishlistService.getStoreWishlists(memberService.getCurrentMember()),
                        pageable
                )
        );
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Get store info API", description = "Put store id what you want to see.")
    public Response getStoreInfo(Long storeId) {
        return success(SUCCESS_TO_GET_STORE_INFO, storeService.getStoreInfo(storeId));
    }

    @GetMapping("/auth")
    @ResponseStatus(OK)
    @Operation(summary = "Get store info with auth member API", description = "Put store id what you want to see.")
    public Response getStoreInfoWithAuthMember(Long storeId) {
        return success(
                SUCCESS_TO_GET_STORE_INFO,
                storeService.getStoreInfoWithAuthMember(
                        storeId,
                        wishlistService.getOptionalStoreWishlist(storeId, memberService.getCurrentMember().getId())
                )
        );
    }
}
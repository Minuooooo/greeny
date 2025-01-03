package greeny.backend.domain.product.presentation.controller;

import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.product.application.ProductService;
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
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product API Document")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final MemberService memberService;

    @GetMapping("/simple")
    @ResponseStatus(OK)
    @Operation(summary = "Get simple product infos API", description = "Please get product store infos.")
    public Response getSimpleProductInfos(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return success(SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS, productService.getSimpleProductInfos(keyword, pageable));
    }

    @GetMapping("/auth/simple")
    @ResponseStatus(OK)
    @Operation(summary = "Get simple product infos with auth member API", description = "Please get product store infos.")
    public Response getSimpleProductInfosWithAuthMember(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return success(
                SUCCESS_TO_GET_SIMPLE_PRODUCT_INFOS,
                productService.getSimpleProductInfosWithAuthMember(
                        keyword,
                        wishlistService.getProductWishlists(memberService.getCurrentMember()),
                        pageable
                )
        );
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Get product info API", description = "Put product id what you want to see.")
    public Response getProductInfo(Long productId) {
        return success(SUCCESS_TO_GET_PRODUCT_INFO, productService.getProductInfo(productId));
    }

    @GetMapping("/auth")
    @ResponseStatus(OK)
    @Operation(summary = "Get product info with auth member API", description = "Put product id what you want to see.")
    public Response getProductInfoWithAuthMember(Long productId) {
        return success(
                SUCCESS_TO_GET_PRODUCT_INFO,
                productService.getProductInfoWithAuthMember(
                        productId,
                        wishlistService.getOptionalProductWishlist(productId, memberService.getCurrentMember().getId())
                )
        );
    }
}
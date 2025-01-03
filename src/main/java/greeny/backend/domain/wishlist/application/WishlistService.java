package greeny.backend.domain.wishlist.application;

import greeny.backend.domain.wishlist.entity.ProductWishlist;
import greeny.backend.domain.wishlist.entity.StoreWishlist;
import greeny.backend.domain.wishlist.entity.ProductWishlistRepository;
import greeny.backend.domain.wishlist.entity.StoreWishlistRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.product.presentation.dto.GetSimpleProductInfosResponseDto;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.application.ProductService;
import greeny.backend.domain.store.presentation.dto.GetSimpleStoreInfosResponseDto;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.application.StoreService;
import greeny.backend.exception.situation.common.TypeDoesntExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static greeny.backend.domain.Target.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {

    private final StoreWishlistRepository storeWishlistRepository;
    private final ProductWishlistRepository productWishlistRepository;
    private final StoreService storeService;
    private final ProductService productService;

    public List<StoreWishlist> getStoreWishlists(Member liker) {
        return storeWishlistRepository.findStoreWishlistsByMember(liker);
    }

    public List<ProductWishlist> getProductWishlists(Member liker){
        return productWishlistRepository.findProductWishlistsByMember(liker);
    }

    @Transactional(readOnly = true)
    public Page<GetSimpleStoreInfosResponseDto> getSimpleStoreWishlistsInfo(Pageable pageable, Member liker) {
        return storeWishlistRepository.findStoreWishlistsByMember(pageable, liker)
                .map(storeWishlist -> GetSimpleStoreInfosResponseDto.from(storeWishlist.getStore(), true));
    }

    @Transactional(readOnly = true)
    public Page<GetSimpleProductInfosResponseDto> getSimpleProductWishlistsInfo(Pageable pageable, Member liker) {
        return productWishlistRepository.findProductWishlistsByMember(pageable, liker)
                .map(productWishlist -> GetSimpleProductInfosResponseDto.from(productWishlist.getProduct(),true));
    }

    public void toggleStoreWishlist(String type, Long id, Member liker) {
        if (type.equals(STORE.toString())) {
            checkAndToggleStoreWishlistBySituation(storeService.getStore(id), liker);
        }
        else if (type.equals(PRODUCT.toString())) {
            toggleProductWishlist(productService.getProduct(id), liker);
        }
        else {
            throw new TypeDoesntExistException();
        }
    }

    private void checkAndToggleStoreWishlistBySituation(Store store, Member liker) {
        Optional<StoreWishlist> storeWishlist = getOptionalStoreWishlist(store.getId(), liker.getId());
        if (storeWishlist.isPresent()) {
            storeWishlistRepository.delete(storeWishlist.get());
        }
        else {
            storeWishlistRepository.save(toEntity(store, liker));
        }
    }

    private void toggleProductWishlist(Product product, Member liker) {
        Optional<ProductWishlist> productWishlist = getOptionalProductWishlist(product.getId(), liker.getId());
        if (productWishlist.isPresent()) {
            productWishlistRepository.delete(productWishlist.get());
        }
        else {
            productWishlistRepository.save(toEntity(product, liker));
        }
    }

    private StoreWishlist toEntity(Store store, Member liker) {
        return StoreWishlist.builder()
                .store(store)
                .member(liker)
                .build();
    }

    private ProductWishlist toEntity(Product product, Member liker) {
        return ProductWishlist.builder()
                .product(product)
                .member(liker)
                .build();
    }

    public Optional<StoreWishlist> getOptionalStoreWishlist(Long storeId, Long likerId) {
        return storeWishlistRepository.findByStoreIdAndMemberId(storeId, likerId);
    }

    public Optional<ProductWishlist> getOptionalProductWishlist(Long productId, Long likerId) {
        return productWishlistRepository.findByProductIdAndMemberId(productId, likerId);
    }
}
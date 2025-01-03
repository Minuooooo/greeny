package greeny.backend.domain.store.presentation.dto;

import greeny.backend.domain.store.entity.Store;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetSimpleStoreInfosResponseDto {

    private Long id;
    private String category;
    private String name;
    private String imageUrl;
    private String location;
    private Boolean isWishlist;

    public static GetSimpleStoreInfosResponseDto from(Store store, boolean isWishlist) {
        return GetSimpleStoreInfosResponseDto.builder()
                .id(store.getId())
                .category(store.getCategory())
                .name(store.getName())
                .imageUrl(store.getImageUrl())
                .location(store.getLocation().substring(0, 2))
                .isWishlist(isWishlist)
                .build();
    }
}
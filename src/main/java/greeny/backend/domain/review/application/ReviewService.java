package greeny.backend.domain.review.application;

import greeny.backend.domain.member.entity.Member;
import greeny.backend.exception.situation.common.TypeDoesntExistException;
import greeny.backend.exception.situation.member.MemberNotEqualsException;
import greeny.backend.exception.situation.product.ProductNotFoundException;
import greeny.backend.exception.situation.review.ReviewNotFoundException;
import greeny.backend.exception.situation.store.StoreNotFoundException;
import greeny.backend.infrastructure.aws.S3Service;
import greeny.backend.domain.product.entity.Product;
import greeny.backend.domain.product.entity.ProductRepository;
import greeny.backend.domain.review.presentation.dto.GetReviewListResponseDto;
import greeny.backend.domain.review.presentation.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.presentation.dto.GetReviewInfoResponseDto;
import greeny.backend.domain.review.entity.ProductReview;
import greeny.backend.domain.review.entity.StoreReview;
import greeny.backend.domain.review.entity.ProductReviewRepository;
import greeny.backend.domain.review.entity.StoreReviewRepository;
import greeny.backend.domain.review.entity.ProductReviewFile;
import greeny.backend.domain.review.entity.StoreReviewFile;
import greeny.backend.domain.review.entity.ProductReviewFileRepository;
import greeny.backend.domain.review.entity.StoreReviewFileRepository;
import greeny.backend.domain.store.entity.Store;
import greeny.backend.domain.store.entity.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

import static greeny.backend.domain.Target.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final ProductReviewFileRepository productReviewFileRepository;
    private final StoreReviewFileRepository storeReviewFileRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final S3Service s3Service;

    @Transactional
    public void writeStoreReview(Long id, WriteReviewRequestDto writeReviewRequestDto, List<MultipartFile> multipartFiles, Member member) {
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        StoreReview storeReview = storeReviewRepository.save(writeReviewRequestDto.toStoreReviewEntity(member, store));
        if (multipartFiles != null) {
            uploadFiles(multipartFiles, storeReview);
        }
    }

    @Transactional
    public void writeProductReview(Long id, WriteReviewRequestDto writeReviewRequestDto, List<MultipartFile> multipartFiles, Member member) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        ProductReview productReview = productReviewRepository.save(writeReviewRequestDto.toProductReviewEntity(member, product));
        if (multipartFiles != null) {
            uploadFiles(multipartFiles, productReview);
        }
    }

    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> getMemberReviewList(String type, Pageable pageable, Member member) {
        if (type.equals(STORE.toString())) {
            return storeReviewRepository.findAllByReviewer(pageable, member)
                    .map(storeReview -> GetReviewListResponseDto.toDetailStoreDto(storeReview, type, storeReview.getStore().getId()));
        }
        else if (type.equals(PRODUCT.toString())) {
            return productReviewRepository.findAllByReviewer(pageable, member)
                    .map(productReview -> GetReviewListResponseDto.toDetailProductDto(productReview, type, productReview.getProduct().getId()));
        }
        throw new TypeDoesntExistException();
    }

    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> searchSimpleReviewInfos(String keyword, String type, Pageable pageable) {
        if (!StringUtils.hasText(keyword))
            return getAllSimpleReviewInfos(type, pageable);

        if (type.equals(STORE.toString())) {
            return storeReviewRepository.findAllByContentContainingIgnoreCase(keyword, pageable)
                    .map(storeReview -> GetReviewListResponseDto.toDetailStoreDto(storeReview, type, storeReview.getStore().getId()));
        }
        else if (type.equals(PRODUCT.toString())) {
            return productReviewRepository.findAllByContentContainingIgnoreCase(keyword, pageable)
                    .map(productReview -> GetReviewListResponseDto.toDetailProductDto(productReview, type, productReview.getProduct().getId()));
        }
        throw new TypeDoesntExistException();
    }

    private Page<GetReviewListResponseDto> getAllSimpleReviewInfos(String type, Pageable pageable) {
        if (type.equals(STORE.toString())) {
            return storeReviewRepository.findAll(pageable)
                    .map(storeReview -> GetReviewListResponseDto.toDetailStoreDto(storeReview, type, storeReview.getStore().getId()));
        }
        else if (type.equals(PRODUCT.toString())) {
            return productReviewRepository.findAll(pageable)
                    .map(productReview -> GetReviewListResponseDto.toDetailProductDto(productReview, type, productReview.getProduct().getId()));
        }
        throw new TypeDoesntExistException();
    }

    @Transactional(readOnly = true)
    public Page<GetReviewListResponseDto> getSimpleReviewInfos(String type, Long id, Pageable pageable) {
        if (type.equals(STORE.toString())) {
            Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
            Page<StoreReview> pages = storeReviewRepository.findStoreReviewsByStore(pageable, store);
            return pages.map(GetReviewListResponseDto::from);
        }
        else if (type.equals(PRODUCT.toString())) {
            Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
            Page<ProductReview> pages = productReviewRepository.findProductReviewsByProduct(pageable, product);
            return pages.map(GetReviewListResponseDto::from);
        }
        else {
            throw new TypeDoesntExistException();
        }
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfo(Long id) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        List<String> urls = getStoreReviewImgUrls(storeReview);
        return buildReviewInfoResponseDto(
                storeReview.getReviewer().getEmail(),
                storeReview.getCreatedAt(),
                storeReview.getStar(),
                storeReview.getContent(),
                urls,
                false
        );
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfo(Long id) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        List<String> urls = getProductReviewImgUrls(productReview);
        return buildReviewInfoResponseDto(
                productReview.getReviewer().getEmail(),
                productReview.getCreatedAt(),
                productReview.getStar(),
                productReview.getContent(),
                urls,
                false
        );
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getStoreReviewInfoWithAuth(Long id, Member member) {
        StoreReview storeReview = storeReviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        boolean isWriter = storeReview.getReviewer().getEmail().equals(member.getEmail());
        List<String> urls = getStoreReviewImgUrls(storeReview);
        return buildReviewInfoResponseDto(
                storeReview.getReviewer().getEmail(),
                storeReview.getCreatedAt(),
                storeReview.getStar(),
                storeReview.getContent(),
                urls,
                isWriter
        );
    }

    @Transactional(readOnly = true)
    public GetReviewInfoResponseDto getProductReviewInfoWithAuth(Long id, Member member) {
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        boolean isWriter = productReview.getReviewer().getEmail().equals(member.getEmail());
        List<String> urls = getProductReviewImgUrls(productReview);
        return buildReviewInfoResponseDto(
                productReview.getReviewer().getEmail(),
                productReview.getCreatedAt(),
                productReview.getStar(),
                productReview.getContent(),
                urls,
                isWriter
        );
    }

    @Transactional
    public void deleteStoreReview(Long reviewId, Member currentMember) {
        StoreReview storeReview = storeReviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        if (!storeReview.getReviewer().getEmail().equals(currentMember.getEmail())) {
            throw new MemberNotEqualsException();
        }

        List<StoreReviewFile> reviewImages = storeReviewFileRepository.findByStoreReviewId(reviewId);
        if (reviewImages != null) {
            for (StoreReviewFile img : reviewImages) {
                s3Service.deleteFile(img.getFileUrl());
            }
            storeReviewFileRepository.deleteAll(reviewImages);
        }
        storeReviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void deleteProductReview(Long reviewId, Member currentMember) {
        ProductReview productReview = productReviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        if (!productReview.getReviewer().getId().equals(currentMember.getId())) {
            throw new MemberNotEqualsException();
        }

        List<ProductReviewFile> reviewImages = productReviewFileRepository.findByProductReviewId(reviewId);
        if (reviewImages != null) {
            for (ProductReviewFile img : reviewImages) {
                s3Service.deleteFile(img.getFileUrl());
            }
            productReviewFileRepository.deleteAll(reviewImages);
        }
        productReviewRepository.deleteById(reviewId);
    }

    public GetReviewInfoResponseDto buildReviewInfoResponseDto(
            String email,
            String createdAt,
            Integer star,
            String content,
            List<String> urls,
            boolean isWriter
    ) {
        return GetReviewInfoResponseDto.builder()
                .writerEmail(email)
                .createdAt(createdAt)
                .star(star)
                .content(content)
                .fileUrls(urls)
                .isWriter(isWriter)
                .build();
    }

    @Transactional
    public void uploadFiles(List<MultipartFile> multipartFiles, StoreReview storeReview) {
        for (MultipartFile file : multipartFiles) {
            StoreReviewFile storeReviewFile = StoreReviewFile.getEntity(storeReview, s3Service.uploadFile(file));
            storeReview.getStoreReviewFiles().add(storeReviewFile);
        }
    }

    @Transactional
    public void uploadFiles(List<MultipartFile> multipartFiles, ProductReview productReview) {
        for (MultipartFile file : multipartFiles) {
            ProductReviewFile productReviewFile = ProductReviewFile.getEntity(productReview, s3Service.uploadFile(file));
            productReview.getProductReviewFiles().add(productReviewFile);
        }
    }

    @NotNull
    private List<String> getProductReviewImgUrls(ProductReview productReview) {
        List<String> urls = new ArrayList<>();
        List<ProductReviewFile> productReviewFiles = productReview.getProductReviewFiles();
        if (productReviewFiles != null) {
            for (ProductReviewFile image : productReview.getProductReviewFiles()) {
                urls.add(image.getFileUrl());
            }
        }
        return urls;
    }

    @NotNull
    private List<String> getStoreReviewImgUrls(StoreReview storeReview) {
        List<String> urls = new ArrayList<>();
        List<StoreReviewFile> storeReviewFiles = storeReview.getStoreReviewFiles();
        if (storeReviewFiles != null) {
            for (StoreReviewFile image : storeReview.getStoreReviewFiles()) {
                urls.add(image.getFileUrl());
            }
        }
        return urls;
    }
}
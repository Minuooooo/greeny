package greeny.backend.domain.review.presentation.controller;

import greeny.backend.domain.member.application.MemberService;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.review.presentation.dto.WriteReviewRequestDto;
import greeny.backend.domain.review.application.ReviewService;
import greeny.backend.exception.situation.common.TypeDoesntExistException;
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
import java.util.List;
import static greeny.backend.domain.Eco.*;
import static greeny.backend.response.Response.success;
import static greeny.backend.response.SuccessMessage.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/reviews")
@Tag(name = "Review", description = "Review API Document")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Write review API", description = "Put review type & content and object type you want to write")
    public Response writeReview(
            @RequestParam String type,
            @RequestParam Long id,
            @Valid @RequestPart(name="body") WriteReviewRequestDto writeReviewRequestDto,
            @RequestPart(name = "file", required = false) List<MultipartFile> multipartFiles
    ) {
        if (valueOf(type) == STORE) {
            reviewService.writeStoreReview(id,writeReviewRequestDto,multipartFiles,memberService.getCurrentMember());
            return success(SUCCESS_TO_WRITE_STORE_REVIEW);
        }
        else if (valueOf(type) == PRODUCT) {
            reviewService.writeProductReview(id,writeReviewRequestDto,multipartFiles,memberService.getCurrentMember());
            return success(SUCCESS_TO_WRITE_PRODUCT_REVIEW);
        }
        else {
            throw new TypeDoesntExistException();
        }
    }

    @GetMapping("/all")
    @ResponseStatus(OK)
    @Operation(summary = "Get all simple review infos API", description = "Put review type and pageable object you want to get")
    public Response getAllSimpleReviewInfos(
            @RequestParam(required = false) String keyword,
            @RequestParam String type,
            @ParameterObject Pageable pageable
    ) {
        return success(SUCCESS_TO_GET_ALL_REVIEW_LIST,reviewService.searchSimpleReviewInfos(keyword,type,pageable));
    }

    @GetMapping("/simple")
    @ResponseStatus(OK)
    @Operation(summary = "Get simple review infos API", description = "Put review type and pageable object you want to get")
    public Response getSimpleReviewInfos(
            @RequestParam String type,
            @RequestParam Long id,
            @ParameterObject Pageable pageable
    ) {
        return success(SUCCESS_TO_GET_REVIEW_LIST,reviewService.getSimpleReviewInfos(type,id,pageable));
    }

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Get review info API", description = "Put review type and reviewId you want to get")
    public Response getReviewInfo(@RequestParam String type, @RequestParam Long id) {
        if (valueOf(type) == STORE) {
            return success(SUCCESS_TO_GET_STORE_REVIEW,reviewService.getStoreReviewInfo(id));
        }
        else if (valueOf(type) == PRODUCT) {
            return success(SUCCESS_TO_GET_PRODUCT_REVIEW,reviewService.getProductReviewInfo(id));
        }
        else {
            throw new TypeDoesntExistException();
        }
    }

    @GetMapping("/auth")
    @ResponseStatus(OK)
    @Operation(summary = "Get review info with Auth API", description = "Put review type and reviewId you want to get")
    public Response getReviewInfoWithAuth(@RequestParam String type, @RequestParam Long id) {
        if (valueOf(type) == STORE) {
            return success(SUCCESS_TO_GET_STORE_REVIEW,reviewService.getStoreReviewInfoWithAuth(id,memberService.getCurrentMember()));
        }
        else if (valueOf(type) == PRODUCT) {
            return success(SUCCESS_TO_GET_PRODUCT_REVIEW,reviewService.getProductReviewInfoWithAuth(id,memberService.getCurrentMember()));
        }
        else {
            throw new TypeDoesntExistException();
        }
    }

    @DeleteMapping
    @ResponseStatus(OK)
    @Operation(summary = "Delete review API", description = "Put review type and object id you want to delete")
    public Response deleteReview(@RequestParam String type, @RequestParam Long id) {
        Member currentMember = memberService.getCurrentMember();
        if (valueOf(type) == STORE) {
            reviewService.deleteStoreReview(id,currentMember);
            return success(SUCCESS_TO_DELETE_STORE_REVIEW);
        }
        else if (valueOf(type) == PRODUCT) {
            reviewService.deleteProductReview(id,currentMember);
            return success(SUCCESS_TO_DELETE_PRODUCT_REVIEW);
        }
        else {
            throw new TypeDoesntExistException();
        }
    }
}
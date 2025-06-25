package com.shongon.restaurant_backend.controller;

import com.shongon.restaurant_backend.domain.ReviewCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.dto.ReviewCreateUpdateRequestDTO;
import com.shongon.restaurant_backend.domain.dto.ReviewDTO;
import com.shongon.restaurant_backend.domain.entities.Review;
import com.shongon.restaurant_backend.domain.entities.User;
import com.shongon.restaurant_backend.mapper.ReviewMapper;
import com.shongon.restaurant_backend.service.blueprint.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {

    ReviewMapper reviewMapper;
    ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDTO review,
            @AuthenticationPrincipal Jwt jwt
    ) {

        ReviewCreateUpdateRequest reviewRequest = reviewMapper.toReviewCreateUpdateRequest(review);

        User author = jwtToUser(jwt);

        Review createdReview = reviewService.createReview(author, restaurantId, reviewRequest);

        return ResponseEntity.ok(reviewMapper.toDTO(createdReview));
    }

    @GetMapping
    public Page<ReviewDTO> getReviews(
            @PathVariable String restaurantId,
            @PageableDefault(size = 20,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return reviewService.listReviews(restaurantId, pageable)
                .map(reviewMapper::toDTO);

    }

    @GetMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(
        @PathVariable String restaurantId,
        @PathVariable String reviewId
    ){
        return reviewService.getReview(restaurantId, reviewId)
                .map(reviewMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDTO review,
            @AuthenticationPrincipal Jwt jwt
    ){
        ReviewCreateUpdateRequest reviewRequest = reviewMapper.toReviewCreateUpdateRequest(review);

        User author = jwtToUser(jwt);

        Review updatedReview = reviewService.updateReview(author, restaurantId, reviewId, reviewRequest);

        return ResponseEntity.ok(reviewMapper.toDTO(updatedReview));
    }

    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ){
        reviewService.deleteReview(restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }

    private User jwtToUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}

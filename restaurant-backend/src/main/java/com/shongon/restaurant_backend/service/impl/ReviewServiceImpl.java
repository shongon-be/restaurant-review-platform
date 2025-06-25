package com.shongon.restaurant_backend.service.impl;

import com.shongon.restaurant_backend.domain.ReviewCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.entities.Photo;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import com.shongon.restaurant_backend.domain.entities.Review;
import com.shongon.restaurant_backend.domain.entities.User;
import com.shongon.restaurant_backend.exception.RestaurantNotFoundException;
import com.shongon.restaurant_backend.exception.ReviewNotAllowedException;
import com.shongon.restaurant_backend.repository.RestaurantRepository;
import com.shongon.restaurant_backend.service.blueprint.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final int EDIT_TIME_LIMIT_HOURS = 48;

    RestaurantRepository restaurantRepository;

    @Override
    public Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        validateNewReview(author, restaurant);

        Review newReview = buildReview(review, author);
        restaurant.getReviews().add(newReview);

        updateAndSaveRestaurant(restaurant);

        return findReviewById(restaurant, newReview.getId())
                .orElseThrow(() -> new RuntimeException("Error retrieving created review"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> listReviews(String restaurantId, Pageable pageable) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        List<Review> reviews = getSortedReviews(restaurant.getReviews(), pageable);

        return createPage(reviews, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReview(String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        return findReviewById(restaurant, reviewId);
    }

    @Override
    public Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest review) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        Review existingReview = getExistingReview(restaurant, reviewId);

        validateUpdatePermission(author, existingReview);

        updateReviewContent(existingReview, review);
        updateAndSaveRestaurant(restaurant);

        return existingReview;
    }

    @Override
    public void deleteReview(String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        restaurant.getReviews().removeIf(r -> reviewId.equals(r.getId()));
        updateAndSaveRestaurant(restaurant);
    }

    // Helper methods
    private void validateNewReview(User author, Restaurant restaurant) {
        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if (hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this restaurant!");
        }
    }

    private Review buildReview(ReviewCreateUpdateRequest request, User author) {
        LocalDateTime now = LocalDateTime.now();

        List<Photo> photos = request.getPhotoIds().stream()
                .map(url -> Photo.builder()
                        .url(url)
                        .uploadDate(now)
                        .build())
                .toList();

        return Review.builder()
                .id(UUID.randomUUID().toString())
                .content(request.getContent())
                .rating(request.getRating())
                .photos(photos)
                .datePosted(now)
                .lastEdited(now)
                .writtenBy(author)
                .build();
    }

    private List<Review> getSortedReviews(List<Review> reviews, Pageable pageable) {
        Comparator<Review> comparator = getComparator(pageable.getSort());
        return reviews.stream()
                .sorted(comparator)
                .toList();
    }

    private Comparator<Review> getComparator(Sort sort) {
        if (!sort.isSorted()) {
            return Comparator.comparing(Review::getDatePosted).reversed();
        }

        Sort.Order order = sort.iterator().next();
        Comparator<Review> comparator = switch (order.getProperty()) {
            case "datePosted" -> Comparator.comparing(Review::getDatePosted);
            case "rating" -> Comparator.comparing(Review::getRating);
            default -> Comparator.comparing(Review::getDatePosted);
        };

        return order.getDirection().isAscending() ? comparator : comparator.reversed();
    }

    private Page<Review> createPage(List<Review> reviews, Pageable pageable) {
        int start = (int) pageable.getOffset();

        if (start >= reviews.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        int end = Math.min(start + pageable.getPageSize(), reviews.size());
        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());
    }

    private Review getExistingReview(Restaurant restaurant, String reviewId) {
        return findReviewById(restaurant, reviewId)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));
    }

    private void validateUpdatePermission(User author, Review existingReview) {
        if (!author.getId().equals(existingReview.getWrittenBy().getId())) {
            throw new ReviewNotAllowedException("Cannot update review written by another user!");
        }

        if (LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(EDIT_TIME_LIMIT_HOURS))) {
            throw new ReviewNotAllowedException("Review can no longer be edited after 48 hours!");
        }
    }

    private void updateReviewContent(Review existingReview, ReviewCreateUpdateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        existingReview.setContent(request.getContent());
        existingReview.setRating(request.getRating());
        existingReview.setLastEdited(now);

        List<Photo> photos = request.getPhotoIds().stream()
                .map(photoId -> Photo.builder()
                        .url(photoId)
                        .uploadDate(now)
                        .build())
                .toList();

        existingReview.setPhotos(photos);
    }

    private void updateAndSaveRestaurant(Restaurant restaurant) {
        updateRestaurantAverageRating(restaurant);
        restaurantRepository.save(restaurant);
    }

    private Restaurant getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        "Restaurant with ID not found: " + restaurantId));
    }

    private void updateRestaurantAverageRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();

        if (reviews.isEmpty()) {
            restaurant.setAverageRating(0.0f);
            return;
        }

        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        restaurant.setAverageRating((float) averageRating);
    }

    private Optional<Review> findReviewById(Restaurant restaurant, String reviewId) {
        return restaurant.getReviews().stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst();
    }
}
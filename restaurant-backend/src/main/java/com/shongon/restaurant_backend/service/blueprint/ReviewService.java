package com.shongon.restaurant_backend.service.blueprint;

import com.shongon.restaurant_backend.domain.ReviewCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.entities.Review;
import com.shongon.restaurant_backend.domain.entities.User;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);

    Page<Review> listReviews(String restaurantId, Pageable pageable);
}

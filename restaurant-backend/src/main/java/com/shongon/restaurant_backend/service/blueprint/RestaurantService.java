package com.shongon.restaurant_backend.service.blueprint;

import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);

    Page<Restaurant> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    Optional<Restaurant> getRestaurants(String id);

    Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest request);

    void deleteRestaurant(String id);
}

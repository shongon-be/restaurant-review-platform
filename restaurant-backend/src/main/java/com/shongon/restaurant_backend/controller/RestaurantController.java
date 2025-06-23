package com.shongon.restaurant_backend.controller;

import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.dto.RestaurantCreateUpdateResquestDTO;
import com.shongon.restaurant_backend.domain.dto.RestaurantDTO;
import com.shongon.restaurant_backend.domain.dto.RestaurantSummaryDTO;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import com.shongon.restaurant_backend.mapper.RestaurantMapper;
import com.shongon.restaurant_backend.service.blueprint.RestaurantService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantController {

    RestaurantService restaurantService;
    RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(
            @Valid @RequestBody RestaurantCreateUpdateResquestDTO request
    ) {
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper
                .toRestaurantCreateUpdateRequest(request);

        Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);

        RestaurantDTO createdRestaurantDTO = restaurantMapper.toRestaurantDTO(restaurant);

        return ResponseEntity.ok(createdRestaurantDTO);
    }

    @GetMapping
    public Page<RestaurantSummaryDTO> searchRestaurants(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Page<Restaurant> searchResults = restaurantService.searchRestaurants(
                q, minRating, latitude, longitude, radius, PageRequest.of(page-1,size)
        );

        return searchResults.map(restaurantMapper::toSummaryDTO);
    }
}

package com.shongon.restaurant_backend.service.impl;

import com.shongon.restaurant_backend.domain.GeoLocation;
import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.entities.Address;
import com.shongon.restaurant_backend.domain.entities.Photo;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import com.shongon.restaurant_backend.exception.RestaurantNotFoundException;
import com.shongon.restaurant_backend.repository.RestaurantRepository;
import com.shongon.restaurant_backend.service.blueprint.RestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantServiceImpl implements RestaurantService {

    RestaurantRepository restaurantRepository;
    RandomLondonGeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {
        Restaurant restaurant = buildRestaurant(request);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> searchRestaurants(
            String query, Float minRating,
            Float latitude, Float longitude, Float radius,
            Pageable pageable
    ) {

        Float searchMinRating = Optional.ofNullable(minRating).orElse(0f);

        // Search by rating only
        if (minRating != null && !StringUtils.hasText(query)) {
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        // Text search with rating filter
        if (StringUtils.hasText(query)) {
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        // Location-based search
        if (isValidLocationSearch(latitude, longitude, radius)) {
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Optional<Restaurant> getRestaurants(String id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest request) {
        Restaurant restaurant = getRestaurants(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID does not exist: " + id));

        updateRestaurantFields(restaurant, request);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }

    private Restaurant buildRestaurant(RestaurantCreateUpdateRequest request) {
        GeoPoint geoPoint = createGeoPoint(request.getAddress());
        List<Photo> photos = convertPhotoIdsToPhotos(request.getPhotoIds());

        return Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .contactInformation(request.getContactInformation())
                .address(request.getAddress())
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();
    }

    private GeoPoint createGeoPoint(Address address) {
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        return new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());
    }

    private List<Photo> convertPhotoIdsToPhotos(List<String> photoIds) {
        if (photoIds == null || photoIds.isEmpty()) {
            return List.of();
        }

        return photoIds.stream()
                .map(this::createPhoto)
                .toList();
    }

    private Photo createPhoto(String photoUrl) {
        return Photo.builder()
                .url(photoUrl)
                .uploadDate(LocalDateTime.now())
                .build();
    }

    private boolean isValidLocationSearch(Float latitude, Float longitude, Float radius) {
        return latitude != null && longitude != null && radius != null;
    }

    private void updateRestaurantFields(Restaurant restaurant, RestaurantCreateUpdateRequest request) {
        GeoPoint geoPoint = createGeoPoint(request.getAddress());
        List<Photo> photos = convertPhotoIdsToPhotos(request.getPhotoIds());

        restaurant.setName(request.getName());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setContactInformation(request.getContactInformation());
        restaurant.setAddress(request.getAddress());
        restaurant.setGeoLocation(geoPoint);
        restaurant.setOperatingHours(request.getOperatingHours());
        restaurant.setPhotos(photos);
    }

}

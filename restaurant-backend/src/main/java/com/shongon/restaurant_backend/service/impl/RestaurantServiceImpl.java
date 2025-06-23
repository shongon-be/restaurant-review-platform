package com.shongon.restaurant_backend.service.impl;

import com.shongon.restaurant_backend.domain.GeoLocation;
import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.entities.Address;
import com.shongon.restaurant_backend.domain.entities.Photo;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import com.shongon.restaurant_backend.repository.RestaurantRepository;
import com.shongon.restaurant_backend.service.blueprint.RestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantServiceImpl implements RestaurantService {

    RestaurantRepository restaurantRepository;
    RandomLondonGeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {
        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream()
                .map(photoUrl -> Photo.builder()
                        .url(photoUrl)
                        .uploadDate(LocalDateTime.now())
                        .build()
                )
                .toList();

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .contactInformation(request.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> searchRestaurants(
            String query, Float minRating,
            Float latitude, Float longitude, Float radius, Pageable pageable
    ) {

        // Search by minRating
        if (null != minRating && (null == query || query.isEmpty())){
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = null == minRating ? 0f : minRating;

        // Fuzzy search (not including minRating)
        if (null != query && !query.trim().isEmpty()){
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        // Search nearby location
        if (null != latitude && null != longitude && null != radius){
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);
    }
}

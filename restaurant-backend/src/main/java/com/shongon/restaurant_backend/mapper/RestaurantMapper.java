package com.shongon.restaurant_backend.mapper;

import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.dto.*;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import com.shongon.restaurant_backend.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateResquestDTO dto);

    RestaurantDTO toRestaurantDTO(Restaurant restaurant);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantSummaryDTO toSummaryDTO(Restaurant restaurant);

    @Named("populateTotalReviews")
    default Integer populateTotalReviews(List<Review> reviews) {
        return reviews.size();
    }

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDTO toGeoPointDTO(GeoPoint geoPoint);

}

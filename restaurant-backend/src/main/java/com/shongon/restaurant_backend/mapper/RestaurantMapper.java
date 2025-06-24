package com.shongon.restaurant_backend.mapper;

import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.dto.*;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateResquestDTO dto);

    RestaurantDTO toRestaurantDTO(Restaurant restaurant);

    @Mapping(target = "totalReviews", expression = "java(restaurant.getReviews() != null ? restaurant.getReviews().size() : 0)")
    RestaurantSummaryDTO toSummaryDTO(Restaurant restaurant);

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDTO toGeoPointDTO(GeoPoint geoPoint);

}

package com.shongon.restaurant_backend.mapper;

import com.shongon.restaurant_backend.domain.RestaurantCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.dto.GeoPointDTO;
import com.shongon.restaurant_backend.domain.dto.RestaurantCreateUpdateResquestDTO;
import com.shongon.restaurant_backend.domain.dto.RestaurantDTO;
import com.shongon.restaurant_backend.domain.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateResquestDTO dto);

    RestaurantDTO toRestaurantDTO(Restaurant restaurant);

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDTO toGeoPointDTO(GeoPoint geoPoint);
}

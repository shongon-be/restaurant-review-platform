package com.shongon.restaurant_backend.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RestaurantDTO {

    String id;

    String name;

    String cuisineType;

    String contactInformation;

    Float averageRating;

    GeoPointDTO geoLocation;

    AddressDTO address;

    OperatingHoursDTO operatingHours;

    List<PhotoDTO> photos = new ArrayList<>();

    List<ReviewDTO> reviews = new ArrayList<>();

    UserDTO createdBy;
}

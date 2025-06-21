package com.shongon.restaurant_backend.domain;

import com.shongon.restaurant_backend.domain.entities.Address;
import com.shongon.restaurant_backend.domain.entities.OperatingHours;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCreateUpdateRequest {
    String name;
    String cuisineType;
    String contactInformation;
    Address address;
    OperatingHours operatingHours;
    List<String> photoIds;
}

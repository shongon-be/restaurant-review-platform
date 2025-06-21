package com.shongon.restaurant_backend.domain.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCreateUpdateResquestDTO {
    @NotBlank(message = "Restaurant name is required")
    String name;

    @NotBlank(message = "Cuisine type is required")
    String cuisineType;

    @NotBlank(message = "Contact infomation is required")
    String contactInformation;

    @Valid
    AddressDTO address;

    @Valid
    OperatingHoursDTO operatingHours;

    @Size(min = 1, message = "At least one photo ID is required")
    List<String> photoIds;
}

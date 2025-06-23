package com.shongon.restaurant_backend.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantSummaryDTO {
    String id;
    String name;
    String cuisineType;
    Float averageRating;
    Integer totalReviews;
    AddressDTO address;
    List<PhotoDTO> photos;
}

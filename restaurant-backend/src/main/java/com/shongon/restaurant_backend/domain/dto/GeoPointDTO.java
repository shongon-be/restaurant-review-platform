package com.shongon.restaurant_backend.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class GeoPointDTO {
    Double latitude;
    Double longitude;
}

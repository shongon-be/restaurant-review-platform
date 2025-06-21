package com.shongon.restaurant_backend.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoLocation {
    Double latitude;
    Double longitude;
}

package com.shongon.restaurant_backend.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewCreateUpdateRequest {
    String content;
    Integer rating;
    List<String> photoIds;
}

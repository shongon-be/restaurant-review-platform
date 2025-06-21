package com.shongon.restaurant_backend.domain.dto;


import com.shongon.restaurant_backend.domain.entities.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDTO {

    String id;
    String content;
    Integer rating;
    LocalDateTime datePosted;
    LocalDateTime lastEdited;
    List<PhotoDTO> photos = new ArrayList<>();
    UserDTO writtenBy;
}

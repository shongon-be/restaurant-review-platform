package com.shongon.restaurant_backend.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeRange {

    @Field(type = FieldType.Keyword)
    String openTime;

    @Field(type = FieldType.Keyword)
    String closeTime;
}

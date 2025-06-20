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
public class OperatingHours {

    @Field(type = FieldType.Nested)
    TimeRange monday;

    @Field(type = FieldType.Nested)
    TimeRange tuesday;

    @Field(type = FieldType.Nested)
    TimeRange wednesday;

    @Field(type = FieldType.Nested)
    TimeRange thursday;

    @Field(type = FieldType.Nested)
    TimeRange friday;

    @Field(type = FieldType.Nested)
    TimeRange saturday;

    @Field(type = FieldType.Nested)
    TimeRange sunday;
}

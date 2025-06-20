package com.shongon.restaurant_backend.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {

    @Field(type = FieldType.Keyword)
    String id;

    @Field(type = FieldType.Text)
    String content;

    @Field(type = FieldType.Integer)
    Integer rating;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    LocalDateTime datePosted;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    LocalDateTime lastEdited;

    @Field(type = FieldType.Nested)
    List<Photo> photos = new ArrayList<>();

    @Field(type = FieldType.Nested)
    User writtenBy;
}

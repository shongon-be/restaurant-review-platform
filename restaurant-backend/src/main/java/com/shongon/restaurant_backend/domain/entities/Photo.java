package com.shongon.restaurant_backend.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Photo {

    @Field(type = FieldType.Keyword)
    String url;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    LocalDateTime uploadDate;

}

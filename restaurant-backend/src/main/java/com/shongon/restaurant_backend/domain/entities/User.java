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
public class User {

    @Field(type = FieldType.Keyword)
    String id;

    @Field(type = FieldType.Text)
    String username;

    @Field(type = FieldType.Text)
    String givenName;

    @Field(type = FieldType.Text)
    String familyName;

}

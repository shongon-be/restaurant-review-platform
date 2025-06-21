package com.shongon.restaurant_backend.domain.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant {

    @Id
    String id;

    @Field(type = FieldType.Text)
    String name;

    @Field(type = FieldType.Text)
    String cuisineType;

    @Field(type = FieldType.Text)
    String contactInformation;

    @Field(type = FieldType.Float)
    Float avgRating;

    @GeoPointField
    GeoPoint geoLocation;

    @Field(type = FieldType.Nested)
    Address address;

    @Field(type = FieldType.Nested)
    OperatingHours operatingHours;

    @Field(type = FieldType.Nested)
    List<Photo> photos = new ArrayList<>();

    @Field(type = FieldType.Nested)
    List<Review> reviews = new ArrayList<>();

    @Field(type = FieldType.Nested)
    User createdBy;
}

package com.shongon.restaurant_backend.domain.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperatingHoursDTO {
    TimeRangeDTO monday;
    TimeRangeDTO tuesday;
    TimeRangeDTO wednesday;
    TimeRangeDTO thursday;
    TimeRangeDTO friday;
    TimeRangeDTO saturday;
    TimeRangeDTO sunday;
}

package com.shongon.restaurant_backend.mapper;

import com.shongon.restaurant_backend.domain.ReviewCreateUpdateRequest;
import com.shongon.restaurant_backend.domain.dto.ReviewCreateUpdateRequestDTO;
import com.shongon.restaurant_backend.domain.dto.ReviewDTO;
import com.shongon.restaurant_backend.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDTO dto);

    ReviewDTO toDTO(Review review);
}

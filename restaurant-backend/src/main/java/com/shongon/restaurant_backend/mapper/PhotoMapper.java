package com.shongon.restaurant_backend.mapper;

import com.shongon.restaurant_backend.domain.dto.PhotoDTO;
import com.shongon.restaurant_backend.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDTO toDTO(Photo photo);

}

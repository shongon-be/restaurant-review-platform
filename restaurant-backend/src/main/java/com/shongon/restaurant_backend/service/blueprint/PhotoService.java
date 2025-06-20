package com.shongon.restaurant_backend.service.blueprint;

import com.shongon.restaurant_backend.domain.entities.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoService {
    Photo uploadPhoto(MultipartFile file);
    Optional<Resource> loadPhotoAsResource(String id);
}

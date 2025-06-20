package com.shongon.restaurant_backend.service.blueprint;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface StorageService {
    String store(MultipartFile file, String filename);
    Optional<Resource> loadAsResource(String filename);
}

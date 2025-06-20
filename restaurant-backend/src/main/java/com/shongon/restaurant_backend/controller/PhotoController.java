package com.shongon.restaurant_backend.controller;

import com.shongon.restaurant_backend.domain.dto.PhotoDTO;
import com.shongon.restaurant_backend.domain.entities.Photo;
import com.shongon.restaurant_backend.mapper.PhotoMapper;
import com.shongon.restaurant_backend.service.blueprint.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @PostMapping()
    public PhotoDTO uploadPhoto(@RequestParam("file") MultipartFile file) {
        Photo savedPhoto = photoService.uploadPhoto(file);
        return photoMapper.toDTO(savedPhoto);
    }
}

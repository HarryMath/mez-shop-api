package com.mez.api.controllers;

import com.mez.api.tools.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageController {

    private final ImageRepository imageRepository;

    @Autowired
    ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @PutMapping("/images/save")
    public String saveImage(@RequestParam(name = "photo") MultipartFile photo) {
        return "{\"url\": \"" + imageRepository.saveImage(photo) + "\"}";
    }

    @PutMapping("/images/saveAll")
    public List<String> saveImages(@RequestParam(name = "photo") List<MultipartFile> photos) {
        return photos.size() > 0 ?
                imageRepository.saveImages(photos) :
                new ArrayList<>();
    }
}

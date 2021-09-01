package com.mez.api.controllers;

import com.mez.api.service.AuthorisationService;
import com.mez.api.tools.ImageRepository;
import com.mez.api.tools.ResponseCodes;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {

  private final ImageRepository imageRepository;
  private final AuthorisationService authService;

  @Autowired
  ImageController(ImageRepository imageRepository, AuthorisationService authService) {
    this.imageRepository = imageRepository;
      this.authService = authService;
  }

  @PutMapping("/images/save")
  public String saveImage(
      @RequestParam(name = "photo") MultipartFile photo,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
      return "{\"url\": \"" + imageRepository.saveImage(photo) + "\"}";
    }
    return String.valueOf(ResponseCodes.UNAUTHORISED);
  }

  @PutMapping("/images/saveAll")
  public List<String> saveImages(
      @RequestParam(name = "photo") List<MultipartFile> photos,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
        return photos.size() > 0 ?
            imageRepository.saveImages(photos) :
            new ArrayList<>();
    }
    return new ArrayList<>();
  }
}

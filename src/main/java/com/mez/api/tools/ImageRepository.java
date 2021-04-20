package com.mez.api.tools;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ImageRepository {

    @Value("${cloudinary.resUrl}")
    private String url;

    public String saveImage(MultipartFile file) {
        if(file == null) return null;
        Cloudinary cloudinary = new Cloudinary(url);
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> saveImages(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        files.forEach(file -> {
            imageUrls.add(saveImage(file));
        });
        return imageUrls;
    }
}

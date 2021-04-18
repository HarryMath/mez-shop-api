package com.mez.api.models.DTO;

import com.mez.api.models.CharacteristicsRow;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EngineUpload {
    private int id;
    private String name;
    private String type;
    private String manufacturer;
    private float price;
    private float mass;
    private MultipartFile photo;

    private List<CharacteristicsRow> characteristics;
    private List<MultipartFile> photos;
}

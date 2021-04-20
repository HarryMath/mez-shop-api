package com.mez.api.models.DTO;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.mez.api.models.CharacteristicsRow;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultipartFile.class, name = "photo")
})
public class EngineUpload {
    private int id;
    private String name;
    private String type;
    private String manufacturer;
    private float price;
    private float mass;
    private String photo;

    private List<CharacteristicsRow> characteristics;
    private List<String> photos;
}

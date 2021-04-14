package com.mez.api.models.DTO;

import com.mez.api.models.CharacteristicsRow;
import lombok.Data;

import java.util.List;

@Data
public class EnginePreview {
    private int id;
    private String name;
    private String manufacturer;
    private String type;
    private float price;
    private String photo;
    private List<CharacteristicsRow> characteristics;
}

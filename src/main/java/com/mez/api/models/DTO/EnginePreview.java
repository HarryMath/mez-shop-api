package com.mez.api.models.DTO;

import com.mez.api.models.CharacteristicsRow;
import lombok.Data;

import java.util.List;

@Data
public class EnginePreview {
    private String name;
    private String manufacturer;
    private String type;
    private float minPrice;
    private float mass;
    private float axisHeight;
    private String photo;
    private List<CharacteristicsRow> characteristics;
}

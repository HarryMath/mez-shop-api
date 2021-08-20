package com.mez.api.models.DTO;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.EngineType;
import lombok.Data;

import java.util.List;

@Data
public class EngineDetails {
    private String name;
    private String manufacturer;
    private float priceLapy;
    private float priceCombi;
    private float priceFlanets;
    private float mass;
    private String photo;

    private EngineType type;
    private List<CharacteristicsRow> characteristics;
    private List<String> photos;
}

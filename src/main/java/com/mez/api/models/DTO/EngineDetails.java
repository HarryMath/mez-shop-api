package com.mez.api.models.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class EngineDetails {
    private int id;
    private String name;
    private String manufacturer;
    private String type;
    private float price;
    private String photo;
    private Map<String, String> characteristics;
}

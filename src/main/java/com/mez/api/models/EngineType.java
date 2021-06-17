package com.mez.api.models;

import lombok.Data;

@Data
public class EngineType {
    private String name;
    private int pageOrder;
    private String photo;
    private String shortDescription;
    private String fullDescription;
}

package com.mez.api.models;

import lombok.Data;

@Data
public class Engine {
    private int id;
    private String name;
    private String manufacturer;
    private String type;
    private float price;
    private float mass;
    private String photo;
}

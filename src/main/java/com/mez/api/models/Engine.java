package com.mez.api.models;

import com.mez.api.tools.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Engine {

  @PrimaryKey
  private String name;
  private String manufacturer;
  private String type;
  private float priceLapy;
  private float priceCombi;
  private float priceFlanets;
  private float mass;
  private float axisHeight;
  private String photo;
}

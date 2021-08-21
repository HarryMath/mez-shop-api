package com.mez.api.models.DTO;

import com.mez.api.models.CharacteristicsRow;
import java.util.List;
import lombok.Data;

@Data
public class EngineUpload {

  private String name;
  private String type;
  private String manufacturer;
  private float priceLapy;
  private float priceCombi;
  private float priceFlanets;
  private float mass;
  private float axisHeight;
  private String photo;

  private List<CharacteristicsRow> characteristics;
  private List<String> photos;
}

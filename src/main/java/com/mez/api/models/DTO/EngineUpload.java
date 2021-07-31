package com.mez.api.models.DTO;

import com.mez.api.models.CharacteristicsRow;
import java.util.List;
import lombok.Data;

@Data
public class EngineUpload {

  private String name;
  private String type;
  private String manufacturer;
  private float price;
  private float mass;
  private String photo;

  private List<CharacteristicsRow> characteristics;
  private List<String> photos;
}

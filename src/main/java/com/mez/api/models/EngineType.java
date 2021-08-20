package com.mez.api.models;

import com.mez.api.tools.annotations.PrimaryKey;
import lombok.Data;

@Data
public class EngineType {

  @PrimaryKey
  private String name;
  private int pageOrder;
  private String photo;
  private String shortDescription;
  private String fullDescription;
}

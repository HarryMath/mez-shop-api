package com.mez.api.models;

import com.mez.api.tools.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Manufacturer {

  @PrimaryKey
  private String name;
}

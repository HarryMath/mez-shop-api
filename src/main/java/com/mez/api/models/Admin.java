package com.mez.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mez.api.tools.annotations.Encrypted;
import com.mez.api.tools.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Admin {

  @PrimaryKey
  private String login;
  private String name;
  private String photo;

  @JsonIgnore
  @Encrypted
  private String password;
}

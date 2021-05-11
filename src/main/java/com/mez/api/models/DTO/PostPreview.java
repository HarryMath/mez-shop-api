package com.mez.api.models.DTO;

import lombok.Data;

@Data
public class PostPreview {
  private int id;
  private String title;
  private String date;
  private String beforePhotoText;
  private String photo;
  private int views;
}

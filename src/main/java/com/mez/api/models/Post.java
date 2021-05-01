package com.mez.api.models;

import lombok.Data;

@Data
public class Post {
  private int id;
  private String title;
  private String date;
  private String beforePhotoText;
  private String photo;
  private String afterPhotoText;
  private String tags;
}

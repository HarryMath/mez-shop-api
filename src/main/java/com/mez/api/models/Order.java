package com.mez.api.models;

import com.mez.api.models.DTO.CartItem;
import java.util.List;
import lombok.Data;

@Data
public class Order {

  private List<CartItem> items;
  private String name;
  private String mail;
  private String phone;
}

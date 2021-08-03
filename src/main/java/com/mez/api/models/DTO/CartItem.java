package com.mez.api.models.DTO;

import lombok.Data;

@Data
public class CartItem {
  private String itemId;
  private int amount;
}

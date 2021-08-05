package com.mez.api.controllers;

import com.mez.api.models.Order;
import com.mez.api.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {

  private final OrderService ordersService;

  public OrdersController(OrderService ordersService) {
    this.ordersService = ordersService;
  }

  @RequestMapping("/orders/create")
  public byte feedBack(@RequestBody Order order) {
    return ordersService.sendCheque(order);
  }
}

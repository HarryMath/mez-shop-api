package com.mez.api.controllers;

import com.mez.api.models.Manufacturer;
import com.mez.api.service.ManufacturersService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManufacturersController {

  private final ManufacturersService manufacturersService;

  @Autowired
  ManufacturersController(ManufacturersService service) {
    this.manufacturersService = service;
  }

  @GetMapping("/manufacturers")
  public List<Manufacturer> getManufacturers() {
    return manufacturersService.getAll();
  }

  @PutMapping("/manufacturers/create")
  public byte createEngine(@RequestBody Manufacturer manufacturer) {
    return manufacturersService.save(manufacturer);
  }

  @GetMapping("/manufacturers/{name}/delete")
  public byte deleteCategory(@PathVariable String name) {
    return manufacturersService.delete(name);
  }
}

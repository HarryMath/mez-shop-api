package com.mez.api.service;

import com.mez.api.models.Manufacturer;
import com.mez.api.repository.ManufacturersRepository;
import com.mez.api.tools.ResponseCodes;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturersService {

  ManufacturersRepository manufacturersRepository;
  @Autowired
  ManufacturersService (ManufacturersRepository repository) {
    this.manufacturersRepository = repository;
  }

  public List<Manufacturer> getAll() {
    return manufacturersRepository.getAll();
  }

  public byte delete(String name) {
    int amount = manufacturersRepository.countEngines(name);
    if (amount > 0) {
      return ResponseCodes.NOT_EMPTY;
    }
    return manufacturersRepository.delete(name);
  }

  public byte save(Manufacturer manufacturer) {
    if (manufacturersRepository.countManufacturers(manufacturer.getName()) > 0) {
      return ResponseCodes.ALREADY_EXISTS;
    }
    return manufacturersRepository.save(manufacturer);
  }
}

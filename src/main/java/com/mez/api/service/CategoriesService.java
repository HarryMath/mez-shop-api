package com.mez.api.service;

import com.mez.api.models.EngineType;
import com.mez.api.repository.CategoriesRepository;
import com.mez.api.tools.ImageRepository;
import com.mez.api.tools.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {

  private final CategoriesRepository categoriesRepository;
  private final ImageRepository imageRepository;

  @Autowired
  CategoriesService(CategoriesRepository repository, ImageRepository imageRepository) {
    this.categoriesRepository = repository;
    this.imageRepository = imageRepository;
  }

  public Object getAll(boolean withDetails) {
    return withDetails ?
        categoriesRepository.getAll() :
        categoriesRepository.getPreviews();
  }

  public EngineType getOne(String name) {
    return categoriesRepository.getByName(name);
  }

  public byte delete(String name) {
    int amount = categoriesRepository.countEngines(name);
    if (amount > 0) {
      return ResponseCodes.NOT_EMPTY;
    }
    EngineType category = categoriesRepository.getByName(name);
    if (category.getPhoto() != null) {
      imageRepository.delete(category.getPhoto());
    }
    return categoriesRepository.delete(name);
  }

  public byte save(EngineType category) {
    if (categoriesRepository.countCategories(category.getName()) > 0) {
      return ResponseCodes.ALREADY_EXISTS;
    }
    return categoriesRepository.save(category);
  }

  public byte update(EngineType category) {
    return categoriesRepository.update(category);
  }
}

package com.mez.api.service;

import com.mez.api.models.EngineType;
import com.mez.api.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    CategoriesRepository categoriesRepository;
    @Autowired
    CategoriesService (CategoriesRepository repository) {
        this.categoriesRepository = repository;
    }

    public List<EngineType> getAll() {
        return categoriesRepository.getAll();
    }
}

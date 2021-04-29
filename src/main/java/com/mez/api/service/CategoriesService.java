package com.mez.api.service;

import com.mez.api.models.EngineType;
import com.mez.api.repository.CategoriesRepository;
import com.mez.api.tools.ResponseCodes;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {

    CategoriesRepository categoriesRepository;
    @Autowired
    CategoriesService (CategoriesRepository repository) {
        this.categoriesRepository = repository;
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
        return categoriesRepository.delete(name);
    }

    public byte save(EngineType category) {
        return categoriesRepository.save(category);
    }

    @Deprecated
    public byte update(EngineType category) {
        return categoriesRepository.update(category);
    }
}

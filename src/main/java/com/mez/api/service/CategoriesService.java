package com.mez.api.service;

import com.mez.api.repository.CategoriesRepository;
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
}

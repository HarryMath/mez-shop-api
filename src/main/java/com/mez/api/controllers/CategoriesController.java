package com.mez.api.controllers;

import com.mez.api.models.EngineType;
import com.mez.api.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoriesController {

    private final CategoriesService categoriesService;

    @Autowired
    CategoriesController(CategoriesService service) {
        this.categoriesService = service;
    }

    @GetMapping("/categories")
    public List<EngineType> getCategories() {
        return categoriesService.getAll();
    }
}

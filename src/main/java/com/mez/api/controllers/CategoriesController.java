package com.mez.api.controllers;

import com.mez.api.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CategoriesController {

    private final CategoriesService categoriesService;

    @Autowired
    CategoriesController(CategoriesService service) {
        this.categoriesService = service;
    }

    @GetMapping("/categories")
    public Object getCategories(
            @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails
    ) {
        return categoriesService.getAll(withDetails);
    }
}

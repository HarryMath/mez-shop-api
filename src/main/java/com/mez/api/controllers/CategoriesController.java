package com.mez.api.controllers;

import com.mez.api.models.EngineType;
import com.mez.api.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/categories/{name}")
    public EngineType getOne(@PathVariable String name) {
        return categoriesService.getOne(name);
    }

    @PutMapping("/categories/create")
    public byte createEngine(@RequestBody EngineType category) {
        return categoriesService.save(category);
    }

    @PutMapping("/categories/update")
    public byte updateEngine(@RequestBody EngineType category) {
        return categoriesService.update(category);
    }

    @GetMapping("/categories/{name}/delete")
    public byte deleteCategory(@PathVariable String name) {
        return categoriesService.delete(name);
    }
}

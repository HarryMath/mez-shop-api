package com.mez.api.controllers;

import com.mez.api.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EngineController {

    EngineService engineService;
    @Autowired
    EngineController(EngineService service) {
        this.engineService = service;
    }

    @GetMapping("/engines")
    public Object getEngines(
            @RequestParam(name = "amount", required = false, defaultValue = "99999") int amount,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset
    ) {
        return engineService.getPreview(offset, amount);
    }

}

package com.mez.api.controllers;

import com.mez.api.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/engines/count")
    public Object getEngines() {
        return engineService.getAmount();
    }

    @GetMapping("/engines/{engineId}")
    public Object getOne(
            @PathVariable int engineId,
            @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails) {
        return engineService.getOne(engineId, withDetails);
    }
}

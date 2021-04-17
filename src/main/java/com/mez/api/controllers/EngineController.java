package com.mez.api.controllers;

import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails
    ) {
        return engineService.get(offset, amount, withDetails);
    }

    @GetMapping("/engines/count")
    public Object countEngines(
            @RequestParam(name = "type", required = false, defaultValue = "") String type,
            @RequestParam(name = "manufacturer", required = false, defaultValue = "") String manufacturer,
            @RequestParam(name = "phase", required = false, defaultValue = "0") int phase,
            @RequestParam(name = "voltage", required = false, defaultValue = "0") int voltage,
            @RequestParam(name = "frequencyMin", required = false, defaultValue = "0") int frequencyMin,
            @RequestParam(name = "frequencyMax", required = false, defaultValue = "99999999") int frequencyMax,
            @RequestParam(name = "powerMin", required = false, defaultValue = "0") int powerMin,
            @RequestParam(name = "powerMax", required = false, defaultValue = "99999999") int powerMax
    ) {
        return engineService.getAmount();
    }

    @GetMapping("/engines/{engineId}")
    public Object getOne(
            @PathVariable int engineId,
            @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails) {
        return engineService.getOne(engineId, withDetails);
    }

    @PutMapping("/engines/create")
    public byte createEngine(@RequestBody EngineUpload engine) {
        return engineService.save(engine);
    }
}

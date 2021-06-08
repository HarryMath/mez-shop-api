package com.mez.api.controllers;

import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.service.EngineService;
import java.util.List;
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

    @GetMapping("/engines/find")
    public List<EnginePreview> findEngines(
        @RequestParam(name = "amount", required = false, defaultValue = "99999") int amount,
        @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
        @RequestParam(name = "orderBy", required = false, defaultValue = "engines.id DESC") String orderBy,
        // сначала послдение добавленные
        @RequestParam(name = "types", required = false, defaultValue = "") String types,
        @RequestParam(name = "manufacturers", required = false, defaultValue = "") String manufacturers,
        @RequestParam(name = "phase", required = false, defaultValue = "") String phase,
        @RequestParam(name = "voltage", required = false, defaultValue = "") String voltage,
        @RequestParam(name = "frequency", required = false, defaultValue = "") String frequency,
        @RequestParam(name = "power", required = false, defaultValue = "") String power,
        @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return engineService.find(
            offset, amount, orderBy, query, // order block
            types, manufacturers, phase, // classify block
            voltage, frequency, power // filters block
        );
    }

    @GetMapping("/engines/count")
    public int countEngines(
        @RequestParam(name = "types", required = false, defaultValue = "") String types,
        @RequestParam(name = "manufacturers", required = false, defaultValue = "") String manufacturers,
        @RequestParam(name = "phase", required = false, defaultValue = "") String phase,
        @RequestParam(name = "voltage", required = false, defaultValue = "") String voltage,
        @RequestParam(name = "frequency", required = false, defaultValue = "") String frequency,
        @RequestParam(name = "power", required = false, defaultValue = "") String power,
        @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return engineService.count(
            query, types, manufacturers, phase, // classify block
            voltage, frequency, power // filters block
        );
    }

    @GetMapping("/engines/{engineId}")
    public Object getOne(
            @PathVariable int engineId,
            @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails) {
        return engineService.getOne(engineId, withDetails);
    }

    @PutMapping("/engines/create")
    public int createEngine(@RequestBody EngineUpload engine) {
        return engineService.save(engine);
    }

    @GetMapping("/engines/{id}/delete")
    public byte deleteEngine(@PathVariable int id) {
        return engineService.delete(id);
    }
}

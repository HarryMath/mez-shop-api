package com.mez.api.controllers;

import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.service.EngineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        @RequestParam(name = "orderBy", required = false, defaultValue = "engines.name DESC") String orderBy,
        @RequestParam(name = "types", required = false, defaultValue = "") String types,
        @RequestParam(name = "manufacturers", required = false, defaultValue = "") String manufacturers,
        @RequestParam(name = "phase", required = false, defaultValue = "") String phase,
        @RequestParam(name = "efficiency", required = false, defaultValue = "") String efficiency,
        @RequestParam(name = "frequency", required = false, defaultValue = "") String frequency,
        @RequestParam(name = "power", required = false, defaultValue = "") String power,
        @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return engineService.find(
            offset, amount, orderBy, query, // order block
            types, manufacturers, phase, // classify block
            efficiency, frequency, power // filters block
        );
    }

    @GetMapping("/engines/count")
    public int countEngines(
        @RequestParam(name = "types", required = false, defaultValue = "") String types,
        @RequestParam(name = "manufacturers", required = false, defaultValue = "") String manufacturers,
        @RequestParam(name = "phase", required = false, defaultValue = "") String phase,
        @RequestParam(name = "efficiency", required = false, defaultValue = "") String efficiency,
        @RequestParam(name = "frequency", required = false, defaultValue = "") String frequency,
        @RequestParam(name = "power", required = false, defaultValue = "") String power,
        @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return engineService.count(
            query, types, manufacturers, phase, // classify block
            efficiency, frequency, power // filters block
        );
    }

    @GetMapping("/engines/{engineName}")
    public Object getOne(
            @PathVariable String engineName,
            @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails) {
        return engineService.getOne(engineName, withDetails);
    }

    @PutMapping("/engines/create")
    public byte createEngine(@RequestBody EngineUpload engine) {
        return engineService.save(engine,
            true); // isNew
    }

    @PutMapping("/engines/update")
    public byte updateEngine(@RequestBody EngineUpload engine) {
        return engineService.save(engine,
            false); // isNew
    }

    @RequestMapping("/engines/loadFromFile")
    public int saveFromFile(@RequestBody MultipartFile file) {
        return engineService.saveFormFile(file);
    }

    @GetMapping("/engines/{engineName}/delete")
    public byte deleteEngine(@PathVariable String engineName) {
        return engineService.delete(engineName);
    }
}

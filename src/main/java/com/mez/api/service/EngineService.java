package com.mez.api.service;

import com.mez.api.models.Characteristic;
import com.mez.api.models.DTO.EngineDetails;
import com.mez.api.models.Engine;
import com.mez.api.repository.EngineRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EngineService {

    private final EngineRepository engineRepository;
    private final ModelMapper modelMapper;

    @Autowired
    EngineService(EngineRepository repository) {
        this.engineRepository = repository;
        this.modelMapper = new ModelMapper();
    }

    public Object get(
            int offset,
            int limit,
            boolean withDetails
    ) {
        List<Engine> engines = engineRepository.get(limit, offset);
        return withDetails ?
                convertToDetails(engines) :
                engines;
    }

    public EngineDetails convertToDetails(Engine engine) {
        EngineDetails engineDetails = modelMapper.map(engine, EngineDetails.class);
        List<Characteristic> characteristicsList = engineRepository.getCharacteristics(engine.getId());
        Map<String, String> characteristicsMap = new HashMap<>();
        characteristicsList.forEach(characteristic -> {
            characteristicsMap.put(characteristic.getName(), characteristic.getValue());
        });
        engineDetails.setCharacteristics( characteristicsMap );
        return engineDetails;
    }

    public List<EngineDetails> convertToDetails(List<Engine> engines) {
        List<EngineDetails> enginesWithCharacteristics = new ArrayList<>();
        engines.forEach(engine -> {
            enginesWithCharacteristics.add(
                    convertToDetails(engine)
            );
        });
        return enginesWithCharacteristics;
    }

}

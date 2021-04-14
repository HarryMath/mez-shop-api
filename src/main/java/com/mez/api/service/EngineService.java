package com.mez.api.service;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.DTO.EngineDetails;
import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.Engine;
import com.mez.api.models.EngineType;
import com.mez.api.repository.EngineRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EngineService {

    private final EngineRepository engineRepository;
    private final ModelMapper modelMapper;

    @Autowired
    EngineService(EngineRepository repository) {
        this.engineRepository = repository;
        this.modelMapper = new ModelMapper();
    }

    public List<EnginePreview> getPreview(int offset, int limit) {
        List<Engine> engines = engineRepository.get(limit, offset);
        return convertToPreview(engines);
    }

    public Object getOne(int id, boolean withDetails) {
        Engine engine = engineRepository.getById(id);
        return withDetails ?
                convertToDetails(engine) :
                engine;
    }

    public int getAmount() {
        return engineRepository.count();
    }

    public EnginePreview convertToPreview(Engine engine) {
        EnginePreview enginePreview = modelMapper.map(engine, EnginePreview.class);
        enginePreview.setCharacteristics(
                engineRepository.getCharacteristics(engine.getId())
        );
        return enginePreview;
    }

    public EngineDetails convertToDetails(Engine engine) {
        EngineDetails engineDetails = modelMapper.map(engine, EngineDetails.class);
        List<CharacteristicsRow> characteristics = engineRepository.getCharacteristics(engine.getId());
        List<String> photos = engineRepository.getPhotos(engine.getId());
        EngineType type = engineRepository.getType(engine.getType());
        engineDetails.setCharacteristics(characteristics);
        engineDetails.setType(type);
        engineDetails.setPhotos(photos);
        return engineDetails;
    }

    public List<EnginePreview> convertToPreview(List<Engine> engines) {
        List<EnginePreview> enginesWithCharacteristics = new ArrayList<>();
        engines.forEach(engine -> {
            enginesWithCharacteristics.add(
                    convertToPreview(engine)
            );
        });
        return enginesWithCharacteristics;
    }
}
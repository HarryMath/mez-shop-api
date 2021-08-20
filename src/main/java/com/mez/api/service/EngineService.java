package com.mez.api.service;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.DTO.EngineDetails;
import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.DTO.EngineUpload;
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

    public Object get(int offset, int limit, boolean withDetails) {
        List<Engine> engines = engineRepository.get(limit, offset);
        return withDetails ?
                convertToDetails(engines) :
                convertToPreview(engines);
    }

    public List<EnginePreview> find(
        int offset, int amount, String orderBy, String query,
        String types, String manufacturers, String phase,
        String efficiency, String frequency, String power
    ) {
        return convertToPreview(
            engineRepository.find(
                offset, amount, orderBy, query,
                types, manufacturers, phase,
                efficiency, frequency, power)
        );
    }

    public Object getOne(String name, boolean withDetails) {
        Engine engine = engineRepository.getById(name);
        return withDetails ?
                convertToDetails(engine) :
                engine;
    }

    public int getAmount() {
        return engineRepository.getAmount();
    }

    public int count(String query, String types, String manufacturers, String phase, String efficiency, String frequency, String power) {
        return engineRepository.count(
            query, types, manufacturers, phase,
            efficiency, frequency, power
        );
    }

    public byte save(EngineUpload engineDTO, boolean isNew) {
        List<String> photosUrls = engineDTO.getPhotos();
        List<CharacteristicsRow> characteristics = engineDTO.getCharacteristics();
        Engine engine = convertFormUploadDTO(engineDTO);
        return engineRepository.save(engine, characteristics, photosUrls, isNew);
    }

    public byte delete(String name) {
        return engineRepository.delete(name);
    }

    public Engine convertFormUploadDTO(EngineUpload engineUpload) {
        return modelMapper.map(engineUpload, Engine.class);
    }

    public EnginePreview convertToPreview(Engine engine) {
        EnginePreview enginePreview = modelMapper.map(engine, EnginePreview.class);
        enginePreview.setMinPrice(
            Math.min(engine.getPriceLapy(),
            Math.min(engine.getPriceFlanets(), engine.getPriceCombi()))
        );
        enginePreview.setCharacteristics(
                engineRepository.getCharacteristics(engine.getName())
        );
        return enginePreview;
    }

    public EngineDetails convertToDetails(Engine engine) {
        EngineDetails engineDetails = modelMapper.map(engine, EngineDetails.class);
        List<CharacteristicsRow> characteristics = engineRepository.getCharacteristics(engine.getName());
        List<String> photos = engineRepository.getPhotos(engine.getName());
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
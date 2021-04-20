package com.mez.api.service;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.DTO.EngineDetails;
import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.models.Engine;
import com.mez.api.models.EngineType;
import com.mez.api.repository.EngineRepository;

import com.mez.api.tools.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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

    public Object getOne(int id, boolean withDetails) {
        Engine engine = engineRepository.getById(id);
        return withDetails ?
                convertToDetails(engine) :
                engine;
    }

    public int getAmount() {
        return engineRepository.count();
    }

    public int save(EngineUpload engine) {
        List<String> photosUrls = engine.getPhotos();
        List<CharacteristicsRow> characteristics = engine.getCharacteristics();
        try {
            int id = engineRepository.save( convertFormUploadDTO(engine) );
            if ( photosUrls.size() > 0 ) engineRepository.savePhotos(photosUrls, id);
            engineRepository.saveCharacteristics(characteristics, id);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseCodes.DATABASE_ERROR;
        }
    }

    public byte delete(int id) {
        return engineRepository.delete(id);
    }

    public Engine convertFormUploadDTO(EngineUpload engineUpload) {
        return modelMapper.map(engineUpload, Engine.class);
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
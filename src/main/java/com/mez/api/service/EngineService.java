package com.mez.api.service;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.DTO.EngineDetails;
import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.models.Engine;
import com.mez.api.models.EngineType;
import com.mez.api.models.Photo;
import com.mez.api.repository.CharacteristicsRepository;
import com.mez.api.repository.EngineRepository;
import com.mez.api.tools.ImageRepository;
import com.mez.api.tools.ResponseCodes;
import com.mez.api.tools.excell.ExcelParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EngineService {

  private final EngineRepository engineRepository;
  private final CharacteristicsRepository characteristicsRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Autowired
  EngineService(EngineRepository repository1, CharacteristicsRepository repository2,
      ImageRepository repository3) {
    this.engineRepository = repository1;
    this.characteristicsRepository = repository2;
    this.imageRepository = repository3;
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
      String efficiency, String frequency, String power, String axisHeight
  ) {
    return convertToPreview(
        engineRepository.find(
            offset, amount, orderBy, query,
            types, manufacturers, phase,
            efficiency, frequency, power, axisHeight)
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

  public int count(String query, String types, String manufacturers, String phase,
      String efficiency, String frequency, String power, String axisHeight) {
    return engineRepository.count(
        query, types, manufacturers, phase,
        efficiency, frequency, power, axisHeight
    );
  }

  public int saveFormFile(MultipartFile file) {
    int savedAmount = 0;
    List<EngineUpload> engines;
    try {
      engines = new ExcelParser().parseEngines(file);
    } catch (IOException e) {
      return ResponseCodes.BAD_FILE;
    }
    for (EngineUpload engine : engines) {
      if (this.save(engine) == ResponseCodes.SUCCESS) {
        savedAmount++;
      }
    }
    return savedAmount;
  }

  private byte save(EngineUpload engineDTO) {
    List<CharacteristicsRow> characteristics = engineDTO.getCharacteristics();
    Engine engine = convertFormUploadDTO(engineDTO);
    return engineRepository.save(engine, characteristics);
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
    List<CharacteristicsRow> characteristics = engineRepository
        .getCharacteristics(engine.getName());
    List<String> photos = engineRepository.getPhotos(engine.getName());
    EngineType type = engineRepository.getType(engine.getType());
    engineDetails.setCharacteristics(characteristics);
    engineDetails.setType(type);
    engineDetails.setPhotos(photos);
    return engineDetails;
  }

  public List<EnginePreview> convertToPreview(List<Engine> engines) {
    List<EnginePreview> enginesWithCharacteristics = new ArrayList<>();
    if (engines.size() <= 24) {
      engines.forEach(engine -> {
        enginesWithCharacteristics.add(
            convertToPreview(engine)
        );
      });
    } else {
      List<CharacteristicsRow> characteristics = characteristicsRepository.getAll();
      engines.forEach(engine -> {
        EnginePreview enginePreview = modelMapper.map(engine, EnginePreview.class);
        enginePreview.setCharacteristics(new ArrayList<>());
        for (int i = 0; i < characteristics.size(); i++) {
          CharacteristicsRow row = characteristics.get(i);
          if ( engine.getName().equals(row.getEngineName()) ) {
            enginePreview.getCharacteristics().add(row);
            characteristics.remove(i--);
          }
        }
        enginesWithCharacteristics.add(enginePreview);
      });
    }
    return enginesWithCharacteristics;
  }

  public List<EngineDetails> convertToDetails(List<Engine> engines) {
    List<EngineDetails> enginesWithCharacteristics = new ArrayList<>();
    if (engines.size() <= 12) {
      engines.forEach(engine -> {
        enginesWithCharacteristics.add(
            convertToDetails(engine)
        );
      });
    } else {
      List<CharacteristicsRow> characteristics = characteristicsRepository.getAll();
      List<Photo> photos = engineRepository.getPhotos();
      engines.forEach(engine -> {
        EngineDetails engineDetails = modelMapper.map(engine, EngineDetails.class);
        EngineType type = new EngineType();
        type.setName(engine.getType());
        engineDetails.setType(type);
        engineDetails.setCharacteristics(new ArrayList<>());
        engineDetails.setPhotos(new ArrayList<>());
        for (int i = 0; i < characteristics.size(); i++) {
          CharacteristicsRow row = characteristics.get(i);
          if ( engine.getName().equals(row.getEngineName()) ) {
            engineDetails.getCharacteristics().add(row);
            characteristics.remove(i--);
          }
        }
        for (int i = 0; i < photos.size(); i++) {
          Photo photo = photos.get(i);
          if ( engine.getName().equals(photo.getEngineName()) ) {
            engineDetails.getPhotos().add(photo.getPhoto());
            photos.remove(i--);
          }
        }
        enginesWithCharacteristics.add(engineDetails);
      });
    }
    return enginesWithCharacteristics;
  }
}
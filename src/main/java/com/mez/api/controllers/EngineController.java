package com.mez.api.controllers;

import com.mez.api.models.DTO.EnginePreview;
import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.service.AuthorisationService;
import com.mez.api.service.EngineService;
import com.mez.api.tools.ResponseCodes;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EngineController {

  private final EngineService engineService;
  private final AuthorisationService authService;

  @Autowired
  EngineController(EngineService service, AuthorisationService authService) {
    this.engineService = service;
    this.authService = authService;
  }

  @GetMapping("/engines")
  public Object getEngines(
      @RequestParam(name = "amount", required = false, defaultValue = "99999") int amount,
      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
      @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
      return engineService.get(offset, amount, withDetails);
    }
    return null;
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
      @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails
  ) {
    String decodedName = engineName.replaceAll("%252F", "/");
    decodedName = decodedName.replaceAll("%2F", "/");
    return engineService.getOne(decodedName, withDetails);
  }

  @PutMapping("/engines/create")
  public byte createEngine(
      @RequestBody EngineUpload engine,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
      return engineService.save(engine,
          true); // isNew
    }
    return ResponseCodes.UNAUTHORISED;
  }

  @PutMapping("/engines/update")
  public byte updateEngine(@RequestBody EngineUpload engine,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
      return engineService.save(engine,
          false); // isNew
    }
    return ResponseCodes.UNAUTHORISED;
  }

  @RequestMapping("/engines/loadFromFile")
  public int saveFromFile(
      @RequestBody MultipartFile file,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
      return engineService.saveFormFile(file);
    }
    return ResponseCodes.UNAUTHORISED;
  }

  @GetMapping("/engines/{engineName}/delete")
  public byte deleteEngine(
      @PathVariable String engineName,
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    if (authService.isAuthorised(token)) {
      return engineService.delete(engineName);
    }
    return ResponseCodes.UNAUTHORISED;
  }
}

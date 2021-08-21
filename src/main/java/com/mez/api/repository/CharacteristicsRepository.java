package com.mez.api.repository;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.tools.DAO;
import org.springframework.stereotype.Component;

@Component
public class CharacteristicsRepository extends Repository<CharacteristicsRow> {

  public CharacteristicsRepository(DAO dao) {
    super(dao, "characteristics");
  }
}

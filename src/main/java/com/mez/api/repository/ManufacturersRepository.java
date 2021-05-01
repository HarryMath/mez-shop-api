package com.mez.api.repository;

import com.mez.api.models.Manufacturer;
import com.mez.api.tools.DAO;
import com.mez.api.tools.ResponseCodes;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManufacturersRepository {

  DAO dao;

  @Autowired
  ManufacturersRepository(DAO dao) {
    this.dao = dao;
    try {
      this.dao.openConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Manufacturer> getAll() {
    return dao.executeListQuery("SELECT * FROM manufacturers", Manufacturer.class);
  }

  public int countEngines(String name) {
    return (int) dao.countQuery("SELECT count(*) FROM engines WHERE manufacturer = \"" + name.trim() + "\"");
  }

  public int countManufacturers(String name) {
    return (int) dao.countQuery("SELECT count(*) FROM manufacturers WHERE name = \"" + name.trim() + "\"");
  }

  public byte save(Manufacturer manufacturer) {
    try {
      dao.executeUpdate(
          "INSERT INTO manufacturers (name) " +
              "values ( \"" + manufacturer.getName().trim() + "\");"
      );
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public byte delete(String name) {
    try {
      dao.executeUpdate("DELETE FROM manufacturers WHERE name = \"" + name.trim() + "\"");
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }
}

package com.mez.api.repository;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.Engine;
import com.mez.api.models.EngineType;
import com.mez.api.tools.DAO;
import com.mez.api.tools.ResponseCodes;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EngineRepository {

  DAO dao;

  @Autowired
  EngineRepository(DAO dao) {
    this.dao = dao;
    try {
      this.dao.openConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void save(Engine engine) throws SQLException {
    dao.executeUpdate(
        "INSERT INTO engines (name, type, manufacturer, photo, price, mass) " +
        "values ( \"" +
        engine.getName() + "\" , \"" +
        engine.getType() + "\", \"" +
        engine.getManufacturer() + "\", \"" +
        engine.getPhoto() + "\", " +
        engine.getPrice() + ", " +
        engine.getMass() + ");");
  }

  private void update(Engine engine) throws SQLException {
    dao.executeUpdate(
        "UPDATE engines SET " +
        "type = \"" + engine.getType() + "\", " +
        "manufacturer = \"" + engine.getManufacturer() + "\", " +
        "photo = \"" + engine.getPhoto() + "\", " +
        "price = " + engine.getPrice() + ", " +
        "mass = " + engine.getMass() +
        "WHERE name = \"" + engine.getName() + "\"");
  }

  public byte save(Engine engine, List<CharacteristicsRow> rows, List<String> photos, boolean isNew) {
    if (engineExist(engine.getName()) && isNew) {
      return ResponseCodes.ALREADY_EXISTS;
    }
    try {
      if (isNew) {
        save(engine);
      } else {
        update(engine);
      }
      final boolean deleteOld = !isNew;
      if (photos.size() > 0) {
        savePhotos(photos, engine.getName(), deleteOld);
      }
      if (rows.size() > 0) {
        saveCharacteristics(rows, engine.getName(), deleteOld);
      }
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      System.out.println("engine not saved: " + e.getMessage());
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public void savePhotos(List<String> photos, String engineName, boolean deleteOld) {
    if (photos.size() == 0) {
      return;
    }
    String query = "INSERT INTO photos (engineName, photo)  values ";
    for (int i = 0; i < photos.size(); i++) {
      query += "(\"" + engineName + "\", \"" + photos.get(i) + "\")";
      query += (i == photos.size() - 1 ? ";" : ",");
    }
    try {
      if (deleteOld) {
        dao.executeUpdate("DELETE FROM photos WHERE engineName = \"" + engineName + "\"");
      }
      dao.executeUpdate(query);
    } catch (SQLException e) {
      System.out.println("photos not saved: " + e.getMessage());
    }
  }

  public void saveCharacteristics(List<CharacteristicsRow> rows, String engineName, boolean deleteOld) {
    String query = "INSERT INTO characteristics " +
        "(engineName, power, frequency, efficiency, cosFi, electricityNominal220, electricityNominal380, "
        + "electricityRatio, momentsRatio, momentsMaxRatio, momentsMinRatio)  values ";
    for (int i = 0; i < rows.size(); i++) {
      CharacteristicsRow row = rows.get(i);
      query += "(\"" + engineName + "\", "
          + row.getPower() + ", " +
          +row.getFrequency() + ", " +
          +row.getEfficiency() + ", " +
          +row.getCosFi() + ", " +
          +row.getElectricityNominal220() + ", " +
          +row.getElectricityNominal380() + ", " +
          +row.getElectricityRatio() + ", " +
          +row.getMomentsRatio() + ", " +
          +row.getMomentsMaxRatio() + ", " +
          +row.getMomentsMinRatio() + ")";
      query += (i == rows.size() - 1 ? ";" : ",");
    }
    System.out.println(query);
    try {
      if (deleteOld) {
        dao.executeUpdate("DELETE FROM characteristics WHERE engineName = \"" + engineName + "\"");
      }
      dao.executeUpdate(query);
    } catch (SQLException e) {
      System.out.println("characteristics not saved: " + e.getMessage());
    }
  }

  public List<Engine> get(int limit, int offset) {
    return dao.executeListQuery(
        "SELECT * FROM engines ORDER BY name LIMIT " + limit + " OFFSET " + offset,
        Engine.class);
  }

  public List<Engine> find(
      int offset, int amount, String orderBy, String query,
      String types, String manufacturers, String phase,
      String efficiency, String frequency, String power
  ) {
    String querySQL = "SELECT"
        + " engines.name, engines.manufacturer, engines.type, engines.price, engines.photo, engines.mass"
        + " FROM engines RIGHT JOIN characteristics ON engines.name = characteristics.engineName"
        + " WHERE length(engines.name) > 0 ";
    if (query.length() > 2) {
      querySQL += "AND concat("
          + "(SELECT concat(engineTypes.fullDescription, engineTypes.shortDescription) FROM engineTypes WHERE engineTypes.name = engines.type), "
          + "manufacturer, name, type) REGEXP '" + query + "' ";
    }
    if (types.length() > 0) {
      String[] separatedTypes = types.split(",");
      querySQL += "AND type in (";
      for (byte i = 0; i < separatedTypes.length; i++) {
        querySQL += "'" + separatedTypes[i] + "'";
        if (i != separatedTypes.length - 1) {
          querySQL += ", ";
        }
      }
      querySQL += ") ";
    }
    if (manufacturers.length() > 0) {
      String[] separatedManufacturers = manufacturers.split(",");
      querySQL += "AND manufacturer in (";
      for (byte i = 0; i < separatedManufacturers.length; i++) {
        querySQL += "'" + separatedManufacturers[i] + "'";
        if (i != separatedManufacturers.length - 1) {
          querySQL += ", ";
        }
      }
      querySQL += ") ";
    }
    if (phase.length() > 0) {
      querySQL += "AND ( "
          + "(SELECT count(*) FROM characteristics WHERE characteristics.engineName = engines.name)"
          + " in (";
      String[] separatedPhase = phase.split(",");
      for (byte i = 0; i < separatedPhase.length; i++) {
        querySQL += separatedPhase[i];
        if (i != separatedPhase.length - 1) {
          querySQL += ", ";
        }
      }
      querySQL += ")) ";
    }
    if (efficiency.length() > 2) {
      querySQL += "AND (";
      String[] separated = efficiency.split(",");
      for (byte i = 0; i < separated.length; i++) {
        String[] range = separated[i].split("-");
        querySQL += "efficiency BETWEEN " + range[0] + " AND " + range[1] + "";
        if (i != separated.length - 1) {
          querySQL += " OR ";
        }
      }
      querySQL += ")";
    }
    if (frequency.length() > 2) {
      querySQL += "AND (";
      String[] separated = frequency.split(",");
      for (byte i = 0; i < separated.length; i++) {
        String[] range = separated[i].split("-");
        querySQL += "frequency BETWEEN " + range[0] + " AND " + range[1] + "";
        if (i != separated.length - 1) {
          querySQL += " OR ";
        }
      }
      querySQL += ")";
    }
    if (power.length() > 2) {
      querySQL += "AND (";
      String[] separated = power.split(",");
      for (byte i = 0; i < separated.length; i++) {
        String[] range = separated[i].split("-");
        querySQL += "power BETWEEN " + range[0] + " AND " + range[1] + "";
        if (i != separated.length - 1) {
          querySQL += " OR ";
        }
      }
      querySQL += ") ";
    }
    querySQL += "GROUP BY engines.name ";
    querySQL += "ORDER BY " + orderBy + " LIMIT " + amount + " OFFSET " + offset;
    return dao.executeListQuery(querySQL, Engine.class);
  }

  public int count(String query,
      String types, String manufacturers, String phase,
      String efficiency, String frequency, String power
  ) {
    String querySQL = "SELECT count(*) FROM"
        + " (SELECT engines.name"
        + " FROM engines RIGHT JOIN characteristics ON engines.name = characteristics.engineName"
        + " WHERE  length(engines.name) > 0 ";
    if (query.length() > 2) {
      querySQL += "AND concat("
          + "(SELECT concat(engineTypes.fullDescription, engineTypes.shortDescription) FROM engineTypes WHERE engineTypes.name = engines.type), "
          + "manufacturer, name, type) REGEXP '" + query + "' ";
    }
    if (types.length() > 1) {
      String[] separatedTypes = types.split(",");
      querySQL += "AND type in (";
      for (byte i = 0; i < separatedTypes.length; i++) {
        querySQL += "'" + separatedTypes[i] + "'";
        if (i != separatedTypes.length - 1) {
          querySQL += ", ";
        }
      }
      querySQL += ") ";
    }
    if (manufacturers.length() > 1) {
      String[] separatedManufacturers = manufacturers.split(",");
      querySQL += "AND manufacturer in (";
      for (byte i = 0; i < separatedManufacturers.length; i++) {
        querySQL += "'" + separatedManufacturers[i] + "'";
        if (i != separatedManufacturers.length - 1) {
          querySQL += ", ";
        }
      }
      querySQL += ") ";
    }
    if (phase.length() > 1) {
      querySQL += "AND ( "
          + "(SELECT count(*) FROM characteristics WHERE characteristics.engineName = engines.name)"
          + " in (";
      String[] separatedPhase = phase.split(",");
      for (byte i = 0; i < separatedPhase.length; i++) {
        querySQL += separatedPhase[i];
        if (i != separatedPhase.length - 1) {
          querySQL += ", ";
        }
      }
      querySQL += ")) ";
    }
    if (efficiency.length() > 3) {
      querySQL += "AND (";
      String[] separated = efficiency.split(",");
      for (byte i = 0; i < separated.length; i++) {
        String[] range = separated[i].split("-");
        querySQL += "efficiency BETWEEN " + range[0] + " AND " + range[1] + "";
        if (i != separated.length - 1) {
          querySQL += " OR ";
        }
      }
      querySQL += ") ";
    }
    if (frequency.length() > 3) {
      querySQL += "AND (";
      String[] separated = frequency.split(",");
      for (byte i = 0; i < separated.length; i++) {
        String[] range = separated[i].split("-");
        querySQL += "frequency BETWEEN " + range[0] + " AND " + range[1] + "";
        if (i != separated.length - 1) {
          querySQL += " OR ";
        }
      }
      querySQL += ") ";
    }
    if (power.length() > 3) {
      querySQL += "AND (";
      String[] separated = power.split(",");
      for (byte i = 0; i < separated.length; i++) {
        String[] range = separated[i].split("-");
        querySQL += "power BETWEEN " + range[0] + " AND " + range[1] + "";
        if (i != separated.length - 1) {
          querySQL += " OR ";
        }
      }
      querySQL += ") ";
    }
    querySQL += "GROUP BY engines.name) as a";
    return (int) dao.countQuery(querySQL);
  }

  public byte delete(String name) {
    try {
      dao.executeUpdate("DELETE FROM engines WHERE name = \"" + name + "\"");
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public Engine getById(String name) {
    return dao.executeQuery(
        "SELECT * FROM engines WHERE name = \"" + name + "\"",
        Engine.class);
  }

  public boolean engineExist(String name) {
    return dao.countQuery(
        "SELECT count(*) FROM engines WHERE name = \"" + name + "\""
    ) > 0;
  }

  public int getAmount() {
    return (int) dao.countQuery("SELECT count(*) FROM engines");
  }

  public List<String> getPhotos(String engineName) {
    return dao.columnQuery("SELECT photo FROM photos WHERE engineName = \"" + engineName + "\"");
  }

  public EngineType getType(String typeId) {
    return dao.executeQuery(
        "SELECT * FROM engineTypes WHERE name = \"" + typeId + "\"",
        EngineType.class);
  }

  public List<CharacteristicsRow> getCharacteristics(String engineName) {
    return dao.executeListQuery(
        "SELECT * FROM characteristics WHERE engineName = \"" + engineName + "\"",
        CharacteristicsRow.class);
  }

}
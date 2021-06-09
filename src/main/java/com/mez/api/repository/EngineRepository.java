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

  public int save(Engine engine) throws SQLException {
    dao.executeUpdate(
        "INSERT INTO engines (name, type, manufacturer, photo, price, mass) " +
            "values ( \"" +
            engine.getName() + "\" , \"" +
            engine.getType() + "\", \"" +
            engine.getManufacturer() + "\", \"" +
            engine.getPhoto() + "\", " +
            engine.getPrice() + ", " +
            engine.getMass() + ");"
    ); // return id of saved engine
    return (int) dao.countQuery("SELECT max(id) FROM engines");
  }

  public int save(Engine engine, List<CharacteristicsRow> rows, List<String> photos) {
    try {
      int id = save(engine);
      if (photos.size() > 0) {
        savePhotos(photos, id);
      }
      saveCharacteristics(rows, id);
      if (engine.getId() > 0) {
        delete(engine.getId());
      }
      return id;
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public void savePhotos(List<String> photos, int engineId) {
    if (photos.size() == 0) {
      return;
    }
    String query = "INSERT INTO photos (engineId, photo)  values ";
    for (int i = 0; i < photos.size(); i++) {
      query += "(" + engineId + ", \"" + photos.get(i) + "\")";
      query += (i == photos.size() - 1 ? ";" : ",");
    }
    try {
      dao.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void saveCharacteristics(List<CharacteristicsRow> rows, int engineId) {
    String query = "INSERT INTO characteristics " +
        "(engineId, power, frequency, efficiency, cosFi, electricityNominal220, electricityNominal380, "
        +
        "electricityRatio, momentsRatio, momentsMaxRatio, momentsMinRatio)  values ";
    for (int i = 0; i < rows.size(); i++) {
      CharacteristicsRow row = rows.get(i);
      query += "(" + engineId + ", "
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
      dao.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Engine> get(int limit, int offset) {
    return dao.executeListQuery(
        "SELECT * FROM engines ORDER BY id LIMIT " + limit + " OFFSET " + offset,
        Engine.class);
  }

  public List<Engine> find(
      int offset, int amount, String orderBy, String query,
      String types, String manufacturers, String phase,
      String efficiency, String frequency, String power
  ) {
    String querySQL = "SELECT"
        + " engines.id, engines.name, engines.manufacturer, engines.type, engines.price, engines.photo, engines.mass"
        + " FROM engines RIGHT JOIN characteristics ON engines.id = characteristics.engineId"
        + " WHERE engines.id > 0 ";
    if (query.length() > 0) {
      querySQL += "AND (type LIKE '%" + query +
          "%' OR manufacturer LIKE '%" + query +
          "%' OR name LIKE '%" + query + "%') ";
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
          + "(SELECT count(*) FROM characteristics WHERE characteristics.engineId = engines.id)"
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
    querySQL += "GROUP BY engines.id ";
    querySQL += "ORDER BY " + orderBy + " LIMIT " + amount + " OFFSET " + offset;
    return dao.executeListQuery(querySQL, Engine.class);
  }

  public byte delete(int id) {
    try {
      dao.executeUpdate("DELETE FROM engines WHERE id = " + id);
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public Engine getById(int id) {
    return dao.executeQuery("SELECT * FROM engines WHERE id = " + id, Engine.class);
  }

  public int getAmount() {
    return (int) dao.countQuery("SELECT count(*) FROM engines");
  }

  public int count(String query, String types, String manufacturers, String phase, String efficiency, String frequency, String power) {
    String querySQL = "SELECT count(*) FROM"
        + " (SELECT engines.id"
        + " FROM engines RIGHT JOIN characteristics ON engines.id = characteristics.engineId"
        + " WHERE engines.id > 0 ";
    if (query.length() > 0) {
      querySQL += "AND (type LIKE '%" + query +
          "%' OR manufacturer LIKE '%" + query +
          "%' OR name LIKE '%" + query + "%') ";
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
          + "(SELECT count(*) FROM characteristics WHERE characteristics.engineId = engines.id)"
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
    querySQL += "GROUP BY engines.id) as a";
    return (int) dao.countQuery(querySQL);
  }

  public List<String> getPhotos(int engineId) {
    return dao.columnQuery("SELECT photo FROM photos WHERE engineId = " + engineId);
  }

  public EngineType getType(String typeId) {
    return dao.executeQuery("SELECT * FROM engineTypes WHERE name = \"" + typeId + "\"",
        EngineType.class);
  }

  public List<CharacteristicsRow> getCharacteristics(int engineId) {
    return dao.executeListQuery(
        "SELECT * FROM characteristics WHERE engineId = " + engineId,
        CharacteristicsRow.class);
  }

}
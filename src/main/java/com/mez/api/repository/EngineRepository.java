package com.mez.api.repository;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.Engine;
import com.mez.api.models.EngineType;
import com.mez.api.tools.DAO;
import com.mez.api.tools.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
        return dao.countQuery("SELECT max(id) FROM engines");
    }

    public int save(Engine engine, List<CharacteristicsRow> rows, List<String> photos) {
        try {
            int id = save( engine );
            if ( photos.size() > 0 ) savePhotos(photos, id);
            saveCharacteristics(rows, id);
            if (engine.getId() > 0) {
                dao.executeUpdate("UPDATE photos SET engineId = " + id + " WHERE engineId = " + engine.getId());
                delete(engine.getId());
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseCodes.DATABASE_ERROR;
        }
    }

    public void savePhotos(List<String> photos, int engineId) {
        if(photos.size() == 0) return;
        String query = "INSERT INTO photos (engineId, photo)  values ";
        for(int i = 0; i < photos.size(); i++) {
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
                "(engineId, power, frequency, efficiency, cosFi, electricityNominal220, electricityNominal380, " +
                "electricityRatio, momentsRatio, momentsMaxRatio, momentsMinRatio)  values ";
        for(int i = 0; i < rows.size(); i++) {
            CharacteristicsRow row = rows.get(i);
            query += "(" + engineId + ", "
                    + row.getPower() + ", " +
                    + row.getFrequency() + ", " +
                    + row.getEfficiency() + ", " +
                    + row.getCosFi() + ", " +
                    + row.getElectricityNominal220() + ", " +
                    + row.getElectricityNominal380() + ", " +
                    + row.getElectricityRatio() + ", " +
                    + row.getMomentsRatio() + ", " +
                    + row.getMomentsMaxRatio() + ", " +
                    + row.getMomentsMinRatio() + ")";
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

    public byte delete(int id) {
        try {
            dao.executeUpdate("DELETE FROM photos WHERE engineId = " + id);
            dao.executeUpdate("DELETE FROM characteristics WHERE engineId = " + id);
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

    public int count() {
        return dao.countQuery("SELECT count(*) FROM engines");
    }

    public List<String> getPhotos(int engineId) {
        return dao.columnQuery("SELECT photo FROM photos WHERE engineId = " + engineId);
    }

    public EngineType getType(String typeId) {
        return dao.executeQuery("SELECT * FROM engineTypes WHERE name = \"" + typeId + "\"", EngineType.class);
    }

    public List<CharacteristicsRow> getCharacteristics(int engineId) {
        return dao.executeListQuery(
                "SELECT * FROM characteristics WHERE engineId = " + engineId,
                CharacteristicsRow.class);
    }
}
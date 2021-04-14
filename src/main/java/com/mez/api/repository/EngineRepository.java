package com.mez.api.repository;

import com.mez.api.models.CharacteristicsRow;
import com.mez.api.models.Engine;
import com.mez.api.models.EngineType;
import com.mez.api.tools.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public List<Engine> get(int limit, int offset) {
        return dao.executeListQuery(
                "SELECT * FROM engines ORDER BY id LIMIT " + limit + " OFFSET " + offset,
                Engine.class);
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
package com.mez.api.repository;

import com.mez.api.models.Characteristic;
import com.mez.api.models.Engine;
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

    public List<Characteristic> getCharacteristics(int engineId) {
        return dao.executeListQuery(
                "SELECT * FROM characteristics WHERE engineId = " + engineId,
                Characteristic.class);
    }
}

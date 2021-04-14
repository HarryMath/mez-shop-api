package com.mez.api.repository;

import com.mez.api.models.EngineType;
import com.mez.api.tools.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class CategoriesRepository {

    DAO dao;
    @Autowired
    CategoriesRepository(DAO dao) {
        this.dao = dao;
        try {
            this.dao.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EngineType> getAll() {
        return dao.executeListQuery("SELECT * FROM engineTypes", EngineType.class);
    }
}

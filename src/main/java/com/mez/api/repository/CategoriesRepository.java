package com.mez.api.repository;

import com.mez.api.models.DTO.CategoryPreview;
import com.mez.api.models.EngineType;
import com.mez.api.tools.DAO;
import com.mez.api.tools.ResponseCodes;
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

    public EngineType getByName(String name) {
        return dao.executeQuery("SELECT * FROM engineTypes WHERE name = \"" + name + "\"", EngineType.class);
    }

    public List<CategoryPreview> getPreviews() {
        return dao.executeListQuery("SELECT name, photo, shortDescription FROM engineTypes", CategoryPreview.class);
    }

    public byte save(EngineType category) {
        try {
            dao.executeUpdate(
                "INSERT INTO engineTypes (name, photo, shortDescription, fullDescription) " +
                    "values ( \"" +
                    category.getName() + "\" , \"" +
                    category.getPhoto() + "\", \"" +
                    category.getShortDescription() + "\", \"" +
                    category.getFullDescription() + "\");"
            );
            return ResponseCodes.SUCCESS;
        } catch (SQLException throwables) {
            return ResponseCodes.DATABASE_ERROR;
        }
    }

    @Deprecated
    public byte update(EngineType category) {
        try {
            dao.executeUpdate(
                "UPDATE engineTypes SET " +
                    "values ( \"" +
                    category.getName() + "\" , \"" +
                    category.getPhoto() + "\", \"" +
                    category.getShortDescription() + "\", \"" +
                    category.getFullDescription() + "\");"
            );
            return ResponseCodes.SUCCESS;
        } catch (SQLException throwables) {
            return ResponseCodes.DATABASE_ERROR;
        }
    }

    public byte delete(String name) {
        try {
            dao.executeUpdate("DELETE FROM engineTypes WHERE name = \"" + name + "\"");
            return ResponseCodes.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseCodes.DATABASE_ERROR;
        }
    }
}

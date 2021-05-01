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

    public int countEngines(String name) {
        return (int) dao.countQuery("SELECT count(*) FROM engines WHERE type = \"" + name + "\"");
    }

    public int countCategories(String name) {
        return (int) dao.countQuery("SELECT count(*) FROM engineTypes WHERE name = \"" + name + "\"");
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
        } catch (SQLException e) {
            return ResponseCodes.DATABASE_ERROR;
        }
    }

    public byte update(EngineType category) {
        try {
            dao.executeUpdate( "UPDATE engineTypes SET " +
                "photo = \"" + category.getPhoto() + "\", " +
                "shortDescription = \"" + category.getShortDescription() + "\", " +
                "fullDescription = \"" + category.getFullDescription() + "\" " +
                "WHERE name = \"" + category.getName() + "\""
            );
            return ResponseCodes.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
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

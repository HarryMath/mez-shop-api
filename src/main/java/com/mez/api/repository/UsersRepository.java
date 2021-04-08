package com.mez.api.repository;

import com.mez.api.models.User;
import com.mez.api.tools.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class UsersRepository {

    DAO dao;
    @Autowired
    UsersRepository(DAO dao) {
        this.dao = dao;
        try {
            this.dao.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAll() {
        return dao.executeListQuery("SELECT * FROM users", User.class);
    }

    public boolean save(User user) {
        try {
            dao.executeUpdate(
                    "INSERT INTO users (googleId, mail, name, phone, photo, password, isAdmin) " +
                            "values(\"" + user.getGoogleId() + "\", \"" +
                            user.getMail() + "\", \"" +
                            user.getName() + "\", \"" +
                            user.getPhone() + "\", \"" +
                            user.getPhoto() + "\", \"" +
                            user.getPassword() + "\", " +
                            user.isAdmin() + ")"
            );
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(User user) {
        try {
            dao.executeUpdate(
                    "UPDATE users SET " +
                            "googleId = \"" + user.getGoogleId() + "\", " +
                            "mail = \"" + user.getMail() + "\", " +
                            "name = \"" + user.getName() + "\", " +
                            "phone = \"" + user.getPhone() + "\", " +
                            "photo = \"" + user.getPhoto() + "\", " +
                            "WHERE id = " + user.getId()
            );
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

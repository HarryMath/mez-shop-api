package com.mez.api.repository;

import com.mez.api.models.User;
import com.mez.api.tools.DAO;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersRepository extends Repository<User> {

  @Autowired
  UsersRepository(DAO dao) {
    super(dao, "users");
  }

  public User getByMailAndPassword(String mail, String password) {
    List<User> possibleUser = dao.executeListQuery(
        "SELECT * FROM users WHERE mail = \"" + mail + "\" AND password = \"" + password + "\"",
        User.class
    );
    return possibleUser.size() > 0 ? possibleUser.get(0) : null;
  }

  public User getByGoogleId(String googleId) {
    return dao.executeQuery("SELECT * FROM users WHERE googleId = " + googleId, User.class);
  }

  public User getByPhoneAndPassword(String phone, String password) {
    List<User> possibleUser = dao.executeListQuery(
        "SELECT * FROM users WHERE phone = \"" + phone + "\" AND password = \"" + password + "\"",
        User.class
    );
    return possibleUser.size() > 0 ? possibleUser.get(0) : null;
  }
}
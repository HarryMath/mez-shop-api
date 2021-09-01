package com.mez.api.repository;

import com.mez.api.models.Admin;
import com.mez.api.tools.DAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaffRepository extends Repository<Admin> {

  @Autowired
  public StaffRepository(DAO dao) {
    super(dao, "staff");
  }

  public Admin getByLoginAndPassword(String login, String password) {
    List<Admin> possibleUser = dao.executeListQuery(
        "SELECT * FROM staff WHERE login = '" + login + "' AND password = MD5('" + password + "')",
        Admin.class
    );
    return possibleUser.size() > 0 ? possibleUser.get(0) : null;
  }
}

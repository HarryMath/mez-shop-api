package com.mez.api.repository;

import com.mez.api.models.Client;
import com.mez.api.tools.DAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersRepository extends Repository<Client> {

  @Autowired
  UsersRepository(DAO dao) {
    super(dao, "clients");
  }

  public Client getByMailAndPassword(String mail, String password) {
    List<Client> possibleClient = dao.executeListQuery(
        "SELECT * FROM users WHERE mail = '" + mail + "' AND password = MD5('" + password + "')",
        Client.class
    );
    return possibleClient.size() > 0 ? possibleClient.get(0) : null;
  }

  public Client getByGoogleId(String googleId) {
    return dao.executeQuery("SELECT * FROM users WHERE googleId = " + googleId, Client.class);
  }

  public Client getByPhoneAndPassword(String phone, String password) {
    List<Client> possibleUser = dao.executeListQuery(
        "SELECT * FROM users WHERE phone = '" + phone + "' AND password = MD5('" + password + "')",
        Client.class
    );
    return possibleUser.size() > 0 ? possibleUser.get(0) : null;
  }
}
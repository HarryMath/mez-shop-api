package com.mez.api.service;

import com.mez.api.models.Client;
import com.mez.api.repository.UsersRepository;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

  private static final Map<String, Client> authorisedUsers = new HashMap<>();
  private final UsersRepository usersRepository;

  @Autowired
  UsersService(UsersRepository repository) {
    this.usersRepository = repository;
  }

  public List<Client> get() {
    return usersRepository.getAll();
  }

  public Client registerWithGoogle(Client user) {
    try {
      usersRepository.save(user);
      return usersRepository.getByGoogleId(user.getGoogleId());
    } catch (SQLException e) {
      return null;
    }
  }

  public Client getById(long id) {
    return usersRepository.getById(String.valueOf(id));
  }
}

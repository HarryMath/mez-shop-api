package com.mez.api.service;

import com.mez.api.models.Admin;
import com.mez.api.repository.StaffRepository;
import com.mez.api.tools.Encryptor;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationService {

  private static final Map<String, Admin> authorisedUsers = new HashMap<>();
  private final StaffRepository usersRepository;

  @Autowired
  AuthorisationService(StaffRepository repository) {
    this.usersRepository = repository;
  }

  public boolean isAuthorised(String token) {
    return authorisedUsers.containsKey(token);
  }

  public Admin getAuthorisedUser(String token) {
    return authorisedUsers.get(token);
  }

  public String authoriseUser(String mailOrPhone, String password) {
    Admin user = usersRepository.getByLoginAndPassword(mailOrPhone, password);
    if (user == null) {
      return null;
    }
    String token = Encryptor.encrypt(
        String.valueOf(authorisedUsers.size() + Math.random()),
        Encryptor.MD5);
    authorisedUsers.put(token, user);
    return token;
  }
}

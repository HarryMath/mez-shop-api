package com.mez.api.service;

import com.mez.api.models.User;
import com.mez.api.repository.UsersRepository;
import com.mez.api.tools.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorisationService {

    private static final Map<String, User> authorisedUsers = new HashMap<>();
    private final UsersRepository usersRepository;

    @Autowired
    AuthorisationService(UsersRepository repository) {
        this.usersRepository = repository;
    }

    public boolean isAuthorised(String token) {
        return authorisedUsers.containsKey(token);
    }

    public User getCurrentUser(String token) {
        return authorisedUsers.get(token);
    }

    boolean authoriseUser(String mailOrPhone, String password, HttpServletResponse response) {
        User user = mailOrPhone.contains("@") ?
                usersRepository.getByMailAndPassword(mailOrPhone, password) :
                usersRepository.getByPhoneAndPassword(mailOrPhone, password);
        if(user == null) return false;
        String token = Encryptor.encrypt(
                String.valueOf(authorisedUsers.size() + Math.random()),
                Encryptor.MD5);
        authorisedUsers.put(token, user);
        Cookie cookieToken = new Cookie("token", token);
        cookieToken.setHttpOnly(true);
        cookieToken.setPath("/");
        response.addCookie(cookieToken);
        return true;
    }
}

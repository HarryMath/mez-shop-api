package com.mez.api.service;

import com.mez.api.models.User;
import com.mez.api.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {

    private static final Map<String, User> authorisedUsers = new HashMap<>();
    private final UsersRepository usersRepository;

    @Autowired
    UsersService(UsersRepository repository) {
        this.usersRepository = repository;
    }

    public List<User> get(int amount, int offset) {
        return usersRepository.get(amount, offset);
    }

    public User registerWithGoogle(User user) {
        return usersRepository.save(user) ?
                usersRepository.getByGoogleId(user.getGoogleId()) :
                null;
    }

    public User getById(long id) {
        return usersRepository.getById(id);
    }
}

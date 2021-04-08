package com.mez.api.controllers;

import com.mez.api.models.User;
import com.mez.api.service.AuthorisationService;
import com.mez.api.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final AuthorisationService authorisationService;

    @Autowired
    public UsersController(UsersService usersService, AuthorisationService authorisationService) {
        this.usersService = usersService;
        this.authorisationService = authorisationService;
    }

    @GetMapping("/users")
    public List<User> get(
            @RequestParam(value = "amount", required = false, defaultValue = "9999") int amount,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @CookieValue(name = "token", required = false, defaultValue = "") String token) {
        return authorisationService.isAuthorised(token) ?
                usersService.get(amount, offset) :
                null;
    }
}

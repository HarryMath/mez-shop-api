package com.mez.api.controllers;

import com.mez.api.models.User;
import com.mez.api.service.AuthorisationService;
import com.mez.api.service.UsersService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

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
      @CookieValue(name = "token", required = false, defaultValue = "") String token) {
    return authorisationService.isAuthorised(token) ?
        usersService.get() :
        null;
  }
}

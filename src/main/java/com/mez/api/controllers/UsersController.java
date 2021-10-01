package com.mez.api.controllers;

import com.mez.api.models.Admin;
import com.mez.api.models.Client;
import com.mez.api.service.AuthorisationService;
import com.mez.api.service.UsersService;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

  private final UsersService usersService;
  private final AuthorisationService authorisationService;

  @Autowired
  public UsersController(UsersService usersService, AuthorisationService authorisationService) {
    this.usersService = usersService;
    this.authorisationService = authorisationService;
  }

  @GetMapping("/users")
  public List<Client> get(
      @CookieValue(name = "token", required = false, defaultValue = "") String token) {
    return authorisationService.isAuthorised(token) ?
        usersService.get() :
        null;
  }

  @GetMapping("/users/current")
  public Admin authoriseUser(
      @CookieValue(name = "token", required = false, defaultValue = "") String token
  ) {
    return authorisationService.getAuthorisedUser(token);
  }

  @RequestMapping("/authorise")
  public String authoriseUser(
      @RequestParam(name = "id") String mailOrPhone,
      @RequestParam(name = "password") String password,
      HttpServletResponse response
  ) {
    String token = authorisationService.authoriseUser(mailOrPhone, password);
    if (token != null &&  token.length() > 0) {
      Cookie cookieToken = new Cookie("token", token);
      cookieToken.setHttpOnly(false);
      cookieToken.setPath("/");
      response.addCookie(cookieToken);
    }
    return token;
  }
}

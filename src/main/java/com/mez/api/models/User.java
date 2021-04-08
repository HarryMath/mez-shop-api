package com.mez.api.models;

import lombok.Data;

@Data
public class User {
    private long id;
    private String googleId;
    private String mail;
    private String name;
    private String phone;
    private String photo;

    private String password;
    private boolean isAdmin;
}

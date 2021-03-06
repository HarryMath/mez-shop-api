package com.mez.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mez.api.tools.annotations.AutoGenerated;
import com.mez.api.tools.annotations.Encrypted;
import com.mez.api.tools.annotations.PrimaryKey;
import lombok.Data;

@Data
public class Client {

    @PrimaryKey
    @AutoGenerated
    private long id;
    private String googleId;
    private String mail;
    private String name;
    private String phone;
    private String photo;

    @JsonIgnore
    @Encrypted
    private String password;
    private boolean isAdmin;
}

package com.lingjun.springsecuritypractice.security.model;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String username;

    private String password;

    private String email;

    private List<String> roles;

}

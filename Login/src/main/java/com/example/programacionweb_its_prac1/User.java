package com.example.programacionweb_its_prac1;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String jwt;

    public User() {
    }

    public User(String fullName, String email, String username, String password) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Object[] getAll() {
        return new Object[] {
                this.fullName,
                this.email,
                this.username,
                this.password
        };
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("fullName", this.fullName);
        map.put("email", this.email);
        map.put("username", this.username);
        // No incluyas la contraseña por seguridad
        return map;
    }

}

package com.darioperez.biblioteca_api.dto;

import com.darioperez.biblioteca_api.model.Rol;

public class RegisterRequest {
    private String nombre;
    private String username;
    private String password;

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}




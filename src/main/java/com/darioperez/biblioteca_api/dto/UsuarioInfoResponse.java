package com.darioperez.biblioteca_api.dto;

import com.darioperez.biblioteca_api.model.Rol;

public class UsuarioInfoResponse {

    private String username;
    private Rol rol;

    public UsuarioInfoResponse(String username, Rol rol) {
        this.username = username;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public Rol getRol() {
        return rol;
    }
}


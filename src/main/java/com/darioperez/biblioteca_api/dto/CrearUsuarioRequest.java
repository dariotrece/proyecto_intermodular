package com.darioperez.biblioteca_api.dto;

public class CrearUsuarioRequest {

    private String nombre;
    private String username;
    private String password;
    private String rol;

    public CrearUsuarioRequest() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

}


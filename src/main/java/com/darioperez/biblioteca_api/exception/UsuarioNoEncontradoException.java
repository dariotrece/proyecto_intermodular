package com.darioperez.biblioteca_api.exception;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Integer id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UsuarioNoEncontradoException(String username) {
        super("Usuario no encontrado con username: " + username);
    }
}


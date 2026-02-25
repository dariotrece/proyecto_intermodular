package com.darioperez.biblioteca_api.exception;

public class UsuarioInvalidoException extends RuntimeException {
    public UsuarioInvalidoException(String message) {
        super(message);
    }
}

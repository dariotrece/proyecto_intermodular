package com.darioperez.biblioteca_api.exception;

public abstract class BibliotecaException extends RuntimeException {

    public BibliotecaException(String message) {
        super(message);
    }
}

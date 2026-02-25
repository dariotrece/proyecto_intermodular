package com.darioperez.biblioteca_api.exception;

public class LibroInvalidoException extends BibliotecaException {

    public LibroInvalidoException(String isbn) {
        super("Los datos del libro no son válidos");
    }
}

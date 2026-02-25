package com.darioperez.biblioteca_api.exception;

public class ISBNDuplicadoException extends BibliotecaException {

    public ISBNDuplicadoException(String isbn) {
        super("El ISBN " + isbn + " ya está registrado en el sistema");
    }
}

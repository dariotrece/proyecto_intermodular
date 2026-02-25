package com.darioperez.biblioteca_api.exception;

public class DevolucionInvalidaException extends BibliotecaException {

    public DevolucionInvalidaException(String isbn) {
        super("Este usuario no tiene prestado este libro");
    }
}

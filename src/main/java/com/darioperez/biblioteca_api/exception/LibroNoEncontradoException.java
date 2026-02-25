package com.darioperez.biblioteca_api.exception;

public class LibroNoEncontradoException extends BibliotecaException {

    public LibroNoEncontradoException(String isbn) {
        super("Libro con ISBN " + isbn + " no encontrado");
    }
}

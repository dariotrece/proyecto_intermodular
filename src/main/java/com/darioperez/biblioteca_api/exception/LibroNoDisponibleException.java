package com.darioperez.biblioteca_api.exception;

public class LibroNoDisponibleException extends BibliotecaException {

    public LibroNoDisponibleException(String titulo) {
        super("El libro '" + titulo + "' no está disponible");
    }
}

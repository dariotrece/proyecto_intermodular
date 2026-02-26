package com.darioperez.biblioteca_api.validation;

import com.darioperez.biblioteca_api.model.Libro;

public class LibroValidator {

    public static boolean isValid(Libro libro) {
        if (libro == null) {
            return false;
        }

        if (libro.getIsbn() == null) {
            return false;
        }

        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            return false;
        }

        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            return false;
        }

        return true;
    }
}

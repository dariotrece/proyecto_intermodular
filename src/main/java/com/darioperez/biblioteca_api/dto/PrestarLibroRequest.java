package com.darioperez.biblioteca_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PrestarLibroRequest {

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 10, max = 13, message = "El ISBN debe tener entre 10 y 13 caracteres")
    private String isbn;

    @Positive(message = "El ID de usuario debe ser un número positivo")
    private int usuarioId;

    public String getIsbn() {
        return isbn;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}

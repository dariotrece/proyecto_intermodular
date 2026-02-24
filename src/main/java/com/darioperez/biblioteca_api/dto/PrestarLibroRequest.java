package com.darioperez.biblioteca_api.dto;

public class PrestarLibroRequest {
    private String isbn;
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

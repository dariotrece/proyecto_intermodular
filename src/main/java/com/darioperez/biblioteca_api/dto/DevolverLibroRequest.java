package com.darioperez.biblioteca_api.dto;

public class DevolverLibroRequest {
    private String isbn;
    private int usuarioId;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }
}

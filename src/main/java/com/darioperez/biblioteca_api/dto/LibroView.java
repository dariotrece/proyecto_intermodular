package com.darioperez.biblioteca_api.dto;

public class LibroView {

    private String isbn;
    private String titulo;
    private String autor;
    private boolean disponible;

    public LibroView(String isbn, String titulo, String autor, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = disponible;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isDisponible() {
        return disponible;
    }
}

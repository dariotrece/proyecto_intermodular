package com.darioperez.biblioteca_api.dto;

public class CrearLibroRequest {  //Esqueleto que convierte el JSON en un objeto. No se usa libro para no perder seguridad y flexibilidad en caso de cambios
    private String isbn;
    private String titulo;
    private String autor;

    public CrearLibroRequest() {
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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
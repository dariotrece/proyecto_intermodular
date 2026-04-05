package com.darioperez.biblioteca_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CrearLibroRequest {//Esqueleto que convierte el JSON en un objeto. No se usa libro para no perder seguridad y flexibilidad en caso de cambios

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 10, max = 12, message = "EL ISBN debe tener entre 10 y 13 caracteres")
    @Pattern(regexp = "^[0-9-]+$", message = "El ISBN solo puede contener números y guiones")
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 2, message = "El título debe tener al menos 2 caracteres")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 2, message = "El autor debe tener al menos 2 caracteres")
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
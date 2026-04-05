package com.darioperez.biblioteca_api.dto;

import com.darioperez.biblioteca_api.model.Prestamo;

import java.time.LocalDateTime;

public class PrestamoView {

    private Long id;
    private String isbn;
    private String titulo;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private boolean activo;

    public PrestamoView(Prestamo p) {
        this.id = p.getId();
        this.isbn = p.getLibro().getIsbn();
        this.titulo = p.getLibro().getTitulo();
        this.fechaPrestamo = p.getFechaPrestamo();
        this.fechaDevolucion = p.getFechaDevolucion();
        this.activo = (p.getFechaDevolucion() == null);
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isActivo() {
        return activo;
    }
}
package com.darioperez.biblioteca_api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    private String titulo;

    private int puntuacion;

    private String comentario;

    @Enumerated(EnumType.STRING)
    private DueñoLibro dueñoLibro;

    private LocalDateTime fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public DueñoLibro getDueñoLibro() {
        return dueñoLibro;
    }

    public void setDueñoLibro(DueñoLibro dueñoLibro) {
        this.dueñoLibro = dueñoLibro;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

package com.darioperez.biblioteca_api.dto;

import com.darioperez.biblioteca_api.model.DueñoLibro;

public class CrearValoracionRequest {

    private String titulo;
    private int puntuacion;
    private String comentario;
    private DueñoLibro dueñoLibro;

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
}


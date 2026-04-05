package com.darioperez.biblioteca_api.dto;

import com.darioperez.biblioteca_api.model.DuenoLibro;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CrearValoracionRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Min(value =1, message = "La puntuación debe ser por lo menos de 1")
    @Max(value=5, message = "La puntución puede ser 5 como máximo")
    private int puntuacion;
    private String comentario;
    private DuenoLibro duenoLibro;

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

    public DuenoLibro getDueñoLibro() {
        return duenoLibro;
    }

    public void setDueñoLibro(DuenoLibro duenoLibro) {
        this.duenoLibro = duenoLibro;
    }
}


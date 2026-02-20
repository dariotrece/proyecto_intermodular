package com.darioperez.biblioteca_api.repository;

import com.darioperez.biblioteca_api.model.Libro;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BibliotecaRepository {
    List<Libro> cargarLibros();
    void guardarLibros(List<Libro> libros);
}

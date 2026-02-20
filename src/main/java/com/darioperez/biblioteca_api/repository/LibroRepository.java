package com.darioperez.biblioteca_api.repository;

import org.springframework.stereotype.Repository;
import com.darioperez.biblioteca_api.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

}


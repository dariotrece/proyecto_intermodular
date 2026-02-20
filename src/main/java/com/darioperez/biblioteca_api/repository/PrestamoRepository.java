package com.darioperez.biblioteca_api.repository;

import com.darioperez.biblioteca_api.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Optional<Prestamo> findByLibroIsbnAndFechaDevolucionIsNull(String isbn);

    List<Prestamo> findByUsuarioIdAndFechaDevolucionIsNull(Integer usuarioId);

    boolean existsByLibroIsbnAndUsuarioId(String isbn, Integer usuarioId);

    boolean existsByUsuarioIdAndLibroTituloIgnoreCase(Integer id, String titulo);
}


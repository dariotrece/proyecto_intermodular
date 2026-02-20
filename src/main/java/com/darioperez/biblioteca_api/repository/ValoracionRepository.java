package com.darioperez.biblioteca_api.repository;

import com.darioperez.biblioteca_api.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    Optional<Valoracion> findByUsuarioIdAndTituloIgnoreCase(Integer usuarioId, String titulo);

    List<Valoracion> findByUsuarioId(Integer usuarioId);

    void deleteByUsuarioIdAndTituloIgnoreCase(Integer usuarioId, String titulo);


}

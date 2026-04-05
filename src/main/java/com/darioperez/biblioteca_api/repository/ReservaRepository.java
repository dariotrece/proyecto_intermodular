package com.darioperez.biblioteca_api.repository;

import com.darioperez.biblioteca_api.model.EstadoReserva;
import com.darioperez.biblioteca_api.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findFirstByLibroIsbnAndEstadoOrderByFechaReservaAsc(String isbn, EstadoReserva estado);

    boolean existsByUsuarioIdAndLibroIsbnAndEstadoIn(Integer usuarioId, String isbn, List<EstadoReserva> estados);

    List<Reserva> findByUsuarioIdAndEstadoIn(Integer usuarioId, List<EstadoReserva> estados);

    List<Reserva> findAllByOrderByFechaReservaDesc();
}
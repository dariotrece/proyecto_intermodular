package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.exception.LibroNoEncontradoException;
import com.darioperez.biblioteca_api.exception.UsuarioNoEncontradoException;
import com.darioperez.biblioteca_api.model.EstadoReserva;
import com.darioperez.biblioteca_api.model.Libro;
import com.darioperez.biblioteca_api.model.Reserva;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.repository.LibroRepository;
import com.darioperez.biblioteca_api.repository.PrestamoRepository;
import com.darioperez.biblioteca_api.repository.ReservaRepository;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository,
                          LibroRepository libroRepository,
                          PrestamoRepository prestamoRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Reserva crearReserva(String username, String titulo) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);

        if (libros.isEmpty()) {
            throw new LibroNoEncontradoException(titulo);
        }

        Libro libro = libros.stream()
                .filter(l -> !prestamoRepository
                        .findByLibroIsbnAndFechaDevolucionIsNull(l.getIsbn())
                        .isEmpty())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No hay ejemplares prestados de ese libro, está disponible, cógelo directamente"));

        boolean yaReservado = reservaRepository.existsByUsuarioIdAndLibroIsbnAndEstadoIn(
                usuario.getId(), libro.getIsbn(), List.of(EstadoReserva.PENDIENTE, EstadoReserva.LISTA)
        );

        if (yaReservado) {
            throw new IllegalStateException("Ya tienes una reserva activa sobre este libro");
        }

        return reservaRepository.save(new Reserva(usuario, libro));
    }

    public List<Reserva> misReservas(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        return reservaRepository.findByUsuarioIdAndEstadoIn(
                usuario.getId(),
                List.of(EstadoReserva.PENDIENTE, EstadoReserva.LISTA)
        );
    }

    public void cancelarReserva(Long reservaId, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        if (!reserva.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalStateException("No puedes cancelar una reserva que no es tuya");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public List<Reserva> todasLasReservas() {
        return reservaRepository.findAllByOrderByFechaReservaDesc()
                .stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .toList();
    }

    public void notificarDevolucion(String isbn) {
        reservaRepository
                .findFirstByLibroIsbnAndEstadoOrderByFechaReservaAsc(isbn, EstadoReserva.PENDIENTE)
                .ifPresent(reserva -> {
                    reserva.setEstado(EstadoReserva.LISTA);
                    reservaRepository.save(reserva);
                });
    }

    public void cancelarReservaBibliotecario(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }
}
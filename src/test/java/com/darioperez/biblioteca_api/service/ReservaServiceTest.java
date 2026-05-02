package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.model.EstadoReserva;
import com.darioperez.biblioteca_api.model.Libro;
import com.darioperez.biblioteca_api.model.Prestamo;
import com.darioperez.biblioteca_api.model.Reserva;
import com.darioperez.biblioteca_api.model.Rol;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.repository.LibroRepository;
import com.darioperez.biblioteca_api.repository.PrestamoRepository;
import com.darioperez.biblioteca_api.repository.ReservaRepository;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Usuario usuario;
    private Usuario otroUsuario;
    private Libro libro;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Dario", "dario", "secret", Rol.USUARIO);
        setField(usuario, "id", 1);

        otroUsuario = new Usuario("Ana", "ana", "secret", Rol.USUARIO);
        setField(otroUsuario, "id", 2);

        libro = new Libro("1234567890", "Effective Java", "Joshua Bloch");
    }

    @Test
    void crearReservaCreatesPendingReservationForBorrowedBook() {
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(libroRepository.findByTituloContainingIgnoreCase("Effective Java")).thenReturn(List.of(libro));
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of(new Prestamo(libro, otroUsuario)));
        when(reservaRepository.existsByUsuarioIdAndLibroIsbnAndEstadoIn(any(), any(), any())).thenReturn(false);

        reservaService.crearReserva(usuario.getUsername(), "Effective Java");

        ArgumentCaptor<Reserva> reservaCaptor = ArgumentCaptor.forClass(Reserva.class);
        verify(reservaRepository).save(reservaCaptor.capture());

        Reserva savedReserva = reservaCaptor.getValue();
        assertEquals(usuario, savedReserva.getUsuario());
        assertEquals(libro, savedReserva.getLibro());
        assertEquals(EstadoReserva.PENDIENTE, savedReserva.getEstado());
    }

    @Test
    void crearReservaThrowsWhenUserAlreadyHasAnActiveReservation() {
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(libroRepository.findByTituloContainingIgnoreCase("Effective Java")).thenReturn(List.of(libro));
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of(new Prestamo(libro, otroUsuario)));
        when(reservaRepository.existsByUsuarioIdAndLibroIsbnAndEstadoIn(any(), any(), any())).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> reservaService.crearReserva(usuario.getUsername(), "Effective Java"));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void cancelarReservaThrowsWhenReservationDoesNotBelongToUser() {
        Reserva reserva = new Reserva(otroUsuario, libro);
        setField(reserva, "id", 10L);

        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(reservaRepository.findById(10L)).thenReturn(Optional.of(reserva));

        assertThrows(IllegalStateException.class,
                () -> reservaService.cancelarReserva(10L, usuario.getUsername()));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void notificarDevolucionMarksOldestPendingReservationAsReady() {
        Reserva reserva = new Reserva(usuario, libro);

        when(reservaRepository.findFirstByLibroIsbnAndEstadoOrderByFechaReservaAsc(
                libro.getIsbn(), EstadoReserva.PENDIENTE))
                .thenReturn(Optional.of(reserva));

        reservaService.notificarDevolucion(libro.getIsbn());

        assertEquals(EstadoReserva.LISTA, reserva.getEstado());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void cancelarReservaBibliotecarioCancelsReservation() {
        Reserva reserva = new Reserva(usuario, libro);
        setField(reserva, "id", 4L);

        when(reservaRepository.findById(4L)).thenReturn(Optional.of(reserva));

        reservaService.cancelarReservaBibliotecario(4L);

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void misReservasReturnsOnlyActiveReservationsFromRepository() {
        Reserva reserva = new Reserva(usuario, libro);

        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.of(usuario));
        when(reservaRepository.findByUsuarioIdAndEstadoIn(any(), any())).thenReturn(List.of(reserva));

        List<Reserva> result = reservaService.misReservas(usuario.getUsername());

        assertEquals(1, result.size());
        assertTrue(result.contains(reserva));
    }
}

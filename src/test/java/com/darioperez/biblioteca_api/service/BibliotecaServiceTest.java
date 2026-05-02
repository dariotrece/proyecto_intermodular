package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.dto.LibroView;
import com.darioperez.biblioteca_api.exception.DevolucionInvalidaException;
import com.darioperez.biblioteca_api.exception.ISBNDuplicadoException;
import com.darioperez.biblioteca_api.exception.LibroNoDisponibleException;
import com.darioperez.biblioteca_api.model.Libro;
import com.darioperez.biblioteca_api.model.Prestamo;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class BibliotecaServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private BibliotecaService bibliotecaService;

    private Libro libro;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        libro = new Libro("1234567890", "Clean Code", "Robert C. Martin");
        usuario = new Usuario("Dario", "dario", "secret", Rol.USUARIO);
        setField(usuario, "id", 7);
    }

    @Test
    void crearLibroThrowsWhenIsbnAlreadyExists() {
        when(libroRepository.existsById(libro.getIsbn())).thenReturn(true);

        assertThrows(ISBNDuplicadoException.class, () -> bibliotecaService.crearLibro(libro));

        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    void prestarLibroThrowsWhenBookIsAlreadyBorrowed() {
        Prestamo prestamoActivo = new Prestamo(libro, usuario);

        when(libroRepository.findById(libro.getIsbn())).thenReturn(Optional.of(libro));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of(prestamoActivo));

        assertThrows(LibroNoDisponibleException.class,
                () -> bibliotecaService.prestarLibro(libro.getIsbn(), usuario.getId()));

        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    @Test
    void prestarLibroCreatesLoanWhenBookIsAvailable() {
        when(libroRepository.findById(libro.getIsbn())).thenReturn(Optional.of(libro));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of());

        bibliotecaService.prestarLibro(libro.getIsbn(), usuario.getId());

        ArgumentCaptor<Prestamo> prestamoCaptor = ArgumentCaptor.forClass(Prestamo.class);
        verify(prestamoRepository).save(prestamoCaptor.capture());

        Prestamo savedPrestamo = prestamoCaptor.getValue();
        assertEquals(libro, savedPrestamo.getLibro());
        assertEquals(usuario, savedPrestamo.getUsuario());
        assertNotNull(savedPrestamo.getFechaPrestamo());
    }

    @Test
    void devolverLibroThrowsWhenThereIsNoActiveLoan() {
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of());

        assertThrows(DevolucionInvalidaException.class, () -> bibliotecaService.devolverLibro(libro.getIsbn()));

        verify(reservaService, never()).notificarDevolucion(any());
    }

    @Test
    void devolverLibroClosesLoanAndNotifiesReservations() {
        Prestamo prestamoActivo = new Prestamo(libro, usuario);

        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of(prestamoActivo));

        bibliotecaService.devolverLibro(libro.getIsbn());

        ArgumentCaptor<Prestamo> prestamoCaptor = ArgumentCaptor.forClass(Prestamo.class);
        verify(prestamoRepository).save(prestamoCaptor.capture());
        verify(reservaService).notificarDevolucion(libro.getIsbn());

        assertNotNull(prestamoCaptor.getValue().getFechaDevolucion());
    }

    @Test
    void buscarLibroReturnsAvailabilityFlag() {
        when(libroRepository.findById(libro.getIsbn())).thenReturn(Optional.of(libro));
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of());

        LibroView result = bibliotecaService.buscarLibro(libro.getIsbn());

        assertEquals(libro.getIsbn(), result.getIsbn());
        assertTrue(result.isDisponible());
    }

    @Test
    void listarLibrosConEstadoMarksBorrowedBooksAsUnavailable() {
        Prestamo prestamoActivo = new Prestamo(libro, usuario);
        prestamoActivo.setFechaPrestamo(LocalDateTime.now().minusDays(1));

        when(libroRepository.findAll()).thenReturn(List.of(libro));
        when(prestamoRepository.findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn()))
                .thenReturn(List.of(prestamoActivo));

        List<LibroView> result = bibliotecaService.listarLibrosConEstado();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isDisponible());
    }
}

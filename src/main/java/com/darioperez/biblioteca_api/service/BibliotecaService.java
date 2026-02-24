package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.exception.*;
import com.darioperez.biblioteca_api.model.Libro;
import com.darioperez.biblioteca_api.dto.LibroView;
import com.darioperez.biblioteca_api.model.Prestamo;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.repository.LibroRepository;
import com.darioperez.biblioteca_api.repository.PrestamoRepository;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BibliotecaService {

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public BibliotecaService(LibroRepository libroRepository, UsuarioRepository usuarioRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    //Crear libro en el catálogo comprobando que no exista previamente. Usa métodos Spring Boot
    public Libro crearLibro(Libro libro) {
        if (libroRepository.existsById(libro.getIsbn())) {
            throw new ISBNDuplicadoException(libro.getIsbn());
        }
        return libroRepository.save(libro);
    }

    //Segunda forma que permite la creación de libro. Llama met0do anterior
    public Libro crearLibro(String isbn, String titulo, String autor) {
        return crearLibro(new Libro(isbn, titulo, autor));
    }

    //Mét0d0 prestar libro. Usa ISBN e id de usuario y si no existe tira excepción. Para saber si esta prestado busca un libro con ese ISBN sin fecha de devolución.
    public void prestarLibro(String isbn, Integer usuarioId) {

        Libro libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        boolean prestado = prestamoRepository
                .findByLibroIsbnAndFechaDevolucionIsNull(isbn)
                .isPresent();

        if (prestado) {
            throw new LibroNoDisponibleException(isbn);
        }

        Prestamo prestamo = new Prestamo(libro, usuario);
        prestamoRepository.save(prestamo);
    }

    //Mét0do devolver libro, busca el préstamo activo de ese lubro, si no lo encuentra salta excepción, si lo encuentra le pone fecha de devolución y lo marca como disponible
    public void devolverLibro(String isbn) {

        Prestamo prestamo = prestamoRepository
                .findByLibroIsbnAndFechaDevolucionIsNull(isbn)
                .orElseThrow(() -> new DevolucionInvalidaException(isbn));

        prestamo.setFechaDevolucion(LocalDateTime.now());
        prestamoRepository.save(prestamo);
    }

    //Devuelve todos los libros
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    //
    public Libro prestarLibroUsuario(String isbn, Integer usuarioId) {

        prestarLibro(isbn, usuarioId);
        return libroRepository.findById(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));
    }

    public Libro devolverLibroUsuario(String isbn, Integer usuarioId) {
        devolverLibro(isbn);
        return libroRepository.findById(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));
    }

    public LibroView buscarLibro(String isbn) {
        Libro libro = libroRepository.findById(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException(isbn));

        boolean prestado = prestamoRepository
                .findByLibroIsbnAndFechaDevolucionIsNull(isbn)
                .isPresent();
        return new LibroView(
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAutor(),
                !prestado
        );
    }
    public List<LibroView> buscarLibroTitulo(String titulo) {

        List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);

        if (libros.isEmpty()) {
            throw new LibroNoEncontradoException(titulo);
        }

        List<LibroView> resultado = new ArrayList<>();

        for (Libro libro : libros) {

            boolean prestado = prestamoRepository
                    .findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn())
                    .isPresent();

            resultado.add(new LibroView(
                    libro.getIsbn(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    !prestado
            ));
        }

        return resultado;
    }



    public List<LibroView> listarLibrosConEstado() {

        List<Libro> libros = libroRepository.findAll();
        List<LibroView> resultado = new ArrayList<>();

        for (Libro libro : libros) {
            boolean prestado = prestamoRepository
                    .findByLibroIsbnAndFechaDevolucionIsNull(libro.getIsbn())
                    .isPresent();

            resultado.add(new LibroView(
                    libro.getIsbn(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    !prestado
            ));
        }

        return resultado;
    }



}


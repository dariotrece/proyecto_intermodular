package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.ApiResponse;
import com.darioperez.biblioteca_api.dto.CrearLibroRequest;
import com.darioperez.biblioteca_api.dto.DevolverLibroRequest;
import com.darioperez.biblioteca_api.dto.PrestarLibroRequest;
import com.darioperez.biblioteca_api.model.Libro;
import com.darioperez.biblioteca_api.service.BibliotecaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/libros") //Endpoint de lo relacionado con los libros
public class LibroController {

    private final BibliotecaService bibliotecaService;

    public LibroController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> crearLibro(@Valid @RequestBody CrearLibroRequest request) {

        Libro libro = bibliotecaService.crearLibro(
                request.getIsbn(),
                request.getTitulo(),
                request.getAutor()
        );

        if (libro == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "No se pudo crear el libro (datos inválidos o ISBN duplicado)"));
        }

        return ResponseEntity.status(201).body(libro);
    }

    @PostMapping("/devoluciones")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> devolverLibro(@Valid @RequestBody DevolverLibroRequest request) {

        Libro libro = bibliotecaService.devolverLibroUsuario(
                request.getIsbn(),
                request.getUsuarioId()
        );

        if (libro == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "No se pudo devolver el libro (datos incorrectos o estado inválido)"));
        }

        return ResponseEntity.ok(new ApiResponse(true,
                "Libro '" + libro.getTitulo() +
                        "' devuelto correctamente"
        ));
    }

    @PostMapping("/prestamos")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> prestarLibro(@Valid @RequestBody PrestarLibroRequest request) {

        Libro libro = bibliotecaService.prestarLibroUsuario(
                request.getIsbn(),
                request.getUsuarioId()
        );

        if (libro == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false,"No se pudo prestar el libro (datos incorrectos o estado inválido)"));
        }

        return ResponseEntity.ok(new ApiResponse(true,
                "Libro '" + libro.getTitulo() +
                        "' prestado correctamente"
        ));
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<?> buscarLibroTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(bibliotecaService.buscarLibroTitulo(titulo));
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> buscarLibro(@PathVariable String isbn) {
        return ResponseEntity.ok(bibliotecaService.buscarLibro(isbn));
    }

    @GetMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> listarLibros() {
        return ResponseEntity.ok(
                bibliotecaService.listarLibrosConEstado()
        );
    }



}

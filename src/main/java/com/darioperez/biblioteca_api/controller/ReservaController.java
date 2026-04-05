package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.model.Reserva;
import com.darioperez.biblioteca_api.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<?> crearReserva(
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        String titulo = body.get("titulo");
        Reserva reserva = reservaService.crearReserva(authentication.getName(), titulo);
        return ResponseEntity.status(201).body(reserva);
    }

    @GetMapping("/mias")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<List<Reserva>> misReservas(Authentication authentication) {
        return ResponseEntity.ok(reservaService.misReservas(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<?> cancelarReserva(
            @PathVariable Long id,
            Authentication authentication
    ) {
        reservaService.cancelarReserva(id, authentication.getName());
        return ResponseEntity.ok("Reserva cancelada correctamente");
    }

    @GetMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<List<Reserva>> todasLasReservas() {
        return ResponseEntity.ok(reservaService.todasLasReservas());
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> cancelarReservaBibliotecario(@PathVariable Long id) {
        reservaService.cancelarReservaBibliotecario(id);
        return ResponseEntity.ok("Reserva cancelada correctamente");
    }
}
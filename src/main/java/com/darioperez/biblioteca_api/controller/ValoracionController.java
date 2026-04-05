package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.CrearValoracionRequest;
import com.darioperez.biblioteca_api.model.Valoracion;
import com.darioperez.biblioteca_api.service.ValoracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/valoraciones")
public class ValoracionController {

    private final ValoracionService valoracionService;

    public ValoracionController(ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @PostMapping
    public ResponseEntity<?> crearValoracion(
            @RequestBody CrearValoracionRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();

        Valoracion valoracion = valoracionService.crearOActualizarValoracion(
                username,
                request.getTitulo(),
                request.getPuntuacion(),
                request.getComentario(),
                request.getDueñoLibro()
        );

        return ResponseEntity.ok(valoracion);
    }

    @GetMapping("/mias")
    public ResponseEntity<List<Valoracion>> misValoraciones(Authentication authentication) {

        String username = authentication.getName();

        List<Valoracion> valoraciones =
                valoracionService.obtenerValoracionesDeUsuario(username);

        return ResponseEntity.ok(valoraciones);
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<Valoracion>> verValoracionesDeOtroUsuario(
            @PathVariable String username
    ) {
        List<Valoracion> valoraciones =
                valoracionService.obtenerValoracionesDeUsuario(username);
        return ResponseEntity.ok(valoraciones);
    }

    @DeleteMapping
    public ResponseEntity<?> borrarValoracion(
            @RequestParam String titulo,
            Authentication authentication
    ) {

        String username = authentication.getName();

        valoracionService.borrarValoracion(username, titulo);

        return ResponseEntity.ok("Valoración eliminada correctamente");
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Map<String, Object>>> obtenerRanking() {
        return ResponseEntity.ok(valoracionService.obtenerRankingLibros());
    }

}

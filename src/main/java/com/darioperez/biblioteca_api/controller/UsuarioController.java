package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.CrearUsuarioRequest;
import com.darioperez.biblioteca_api.dto.LoginRequest;
import com.darioperez.biblioteca_api.model.Rol;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/usuarios") //Endpoint de usuarios, desde su creación, búsqueda y listado
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody CrearUsuarioRequest request) {

        Usuario usuario = usuarioService.crearUsuario(
                request.getNombre(),
                request.getUsername(),
                request.getPassword(),
                Rol.valueOf(request.getRol().toUpperCase())
        );
        return ResponseEntity.status(201).body(usuario);
    }


    @GetMapping
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
}


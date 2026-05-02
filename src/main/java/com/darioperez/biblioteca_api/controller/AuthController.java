package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.LoginRequest;
import com.darioperez.biblioteca_api.dto.LoginResponse;
import com.darioperez.biblioteca_api.dto.RegisterRequest;
import com.darioperez.biblioteca_api.dto.UsuarioInfoResponse;
import com.darioperez.biblioteca_api.model.Rol;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") //endpoint de autenticación
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register") //Endpoint para registrar usuario
    public ResponseEntity<Usuario> register(@RequestBody RegisterRequest request) {
        Usuario usuario = usuarioService.crearUsuario(
                request.getNombre(),
                request.getUsername(),
                request.getPassword(),
                Rol.USUARIO // rol por defecto, en caso de que alguien sea admin se cambia desde bbdd
        );
        return ResponseEntity.status(201).body(usuario);
    }

    @PostMapping("/login") //Endpoint de logeo, valida usuario y contraseña
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                new LoginResponse(usuario.getUsername(), usuario.getRol())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();

        String rol = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .findFirst()
                .orElse("USUARIO");

        return ResponseEntity.ok(
                new UsuarioInfoResponse(username, Rol.valueOf(rol))
        );
    }



}


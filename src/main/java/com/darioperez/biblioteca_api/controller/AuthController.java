package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.LoginRequest;
import com.darioperez.biblioteca_api.dto.LoginResponse;
import com.darioperez.biblioteca_api.dto.RegisterRequest;
import com.darioperez.biblioteca_api.dto.UsuarioInfoResponse;
import com.darioperez.biblioteca_api.model.Rol;
import com.darioperez.biblioteca_api.model.Usuario;
//import com.darioperez.biblioteca_api.security.JwtService;
import com.darioperez.biblioteca_api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") //endpoint de autenticación
public class AuthController {

    private final UsuarioService usuarioService;
    //private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        //this.jwtService = jwtService;
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
        
        //String token = jwtService.generarToken(usuario); //En caso de login correcto, te genera un token que te identifica para tu uso de la app
        return ResponseEntity.ok(
                new LoginResponse(usuario.getUsername(), usuario.getRol())
        );
    }

    @GetMapping("/me")  //endpoint para poder mostrar/ocultar lo que tenga que ver cada usuario de la aplicación
    public ResponseEntity<?> me(Authentication authentication)  {//spring inyecta automaticamente

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();

        Usuario usuario = usuarioService.buscarPorUsername(username);

        return ResponseEntity.ok(
                new UsuarioInfoResponse(
                        usuario.getUsername(),
                        usuario.getRol()
                )
        );
    }



}


package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.exception.UsuarioInvalidoException;
import com.darioperez.biblioteca_api.exception.UsuarioNoEncontradoException;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import com.darioperez.biblioteca_api.model.Rol;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
public class UsuarioService implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Usuario crearUsuario(String nombre, String username, String password, Rol rol) {

        if (nombre == null || nombre.isBlank()
                || username == null || username.isBlank()
                || password == null || password.isBlank()
                || rol == null) {
            throw new UsuarioInvalidoException("Datos de usuario inválidos");
        }
        String passwordHash = passwordEncoder.encode(password);
        Usuario usuario = new Usuario(
                nombre,
                username,
                passwordHash,
                rol
        );

        return usuarioRepository.save(usuario);
    }


    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));
    }

    public Usuario login(String username, String password) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }


}

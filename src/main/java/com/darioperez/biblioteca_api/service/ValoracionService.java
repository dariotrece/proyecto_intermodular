package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.model.DueñoLibro;
import com.darioperez.biblioteca_api.model.Usuario;
import com.darioperez.biblioteca_api.model.Valoracion;
import com.darioperez.biblioteca_api.repository.PrestamoRepository;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import com.darioperez.biblioteca_api.repository.ValoracionRepository;
import com.darioperez.biblioteca_api.exception.UsuarioNoEncontradoException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ValoracionService {

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final ValoracionRepository valoracionRepository;

    public ValoracionService(UsuarioRepository usuarioRepository,
                             PrestamoRepository prestamoRepository,
                             ValoracionRepository valoracionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
        this.valoracionRepository = valoracionRepository;
    }

    public Valoracion crearOActualizarValoracion(
            String username,
            String titulo,
            int puntuacion,
            String comentario,
            DueñoLibro dueñoLibro
    ) {

        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        Optional<Valoracion> existente =
                valoracionRepository.findByUsuarioIdAndTituloIgnoreCase(
                        usuario.getId(), titulo);

        boolean leidoEnBiblioteca =
                prestamoRepository.existsByUsuarioIdAndLibroTituloIgnoreCase(
                        usuario.getId(), titulo);

        DueñoLibro dueñoFinal = leidoEnBiblioteca
                ? DueñoLibro.BIBLIOTECA
                : DueñoLibro.PRIVADO;

        Valoracion valoracion;

        if (existente.isPresent()) {
            valoracion = existente.get();
            valoracion.setPuntuacion(puntuacion);
            valoracion.setComentario(comentario);
            valoracion.setDueñoLibro(dueñoFinal);
            valoracion.setFecha(LocalDateTime.now());

        } else {
            valoracion = new Valoracion();
            valoracion.setUsuario(usuario);
            valoracion.setTitulo(titulo);
            valoracion.setPuntuacion(puntuacion);
            valoracion.setComentario(comentario);
            valoracion.setDueñoLibro(dueñoFinal);
            valoracion.setFecha(LocalDateTime.now());
        }
        return valoracionRepository.save(valoracion);

    }

    public List<Valoracion> obtenerValoracionesDeUsuario(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        return valoracionRepository.findByUsuarioId(usuario.getId());
    }

    public void borrarValoracion(String username, String titulo) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        valoracionRepository
                .deleteByUsuarioIdAndTituloIgnoreCase(usuario.getId(), titulo);
    }

}

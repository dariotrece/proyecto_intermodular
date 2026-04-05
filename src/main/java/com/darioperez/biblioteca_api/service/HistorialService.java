package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.dto.PrestamoView;
import com.darioperez.biblioteca_api.exception.UsuarioNoEncontradoException;
import com.darioperez.biblioteca_api.repository.PrestamoRepository;
import com.darioperez.biblioteca_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistorialService {

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public HistorialService(UsuarioRepository usuarioRepository, PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Page<PrestamoView> misPrestamos(String username, Boolean soloActivos, Pageable pageable) {
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException(username));

        var page = (soloActivos != null && soloActivos)
                ? prestamoRepository.findByUsuarioIdAndFechaDevolucionIsNullOrderByFechaPrestamoDesc(usuario.getId(), pageable)
                : prestamoRepository.findByUsuarioIdOrderByFechaPrestamoDesc(usuario.getId(), pageable);

        return page.map(PrestamoView::new);
    }
}
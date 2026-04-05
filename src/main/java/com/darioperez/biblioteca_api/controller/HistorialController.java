package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.PrestamoView;
import com.darioperez.biblioteca_api.service.HistorialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/historial")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/mio")
    public Page<PrestamoView> mio(
            Principal principal,
            @RequestParam(required = false) Boolean activos,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return historialService.misPrestamos(principal.getName(), activos, pageable);
    }
}
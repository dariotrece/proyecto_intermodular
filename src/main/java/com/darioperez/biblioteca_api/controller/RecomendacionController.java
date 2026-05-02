package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.service.RecomendacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(RecomendacionService recomendacionService) {
        this.recomendacionService = recomendacionService;
    }

    @GetMapping("/mia")
    public Map<String, String> miRecomendacion(Principal principal) {
        String recomendacion = recomendacionService.recomendar(principal.getName());
        return Map.of("recomendacion", recomendacion);
    }
}
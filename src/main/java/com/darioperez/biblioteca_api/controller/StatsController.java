package com.darioperez.biblioteca_api.controller;

import com.darioperez.biblioteca_api.dto.TituloRatingStats;
import com.darioperez.biblioteca_api.service.ValoracionStatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final ValoracionStatsService statsService;

    public StatsController(ValoracionStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/top-titulos")
    public List<TituloRatingStats> topTitulos(@RequestParam(defaultValue = "10") int limit) {
        return statsService.topTitulos(limit);
    }
}
package com.darioperez.biblioteca_api.service;

import com.darioperez.biblioteca_api.dto.TituloRatingStats;
import com.darioperez.biblioteca_api.repository.ValoracionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoracionStatsService {

    private final ValoracionRepository valoracionRepository;

    public ValoracionStatsService(ValoracionRepository valoracionRepository) {
        this.valoracionRepository = valoracionRepository;
    }

    public List<TituloRatingStats> topTitulos(int limit) {
        return valoracionRepository.topTitulos(PageRequest.of(0, Math.max(1, limit)));
    }
}

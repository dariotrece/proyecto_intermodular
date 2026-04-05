package com.darioperez.biblioteca_api.repository;

import com.darioperez.biblioteca_api.dto.TituloRatingStats;
import com.darioperez.biblioteca_api.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    Optional<Valoracion> findByUsuarioIdAndTituloIgnoreCase(Integer usuarioId, String titulo);

    List<Valoracion> findByUsuarioId(Integer usuarioId);

    public void deleteByUsuarioIdAndTituloIgnoreCase(Integer usuarioId, String titulo);

    @Query("SELECT v.titulo, AVG(v.puntuacion) as media, COUNT(v) as total " +
            "FROM Valoracion v WHERE v.duenoLibro = 'BIBLIOTECA' " +
            "GROUP BY v.titulo " +
            "HAVING COUNT(v) >= 2 " +
            "ORDER BY media DESC")    List<Object[]> findTopRatedBooks();


    @Query("""
           select v.titulo as titulo,
                  avg(v.puntuacion) as media,
                  count(v) as total
           from Valoracion v
           group by v.titulo
           order by avg(v.puntuacion) desc, count(v) desc
           """)
    List<TituloRatingStats> topTitulos(Pageable pageable);
}



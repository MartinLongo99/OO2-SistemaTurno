package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.oo2.grupo15.entities.Turno;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    // Consulta simplificada que solo retorna datos básicos
    @Query("SELECT t.id, t.fechaHora, t.estado FROM Turno t WHERE t.fechaHora >= :fechaInicio AND t.fechaHora <= :fechaFin ORDER BY t.fechaHora ASC")
    List<Object[]> buscarTurnosSimplificados(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
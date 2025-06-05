package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oo2.grupo15.entities.Turno;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
    // Método para buscar turnos entre dos fechas
    List<Turno> findByFechaHoraBetweenOrderByFechaHoraAsc(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
package com.oo2.grupo15.services;

import java.time.LocalDateTime;
import java.util.List;
import com.oo2.grupo15.dtos.TurnoDTO;

public interface ITurnoService {
    TurnoDTO crearTurno(TurnoDTO dto);
    TurnoDTO actualizarTurno(Long id, TurnoDTO dto);
    void eliminarTurno(Long id);
    List<TurnoDTO> obtenerTodos();
    TurnoDTO obtenerPorId(Long id);

    List<TurnoDTO> obtenerTurnosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
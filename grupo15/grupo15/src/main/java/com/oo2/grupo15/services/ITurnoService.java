package com.oo2.grupo15.services;

import java.util.List;
import com.oo2.grupo15.entities.Turno;
import com.oo2.grupo15.dtos.TurnoDTO;

public interface ITurnoService {
    Turno crearTurno(TurnoDTO dto);
    Turno actualizarTurno(Long id, TurnoDTO dto);
    void eliminarTurno(Long id);
    List<Turno> obtenerTodos();
    Turno obtenerPorId(Long id);
}

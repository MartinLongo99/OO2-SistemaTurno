package com.oo2.grupo15.services;

import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.entities.Profesional;

import java.util.List;

public interface IProfesionalService {
    List<ProfesionalDTO> buscarPorMatricula(String matricula);
    List<ProfesionalDTO> obtenerTodos();
    Profesional findEntityById(Long id);
}

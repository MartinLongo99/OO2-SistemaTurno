package com.oo2.grupo15.services;

import com.oo2.grupo15.dtos.ProfesionalDTO;

import java.util.List;

public interface IProfesionalService {
    List<ProfesionalDTO> buscarPorMatricula(String matricula);
    List<ProfesionalDTO> obtenerTodos();
}

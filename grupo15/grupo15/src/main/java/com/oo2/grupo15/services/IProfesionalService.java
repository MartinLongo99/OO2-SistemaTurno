
package com.oo2.grupo15.services;

import com.oo2.grupo15.entities.Profesional;

import java.util.List;

public interface IProfesionalService {
    List<Profesional> findByMatricula(String matricula);
}

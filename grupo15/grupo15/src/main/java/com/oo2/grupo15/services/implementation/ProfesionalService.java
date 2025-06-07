
package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.entities.Profesional;
import com.oo2.grupo15.repositories.IProfesionalRepository;
import com.oo2.grupo15.services.IProfesionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesionalService implements IProfesionalService {

    @Autowired
    private IProfesionalRepository profesionalRepository;

    @Override
    public List<Profesional> findByMatricula(String matricula) {
        return profesionalRepository.findByMatricula(matricula);
    }
}

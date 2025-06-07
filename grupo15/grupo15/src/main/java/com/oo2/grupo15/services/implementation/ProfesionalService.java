package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.entities.Profesional;
import com.oo2.grupo15.repositories.IProfesionalRepository;
import com.oo2.grupo15.services.IProfesionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesionalService implements IProfesionalService {

    @Autowired
    private IProfesionalRepository profesionalRepository;

    @Override
    public List<Profesional> findByMatricula(String matricula) {
        return profesionalRepository.findByMatricula(matricula);
    }

    @Override
    public List<ProfesionalDTO> findDTOsByMatricula(String matricula) {
        return profesionalRepository.findByMatricula(matricula).stream()
            .map(p -> new ProfesionalDTO(
                p.getId(),
                p.getEmail(),
                p.getContacto().getNombre(),
                p.getContacto().getApellido(),
                p.getMatricula(),
                p.getEspecialidades().stream()
                    .map(e -> e.getNombreEspecialidad())
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }
}

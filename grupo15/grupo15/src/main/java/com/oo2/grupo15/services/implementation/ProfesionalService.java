package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.ContactoDTO;
import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.entities.Contacto;
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
    public List<ProfesionalDTO> buscarPorMatricula(String matricula) {
        return profesionalRepository.findByMatricula(matricula).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProfesionalDTO> obtenerTodos() {
        return profesionalRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Profesional findEntityById(Long id) {
        return profesionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado con ID: " + id));
    }

    private ProfesionalDTO convertToDTO(Profesional profesional) {
        Contacto contacto = profesional.getContacto();
        ContactoDTO contactoDTO = new ContactoDTO();

        if (contacto != null) {
            contactoDTO.setNombre(contacto.getNombre());
            contactoDTO.setApellido(contacto.getApellido());
            contactoDTO.setDni(contacto.getDni());

            if (contacto.getDireccion() != null) {
                contactoDTO.setCalleYAltura(contacto.getDireccion().getCalleYAltura());

                if (contacto.getDireccion().getLocalidad() != null) {
                    contactoDTO.setLocalidad(contacto.getDireccion().getLocalidad().getNombre());

                    if (contacto.getDireccion().getLocalidad().getProvincia() != null) {
                        contactoDTO.setProvincia(contacto.getDireccion().getLocalidad().getProvincia().getNombre());
                    }
                }
            }
        }

        List<String> especialidades = profesional.getEspecialidades().stream()
            .map(e -> e.getNombreEspecialidad())
            .collect(Collectors.toList());

        return new ProfesionalDTO(
            profesional.getId(),
            profesional.getEmail(),
            profesional.getMatricula(),
            contactoDTO,
            especialidades
        );
    }
}

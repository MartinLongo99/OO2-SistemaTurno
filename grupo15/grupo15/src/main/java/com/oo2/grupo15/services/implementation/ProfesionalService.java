package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.ContactoDTO;
import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.entities.Especialidad;
import com.oo2.grupo15.entities.Profesional;
import com.oo2.grupo15.repositories.IEspecialidadRepository;
import com.oo2.grupo15.repositories.IProfesionalRepository;
import com.oo2.grupo15.services.IProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfesionalService implements IProfesionalService {

    @Autowired
    private IProfesionalRepository profesionalRepository;

    @Autowired
    private IEspecialidadRepository especialidadRepository;

    @Override
    public List<ProfesionalDTO> buscarPorMatricula(String matricula) {
        return profesionalRepository.findByMatriculaConEspecialidades(matricula)
                .map(this::convertToDTO)
                .stream()
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

    @Override
    public void guardar(ProfesionalDTO dto) {
        Profesional profesional = new Profesional();
        profesional.setId(dto.id()); 
        profesional.setEmail(dto.email()); 
        profesional.setMatricula(dto.matricula()); 

        Set<Especialidad> especialidades = new HashSet<>();
        if (dto.especialidadesIds() != null) { 
            especialidades = dto.especialidadesIds().stream()
                    .map(id -> especialidadRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id)))
                    .collect(Collectors.toSet());
        }

        profesional.setEspecialidades(especialidades);

        profesionalRepository.save(profesional);
    }

    private ProfesionalDTO convertToDTO(Profesional profesional) {
        ContactoDTO contactoDTO = null;
        Contacto contacto = profesional.getContacto();

        if (contacto != null) {
            String calleYAltura = null;
            String localidad = null;
            String provincia = null;

            if (contacto.getDireccion() != null) {
                calleYAltura = contacto.getDireccion().getCalleYAltura();

                if (contacto.getDireccion().getLocalidad() != null) {
                    localidad = contacto.getDireccion().getLocalidad().getNombre();

                    if (contacto.getDireccion().getLocalidad().getProvincia() != null) {
                        provincia = contacto.getDireccion().getLocalidad().getProvincia().getNombre();
                    }
                }
            }
         
            contactoDTO = new ContactoDTO(
                contacto.getNombre(),
                contacto.getApellido(),
                contacto.getDni(),
                calleYAltura,
                localidad,
                provincia
            );
        }

        // Convertir especialidades a Nombres
        List<String> especialidadesNombres = profesional.getEspecialidades().stream()
                .map(Especialidad::getNombreEspecialidad)
                .collect(Collectors.toList());

        return new ProfesionalDTO(
            profesional.getId(),
            profesional.getEmail(),
            profesional.getMatricula(),
            contactoDTO,
            especialidadesNombres,
            null
        );
    }
}
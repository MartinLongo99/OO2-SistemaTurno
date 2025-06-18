package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.ContactoDTO;
import com.oo2.grupo15.dtos.DireccionDTO;
import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.dtos.ProvinciaDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.entities.Direccion;
import com.oo2.grupo15.entities.Especialidad;
import com.oo2.grupo15.entities.Localidad;
import com.oo2.grupo15.entities.Profesional;
import com.oo2.grupo15.entities.Provincia;
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
        profesional.setId(dto.getId());
        profesional.setEmail(dto.getEmail());
        profesional.setMatricula(dto.getMatricula());

        // Mapear especialidades desde IDs (para guardar)
        Set<Especialidad> especialidades = new HashSet<>();
        if (dto.getEspecialidadesIds() != null) {
            especialidades = dto.getEspecialidadesIds().stream()
                    .map(id -> especialidadRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id)))
                    .collect(Collectors.toSet());
        }

        profesional.setEspecialidades(especialidades);

        profesionalRepository.save(profesional);
    }

    private ProfesionalDTO convertToDTO(Profesional profesional) {
        // Convertir contacto
        Contacto contacto = profesional.getContacto();
        ContactoDTO contactoDTO = new ContactoDTO();

        if (contacto != null) {
            contactoDTO.setNombre(contacto.getNombre());
            contactoDTO.setApellido(contacto.getApellido());
            contactoDTO.setDni(contacto.getDni());
            contactoDTO.setTelefono(contacto.getTelefono());

            Direccion direccion = contacto.getDireccion();
            if (direccion != null) {
                DireccionDTO direccionDTO = new DireccionDTO();
                direccionDTO.setId(direccion.getId());
                direccionDTO.setCalleYAltura(direccion.getCalleYAltura());

                Localidad localidad = direccion.getLocalidad();
                if (localidad != null) {
                    LocalidadDTO localidadDTO = new LocalidadDTO();
                    localidadDTO.setId(localidad.getId());
                    localidadDTO.setNombre(localidad.getNombre());

                    Provincia provincia = localidad.getProvincia();
                    if (provincia != null) {
                        ProvinciaDTO provinciaDTO = new ProvinciaDTO();
                        provinciaDTO.setId(provincia.getId());
                        provinciaDTO.setNombre(provincia.getNombre());
                        localidadDTO.setProvincia(provinciaDTO);
                    }

                    direccionDTO.setLocalidad(localidadDTO);
                }

                contactoDTO.setDireccion(direccionDTO);
            }
        }


        // Convertir especialidades a NOMBRES
        List<String> especialidadesNombres = profesional.getEspecialidades().stream()
                .map(Especialidad::getNombreEspecialidad)
                .collect(Collectors.toList());

        // Construir DTO final
        return ProfesionalDTO.builder()
                .id(profesional.getId())
                .email(profesional.getEmail())
                .matricula(profesional.getMatricula())
                .contacto(contactoDTO)
                .especialidades(especialidadesNombres) // ✅ usamos nombres
                .build();
    }
    
}

package com.oo2.grupo15.services.implementation;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.dtos.ServicioLugarDTO;
import com.oo2.grupo15.entities.*;
import com.oo2.grupo15.repositories.*;
import com.oo2.grupo15.services.*;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private IServicioRepository servicioRepository;

    @Autowired
    private IServicioLugarRepository servicioLugarRepository;

    @Autowired
    private ILugarService lugarService;

    @Autowired
    private IProfesionalService profesionalService;

    private final ModelMapper modelMapper = new ModelMapper();

    public ServicioService(IServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<ServicioDTO> findAll() {
        return servicioRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ServicioDTO> getAll() {
        return servicioRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ServicioDTO> findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ServicioDTO> findByEstado(Boolean estado) {
        return servicioRepository.findByEstado(estado != null ? estado : false).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ServicioDTO> findByDuracionMinutos(Integer duracion) {
        return servicioRepository.findByDuracionMinutos(duracion).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ServicioDTO findById(Long id) {
        return servicioRepository.findById(id)
            .map(this::convertToDTO)
            .orElse(null);
    }

    @Override
    public ServicioDTO save(ServicioDTO dto) {
        Servicio servicio = convertToEntity(dto);
        Servicio savedServicio = servicioRepository.save(servicio);

        // Guardar relaciones ServicioLugar
        guardarServicioLugares(savedServicio, dto.getLugarIds(), dto.getProfesionalIds());

        return convertToDTO(savedServicio);
    }

    @Override
    public ServicioDTO update(Long id, ServicioDTO dto) {
        if (servicioRepository.existsById(id)) {
            Servicio servicio = convertToEntity(dto);
            servicio.setId(id);
            Servicio updatedServicio = servicioRepository.save(servicio);

            // Actualizar relaciones ServicioLugar
            servicioLugarRepository.deleteByServicioId(id);
            guardarServicioLugares(updatedServicio, dto.getLugarIds(), dto.getProfesionalIds());

            return convertToDTO(updatedServicio);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        servicioLugarRepository.deleteByServicioId(id); // Eliminar relaciones primero
        servicioRepository.deleteById(id);
        return true;
    }

    @Override
    public Servicio findEntityById(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

    // ------------------ Helpers ------------------

    private void guardarServicioLugares(Servicio servicio, List<Long> lugarIds, List<Long> profesionalIds) {
        if (lugarIds == null || profesionalIds == null) return;

        for (Long lugarId : lugarIds) {
            Lugar lugar = lugarService.findEntityById(lugarId);
            for (Long profesionalId : profesionalIds) {
                Profesional profesional = profesionalService.findEntityById(profesionalId);
                ServicioLugar sl = new ServicioLugar();
                sl.setServicio(servicio);
                sl.setLugar(lugar);
                sl.setProfesional(profesional);
                sl.setActivo(true); // o dto.getActivo() si se pasa desde el form
                servicioLugarRepository.save(sl);
            }
        }
    }

    private ServicioDTO convertToDTO(Servicio servicio) {
        ServicioDTO dto = ServicioDTO.builder()
            .id(servicio.getId())
            .nombre(servicio.getNombre())
            .duracionMinutos(servicio.getDuracionMinutos())
            .estado(servicio.isEstado())
            .horarioInicio(servicio.getHorarioInicio() != null ? servicio.getHorarioInicio().toString() : null)
            .horarioFin(servicio.getHorarioFin() != null ? servicio.getHorarioFin().toString() : null)
            .diasSemana(servicio.getDiasSemana())
            .build();

        if (servicio.getServicioLugares() != null) {
            Set<ServicioLugarDTO> lugaresDTO = servicio.getServicioLugares().stream()
                .map(sl -> ServicioLugarDTO.builder()
                    .id(sl.getId())
                    .activo(sl.isActivo())
                    .build())
                .collect(Collectors.toSet());
            dto.setServicioLugares(lugaresDTO);
        }

        return dto;
    }

    private Servicio convertToEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setId(dto.getId());
        servicio.setNombre(dto.getNombre());
        servicio.setEstado(dto.isEstado());
        servicio.setDuracionMinutos(dto.getDuracionMinutos());

        if (dto.getHorarioInicio() != null && !dto.getHorarioInicio().isEmpty())
            servicio.setHorarioInicio(LocalTime.parse(dto.getHorarioInicio()));
        if (dto.getHorarioFin() != null && !dto.getHorarioFin().isEmpty())
            servicio.setHorarioFin(LocalTime.parse(dto.getHorarioFin()));

        servicio.setDiasSemana(dto.getDiasSemana() != null ? dto.getDiasSemana() : Collections.emptySet());
        return servicio;
    }
}

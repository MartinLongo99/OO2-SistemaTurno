package com.oo2.grupo15.services.implementation;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.entities.Servicio;
import com.oo2.grupo15.repositories.IServicioRepository;
import com.oo2.grupo15.services.IServicioService;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private IServicioRepository servicioRepository;

    public ServicioService(IServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<ServicioDTO> findAll() {
        return servicioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServicioDTO> getAll() {
        return servicioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServicioDTO> findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicioDTO> findByEstado(Boolean estado) {
        boolean estadoValue = (estado != null) ? estado : false;
        return servicioRepository.findByEstado(estadoValue).stream()
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
        Servicio saved = servicioRepository.save(servicio);
        return convertToDTO(saved);
    }

    @Override
    public ServicioDTO update(Long id, ServicioDTO dto) {
        if (servicioRepository.existsById(id)) {
            Servicio servicio = convertToEntity(dto);
            servicio.setId(id);
            Servicio updated = servicioRepository.save(servicio);
            return convertToDTO(updated);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        servicioRepository.deleteById(id);
        return true;
    }

    @Override
    public Servicio findEntityById(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

    private ServicioDTO convertToDTO(Servicio servicio) {
        return new ServicioDTO(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDuracionMinutos(),
                servicio.isEstado(),
                servicio.getHorarioInicio() != null ? servicio.getHorarioInicio().toString() : null,
                servicio.getHorarioFin() != null ? servicio.getHorarioFin().toString() : null,
                servicio.getDiasSemana()
        );
    }

    private Servicio convertToEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setId(dto.id());
        servicio.setNombre(dto.nombre());
        servicio.setEstado(dto.estado());
        servicio.setDuracionMinutos(dto.duracionMinutos());

        if (dto.horarioInicio() != null && !dto.horarioInicio().isEmpty()) {
            servicio.setHorarioInicio(LocalTime.parse(dto.horarioInicio()));
        }

        if (dto.horarioFin() != null && !dto.horarioFin().isEmpty()) {
            servicio.setHorarioFin(LocalTime.parse(dto.horarioFin()));
        }

        servicio.setDiasSemana(dto.diasSemana() != null ? dto.diasSemana() : Collections.emptySet());

        return servicio;
    }
}
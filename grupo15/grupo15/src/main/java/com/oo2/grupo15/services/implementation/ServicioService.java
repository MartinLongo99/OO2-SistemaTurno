package com.oo2.grupo15.services.implementation;

import java.time.DayOfWeek;
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

    @Override
    public List<ServicioDTO> findAll() {
        return servicioRepository.findAll().stream()
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
    public void delete(Long id) {
        servicioRepository.deleteById(id);
    }

    // ---------- Métodos auxiliares de mapeo ----------

    private ServicioDTO convertToDTO(Servicio servicio) {
        Set<String> dias = servicio.getDiasSemana().stream()
            .map(d -> capitalize(d.name()))
            .collect(Collectors.toSet());

        return new ServicioDTO(
            servicio.getId(),
            servicio.getNombre(),
            servicio.getDuracionMinutos(),
            servicio.isEstado(),
            servicio.getHorarioInicio() != null ? servicio.getHorarioInicio().toString() : null,
            servicio.getHorarioFin() != null ? servicio.getHorarioFin().toString() : null,
            dias
        );
    }

    private Servicio convertToEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setId(dto.getId());
        servicio.setNombre(dto.getNombre());
        servicio.setEstado(dto.isEstado());
        servicio.setDuracionMinutos(dto.getDuracionMinutos());

        if (dto.getHorarioInicio() != null && !dto.getHorarioInicio().isEmpty()) {
            servicio.setHorarioInicio(LocalTime.parse(dto.getHorarioInicio()));
        }

        if (dto.getHorarioFin() != null && !dto.getHorarioFin().isEmpty()) {
            servicio.setHorarioFin(LocalTime.parse(dto.getHorarioFin()));
        }

        if (dto.getDiasSemana() != null && !dto.getDiasSemana().isEmpty()) {
            Set<DayOfWeek> dias = dto.getDiasSemana().stream()
                .map(this::toDayOfWeek)
                .collect(Collectors.toSet());
            servicio.setDiasSemana(dias);
        } else {
            servicio.setDiasSemana(Collections.emptySet());
        }

        return servicio;
    }

    // ---------- Utilidades ----------

    private static final Map<String, DayOfWeek> DIAS_MAP = Map.ofEntries(
    	    Map.entry("LUNES", DayOfWeek.MONDAY),
    	    Map.entry("MARTES", DayOfWeek.TUESDAY),
    	    Map.entry("MIÉRCOLES", DayOfWeek.WEDNESDAY),
    	    Map.entry("MIERCOLES", DayOfWeek.WEDNESDAY), // por si no viene con tilde
    	    Map.entry("JUEVES", DayOfWeek.THURSDAY),
    	    Map.entry("VIERNES", DayOfWeek.FRIDAY),
    	    Map.entry("SÁBADO", DayOfWeek.SATURDAY),
    	    Map.entry("SABADO", DayOfWeek.SATURDAY),
    	    Map.entry("DOMINGO", DayOfWeek.SUNDAY)
    	);

    	private DayOfWeek toDayOfWeek(String dia) {
    	    return DIAS_MAP.get(dia.toUpperCase());
    	}


    private String capitalize(String diaEnum) {
        String lower = diaEnum.toLowerCase();
        String capitalized = Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
        switch (capitalized) {
            case "Miercoles": return "Miércoles";
            case "Sabado": return "Sábado";
            default: return capitalized;
        }
    }
}

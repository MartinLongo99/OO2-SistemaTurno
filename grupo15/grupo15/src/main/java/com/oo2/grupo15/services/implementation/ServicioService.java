package com.oo2.grupo15.services.implementation;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
    
	private ModelMapper modelMapper = new ModelMapper();

    public ServicioService(IServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}

	@Override
    public List<ServicioDTO> findAll() {
        return servicioRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ServicioDTO> getAll(){
    	return servicioRepository.findAll()
    			.stream()
    			.map(servicio -> modelMapper.map(servicio, ServicioDTO.class))
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

        servicio.setDiasSemana(dto.getDiasSemana() != null ? dto.getDiasSemana() : Collections.emptySet());

        return servicio;
    }
}


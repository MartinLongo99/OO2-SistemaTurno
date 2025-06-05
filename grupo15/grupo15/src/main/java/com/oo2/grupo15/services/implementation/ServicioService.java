package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.entities.Servicio;
import com.oo2.grupo15.repositories.IServicioRepository;
import com.oo2.grupo15.services.IServicioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioService implements IServicioService {

    @Autowired
    private IServicioRepository servicioRepo;

    private ServicioDTO toDTO(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setDuracionMinutos(servicio.getDuracionMinutos());
        dto.setEstado(servicio.isEstado());
        dto.setHorarioInicio(servicio.getHorarioInicio());
        dto.setHorarioFin(servicio.getHorarioFin());
        return dto;
    }

    private Servicio toEntity(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setId(dto.getId());
        servicio.setNombre(dto.getNombre());
        servicio.setDuracionMinutos(dto.getDuracionMinutos());
        servicio.setEstado(dto.isEstado());
        servicio.setHorarioInicio(dto.getHorarioInicio());
        servicio.setHorarioFin(dto.getHorarioFin());
        return servicio;
    }

    @Override
    public List<ServicioDTO> getAll() {
        return servicioRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServicioDTO getById(Long id) {
        Optional<Servicio> servicio = servicioRepo.findById(id);
        return servicio.map(this::toDTO).orElse(null);
    }

    @Override
    public ServicioDTO save(ServicioDTO dto) {
        Servicio servicio = toEntity(dto);
        return toDTO(servicioRepo.save(servicio));
    }

    @Override
    public ServicioDTO update(Long id, ServicioDTO dto) {
        if (servicioRepo.existsById(id)) {
            Servicio servicio = toEntity(dto);
            servicio.setId(id);
            return toDTO(servicioRepo.save(servicio));
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        servicioRepo.deleteById(id);
    }
}

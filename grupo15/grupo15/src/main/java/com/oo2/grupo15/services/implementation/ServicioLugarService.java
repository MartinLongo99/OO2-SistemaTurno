package com.oo2.grupo15.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.oo2.grupo15.entities.*;
import com.oo2.grupo15.dtos.ServicioLugarDTO;
import com.oo2.grupo15.repositories.*;
import com.oo2.grupo15.services.IServicioLugarService;

@Service
public class ServicioLugarService implements IServicioLugarService {

    @Autowired
    private IServicioLugarRepository servicioLugarRepo;

    @Autowired
    private IServicioRepository servicioRepo;

    @Autowired
    private ILugarRepository lugarRepo;

    @Autowired
    private IProfesionalRepository profesionalRepo;

    @Override
    public List<ServicioLugarDTO> getAll() {
        return servicioLugarRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ServicioLugarDTO getById(Long id) {
        return servicioLugarRepo.findById(id).map(this::toDTO).orElse(null);
    }

    @Override
    public ServicioLugarDTO save(ServicioLugarDTO dto) {
        ServicioLugar entity = toEntity(dto);
        return toDTO(servicioLugarRepo.save(entity));
    }

    @Override
    public ServicioLugarDTO update(Long id, ServicioLugarDTO dto) {
        ServicioLugar existing = servicioLugarRepo.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setActivo(dto.isActivo());
        existing.setServicio(servicioRepo.findById(dto.getServicioId()).orElse(null));
        existing.setLugar(lugarRepo.findById(dto.getLugarId()).orElse(null));
        existing.setProfesional(profesionalRepo.findById(dto.getProfesionalId()).orElse(null));

        return toDTO(servicioLugarRepo.save(existing));
    }

    @Override
    public void delete(Long id) {
        servicioLugarRepo.deleteById(id);
    }

    // Mappers
    private ServicioLugarDTO toDTO(ServicioLugar s) {
        Profesional profesional = s.getProfesional();
        String nombreProfesional = "";

        if (profesional != null && profesional.getContacto() != null) {
            nombreProfesional = profesional.getContacto().getNombre();
        }

        return new ServicioLugarDTO(
            s.getId(),
            s.getServicio().getId(),
            s.getServicio().getNombre(),
            s.getLugar().getId(),
            s.getLugar().getNombre(),
            profesional.getId(),
            nombreProfesional,
            s.isActivo()
        );
    }


    private ServicioLugar toEntity(ServicioLugarDTO dto) {
        ServicioLugar entity = new ServicioLugar();
        entity.setId(dto.getId());
        entity.setActivo(dto.isActivo());
        entity.setServicio(servicioRepo.findById(dto.getServicioId()).orElse(null));
        entity.setLugar(lugarRepo.findById(dto.getLugarId()).orElse(null));
        entity.setProfesional(profesionalRepo.findById(dto.getProfesionalId()).orElse(null));
        return entity;
    }
}

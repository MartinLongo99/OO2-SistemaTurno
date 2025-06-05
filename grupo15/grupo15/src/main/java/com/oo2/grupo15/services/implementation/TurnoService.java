package com.oo2.grupo15.services.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.entities.ServicioLugar;
import com.oo2.grupo15.entities.Solicitante;
import com.oo2.grupo15.entities.Turno;
import com.oo2.grupo15.repositories.ITurnoRepository;
import com.oo2.grupo15.repositories.IServicioLugarRepository;
import com.oo2.grupo15.repositories.ISolicitanteRepository;
import com.oo2.grupo15.services.ITurnoService;

@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IServicioLugarRepository servicioLugarRepository;

    @Autowired
    private ISolicitanteRepository solicitanteRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TurnoDTO crearTurno(TurnoDTO dto) {
        Turno turno = new Turno();
        turno.setFechaHora(dto.getFechaHora());
        turno.setEstado(dto.isEstado());
        turno.setServicioLugar(servicioLugarRepository.findById(dto.getServicioLugarId()).orElseThrow());
        turno.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId()).orElseThrow());
        
        Turno saved = turnoRepository.save(turno);
        return modelMapper.map(saved, TurnoDTO.class);
    }

    @Override
    public TurnoDTO actualizarTurno(Long id, TurnoDTO dto) {
        Turno turno = turnoRepository.findById(id).orElseThrow();
        turno.setFechaHora(dto.getFechaHora());
        turno.setEstado(dto.isEstado());
        turno.setServicioLugar(servicioLugarRepository.findById(dto.getServicioLugarId()).orElseThrow());
        turno.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId()).orElseThrow());

        Turno updated = turnoRepository.save(turno);
        return modelMapper.map(updated, TurnoDTO.class);
    }

    @Override
    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public List<TurnoDTO> obtenerTodos() {
        return turnoRepository.findAll()
                .stream()
                .map(turno -> modelMapper.map(turno, TurnoDTO.class))
                .toList();
    }

    @Override
    public TurnoDTO obtenerPorId(Long id) {
        return modelMapper.map(turnoRepository.findById(id).orElseThrow(), TurnoDTO.class);
    }
    
    @Override
    public List<TurnoDTO> obtenerTurnosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return turnoRepository.findByFechaHoraBetweenOrderByFechaHoraAsc(fechaInicio, fechaFin)
                .stream()
                .map(turno -> {
                    TurnoDTO dto = modelMapper.map(turno, TurnoDTO.class);
                    dto.setServicioLugarId(turno.getServicioLugar().getId());
                    dto.setSolicitanteId(turno.getSolicitante().getId());
                    return dto;
                })
                .toList();
    }
}
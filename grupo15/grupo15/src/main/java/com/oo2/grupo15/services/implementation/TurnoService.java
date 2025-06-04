package com.oo2.grupo15.services.implementation;

import java.util.List;

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

    @Override
    public Turno crearTurno(TurnoDTO dto) {
        Turno turno = new Turno();
        turno.setFechaHora(dto.getFechaHora());
        turno.setEstado(dto.isEstado());
        turno.setServicioLugar(servicioLugarRepository.findById(dto.getServicioLugarId()).orElseThrow());
        turno.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId()).orElseThrow());
        return turnoRepository.save(turno);
    }

    @Override
    public Turno actualizarTurno(Long id, TurnoDTO dto) {
        Turno turno = turnoRepository.findById(id).orElseThrow();
        turno.setFechaHora(dto.getFechaHora());
        turno.setEstado(dto.isEstado());
        turno.setServicioLugar(servicioLugarRepository.findById(dto.getServicioLugarId()).orElseThrow());
        turno.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId()).orElseThrow());
        return turnoRepository.save(turno);
    }

    @Override
    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public List<Turno> obtenerTodos() {
        return turnoRepository.findAll();
    }

    @Override
    public Turno obtenerPorId(Long id) {
        return turnoRepository.findById(id).orElseThrow();
    }
}

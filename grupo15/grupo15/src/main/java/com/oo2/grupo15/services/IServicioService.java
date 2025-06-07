package com.oo2.grupo15.services;

import java.util.List;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.entities.Servicio;

public interface IServicioService {
    List<ServicioDTO> findAll();
    ServicioDTO findById(Long id);
    ServicioDTO save(ServicioDTO servicioDTO);
    ServicioDTO update(Long id, ServicioDTO servicioDTO);
    void delete(Long id);
    List<ServicioDTO> getAll();
    List<ServicioDTO> findByNombre(String nombre);
	List<ServicioDTO> findByEstado(boolean estado);
	List<ServicioDTO> findByDuracionMinutos(int duracion);
}

package com.oo2.grupo15.services;

import java.util.List;
import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.entities.Servicio;

public interface IServicioService {
    List<ServicioDTO> getAll();
    List<ServicioDTO> findAll();
    ServicioDTO findById(Long id);
    ServicioDTO save(ServicioDTO dto);
    ServicioDTO update(Long id, ServicioDTO dto);
    boolean delete(Long id);
    
    // Método adicional para obtener la entidad directamente
    Servicio findEntityById(Long id);
}
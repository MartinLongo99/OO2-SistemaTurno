package com.oo2.grupo15.services;

import java.util.List;
import com.oo2.grupo15.dtos.ServicioLugarDTO;

public interface IServicioLugarService {
    List<ServicioLugarDTO> getAll();
    ServicioLugarDTO getById(Long id);
    ServicioLugarDTO save(ServicioLugarDTO dto);
    ServicioLugarDTO update(Long id, ServicioLugarDTO dto);
    void delete(Long id);
}

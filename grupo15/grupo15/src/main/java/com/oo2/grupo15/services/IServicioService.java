package com.oo2.grupo15.services;

import com.oo2.grupo15.dtos.ServicioDTO;
import java.util.List;

public interface IServicioService {
    List<ServicioDTO> getAll();
    ServicioDTO getById(Long id);
    ServicioDTO save(ServicioDTO servicio);
    ServicioDTO update(Long id, ServicioDTO servicio);
    void delete(Long id);
}

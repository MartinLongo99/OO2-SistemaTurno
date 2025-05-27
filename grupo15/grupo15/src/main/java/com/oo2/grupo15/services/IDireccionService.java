package com.oo2.grupo15.services;

import java.util.List;

import com.oo2.grupo15.dtos.DireccionDTO;


public interface IDireccionService {
    public List<DireccionDTO> getDireccionesByLocalidad(int localidadId);

}

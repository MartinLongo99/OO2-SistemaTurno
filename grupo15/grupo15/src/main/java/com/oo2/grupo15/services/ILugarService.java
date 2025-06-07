package com.oo2.grupo15.services;

import java.util.List;
import com.oo2.grupo15.dtos.LugarDTO;

public interface ILugarService {
	List<LugarDTO> getLugaresByLocalidad(int localidadId);
	List<LugarDTO> getLugaresByNombre(String nombre);
	List<LugarDTO> getAll();
}

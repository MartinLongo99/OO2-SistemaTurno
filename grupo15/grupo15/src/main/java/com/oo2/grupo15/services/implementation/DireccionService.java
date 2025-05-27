package com.oo2.grupo15.services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.DireccionDTO;
import com.oo2.grupo15.repositories.IDireccionRepository;
import com.oo2.grupo15.repositories.ILocalidadRepository;
import com.oo2.grupo15.services.IDireccionService;


@Service("DireccionService")
public class DireccionService implements IDireccionService{
	
	private IDireccionRepository direccionRepository;
	
    private ModelMapper modelMapper = new ModelMapper();

    public DireccionService(IDireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

	
    public List<DireccionDTO> getDireccionesByLocalidad(int localidadId) {
        return direccionRepository.findByLocalidadIdOrderByCalleYAlturaAsc(localidadId) // 
                                .stream()
                                .map(direccion -> modelMapper.map(direccion, DireccionDTO.class))
                                .collect(Collectors.toList());
    }
}

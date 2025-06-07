package com.oo2.grupo15.services.implementation;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.repositories.IServicioRepository;
import com.oo2.grupo15.services.IServicioService;

@Service("ServicioService")
public class ServicioService implements IServicioService{
	
	private IServicioRepository servicioRepository;
	private ModelMapper modelMapper = new ModelMapper();
	
	public ServicioService(IServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}

	public List<ServicioDTO> getAll(){
		return servicioRepository.findAll()
				.stream()
				.map(servicio -> modelMapper.map(servicio, ServicioDTO.class))
				.collect(Collectors.toList());
	}

}

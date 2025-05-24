package com.oo2.grupo15.services.implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.entities.Localidad;
import com.oo2.grupo15.repositories.ILocalidadRepository;
import com.oo2.grupo15.services.ILocalidadService;

@Service("LocalidadService")
public class LocalidadService implements ILocalidadService{
	private ILocalidadRepository localidadRepository;

	private ModelMapper modelMapper = new ModelMapper();

	public LocalidadService(ILocalidadRepository localidadRepository) {
		this.localidadRepository = localidadRepository;
	}

	@Override
	public List<Localidad> getAll() {
		return localidadRepository.findAll();
	}

	@Override
	public boolean remove(int id) {
		try {
			localidadRepository.deleteById(id);
			return true;
		}catch (Exception e) {
			return false;
		}
	}
}

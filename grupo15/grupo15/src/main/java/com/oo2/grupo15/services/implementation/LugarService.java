package com.oo2.grupo15.services.implementation;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.LugarDTO;
import com.oo2.grupo15.entities.Lugar;
import com.oo2.grupo15.repositories.ILugarRepository;
import com.oo2.grupo15.services.ILugarService;

@Service("LugarService")
public class LugarService implements ILugarService{
	
	private ILugarRepository lugarRepository;
	private ModelMapper modelMapper = new ModelMapper();
	
	public LugarService(ILugarRepository lugarRepository) {
		this.lugarRepository = lugarRepository;
	}
	
	public List<LugarDTO> getLugaresByLocalidad(int localidadId){
		return lugarRepository.findByDireccionLocalidadId(localidadId)
				.stream()
				.map(lugar -> modelMapper.map(lugar, LugarDTO.class))
				.collect(Collectors.toList());
	}
	
	public List<LugarDTO> getLugaresByNombre(String nombre){
		return lugarRepository.findByNombre(nombre)
				.stream()
				.map(lugar -> modelMapper.map(lugar, LugarDTO.class))
				.collect(Collectors.toList());
	}

	public List<LugarDTO> getAll(){
        Sort sort = Sort.by(
                Sort.Order.asc("direccion.localidad.provincia.nombre"), 
                Sort.Order.asc("direccion.localidad.nombre") 
            );
		return lugarRepository.findAll(sort)
				.stream()
				.map(lugar -> modelMapper.map(lugar, LugarDTO.class))
				.collect(Collectors.toList());
	}
	
	
	@Override
	public LugarDTO save(LugarDTO dto) {
	    Lugar lugar = modelMapper.map(dto, Lugar.class);
	    lugar = lugarRepository.save(lugar);
	    return modelMapper.map(lugar, LugarDTO.class);
	}

	@Override
	public LugarDTO getById(int id) {
	    return lugarRepository.findById(id)
	        .map(lugar -> modelMapper.map(lugar, LugarDTO.class))
	        .orElse(null);
	}

	@Override
	public boolean existeLugarDuplicado(LugarDTO dto) {
	    return lugarRepository.existsByNombreAndDireccion_CalleYAlturaAndDireccion_Localidad_Id(
	        dto.getNombre(),
	        dto.getDireccion().getCalleYAltura(),
	        dto.getDireccion().getLocalidad().getId()
	    );
	}


	
	@Override
	public boolean delete(int id) {
	    try {
	        lugarRepository.deleteById(id);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

}

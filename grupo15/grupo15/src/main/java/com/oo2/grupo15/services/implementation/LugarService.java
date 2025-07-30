package com.oo2.grupo15.services.implementation;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.DireccionDTO;
import com.oo2.grupo15.dtos.LugarDTO;
import com.oo2.grupo15.entities.Direccion;
import com.oo2.grupo15.entities.Lugar;
import com.oo2.grupo15.repositories.ILugarRepository;
import com.oo2.grupo15.services.ILugarService;

@Service("LugarService")
public class LugarService implements ILugarService{
	
	 private ILugarRepository lugarRepository;
	    private ModelMapper modelMapper;

	    public LugarService(ILugarRepository lugarRepository) {
	        this.lugarRepository = lugarRepository;
	        this.modelMapper = new ModelMapper();

	        // Configuración para que ModelMapper entienda los records
	        modelMapper.getConfiguration().setAmbiguityIgnored(true);

	        modelMapper.createTypeMap(Lugar.class, LugarDTO.class).setProvider(request -> {
	            Lugar source = (Lugar) request.getSource();
	            return new LugarDTO(
	                source.getId(),
	                source.getNombre(),
	                modelMapper.map(source.getDireccion(), DireccionDTO.class)
	            );
	        });

	        modelMapper.createTypeMap(LugarDTO.class, Lugar.class).setProvider(request -> {
	            LugarDTO source = (LugarDTO) request.getSource();
	            Lugar lugar = new Lugar();
	            lugar.setId(source.id());
	            lugar.setNombre(source.nombre());
	            lugar.setDireccion(modelMapper.map(source.direccion(), Direccion.class));
	            return lugar;
	        });
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
	    if (dto == null || dto.direccion() == null || dto.direccion().getLocalidad() == null) {
	        return false; // o lanzar excepción si querés
	    }
	    return lugarRepository.existsByNombreAndDireccion_CalleYAlturaAndDireccion_Localidad_Id(
	        dto.nombre(),
	        dto.direccion().getCalleYAltura(),
	        dto.direccion().getLocalidad().getId()
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

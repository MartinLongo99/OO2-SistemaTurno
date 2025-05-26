package com.oo2.grupo15.services.implementation;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.oo2.grupo15.dtos.ProvinciaDTO;
import com.oo2.grupo15.repositories.IProvinciaRepository;
import com.oo2.grupo15.services.IProvinciaService;

@Service("ProvinciaService")
public class ProvinciaService implements IProvinciaService {
	private IProvinciaRepository provinciaRepository;

	private ModelMapper modelMapper = new ModelMapper();

	public ProvinciaService(IProvinciaRepository provinciaRepository) {
		this.provinciaRepository = provinciaRepository;
	}

	@Override
	public List<ProvinciaDTO> getAll() {
		return provinciaRepository.findAll(Sort.by("nombre")).stream().map(provincia -> modelMapper.map(provincia, ProvinciaDTO.class))
				.collect(Collectors.toList());
	}
}

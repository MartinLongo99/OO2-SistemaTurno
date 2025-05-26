package com.oo2.grupo15.services.implementation;

import java.util.List;
import java.util.stream.Collectors; // Necesario para el stream

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.LocalidadDTO;
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
    public List<LocalidadDTO> getAll() {
        return localidadRepository.findAllWithProvincia() // Usa el nuevo método
                                .stream()
                                .map(localidad -> modelMapper.map(localidad, LocalidadDTO.class))
                                .collect(Collectors.toList());
    }

    @Override
    public LocalidadDTO insertOrUpdate(LocalidadDTO localidadModel) {
        Localidad localidad = localidadRepository.save(modelMapper.map(localidadModel, Localidad.class));
        return modelMapper.map(localidad, LocalidadDTO.class);
    }

    @Override
    public boolean delete(int id) {
        try {
            localidadRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    
    public List<LocalidadDTO> getLocalidadesByProvincia(int provinciaId) {
        return localidadRepository.findByProvinciaId(provinciaId,Sort.by("Nombre")) // 
                                .stream()
                                .map(localidad -> modelMapper.map(localidad, LocalidadDTO.class))
                                .collect(Collectors.toList());
    }

}
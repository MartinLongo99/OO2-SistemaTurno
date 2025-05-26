package com.oo2.grupo15.services;

import java.util.List;

import com.oo2.grupo15.dtos.LocalidadDTO; // Importa LocalidadDTO

public interface ILocalidadService {

    public List<LocalidadDTO> getAll(); 

    public LocalidadDTO insertOrUpdate(LocalidadDTO localidadModel);

    public boolean delete(int id);
    
    public List<LocalidadDTO> getLocalidadesByProvincia(int provinciaId);
}
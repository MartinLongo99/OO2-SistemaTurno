package com.oo2.grupo15.services;

import java.util.List;

import com.oo2.grupo15.dtos.LocalidadDTO; // Importa LocalidadDTO

public interface ILocalidadService {

    public LocalidadDTO insertOrUpdate(LocalidadDTO localidadModel);

    public boolean delete(int id);
    
    public List<LocalidadDTO> getLocalidadesByProvincia(int provinciaId);
}
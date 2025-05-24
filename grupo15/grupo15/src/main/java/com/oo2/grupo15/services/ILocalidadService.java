package com.oo2.grupo15.services;

import java.util.List;

import com.oo2.grupo15.dtos.LocalidadDTO; // Importa LocalidadDTO

public interface ILocalidadService {

    public List<LocalidadDTO> getAll(); // Cambia el tipo de retorno a List<LocalidadDTO>

    public LocalidadDTO insertOrUpdate(LocalidadDTO localidadModel);

    public boolean remove(int id);
}
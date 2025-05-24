package com.oo2.grupo15.services;

import java.util.List;

import com.oo2.grupo15.entities.Localidad;

public interface ILocalidadService {

	public List<Localidad> getAll();

	//public LocalidadDTO insertOrUpdate(LocalidadDTO localidadModel);

	public boolean remove(int id);
}

package com.oo2.grupo15.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Provincia;

@Repository("provinciaRepository")
public interface IProvinciaRepository extends JpaRepository<Provincia, Serializable> {
	public abstract Optional<Provincia> findByNombre(String nombre);
}

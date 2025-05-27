package com.oo2.grupo15.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Localidad;

@Repository("localidadRepository")
public interface ILocalidadRepository extends JpaRepository<Localidad, Serializable> {
	Optional<Localidad> findByNombre(String nombre);

    @Query("SELECT l FROM Localidad l JOIN FETCH l.provincia")
    List<Localidad> findAllWithProvincia(); 
    
    List<Localidad> findByProvinciaIdOrderByNombreAsc(int provinciaId);
}

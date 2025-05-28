package com.oo2.grupo15.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Lugar;

@Repository("lugarRepository")
public interface ILugarRepository extends JpaRepository<Lugar, Serializable>{
	List<Lugar> findByDireccionLocalidadId(int localidadId);
}

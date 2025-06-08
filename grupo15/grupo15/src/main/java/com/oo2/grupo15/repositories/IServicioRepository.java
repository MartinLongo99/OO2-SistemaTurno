
package com.oo2.grupo15.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Servicio;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Long> {
	List<Servicio> findByNombre(String nombre);
	List<Servicio> findByEstado(boolean estado);
	List<Servicio> findByDuracionMinutos(int duracion);
}

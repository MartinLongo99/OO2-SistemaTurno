package com.oo2.grupo15.repositories;

import com.oo2.grupo15.entities.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}

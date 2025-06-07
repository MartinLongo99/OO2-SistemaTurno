package com.oo2.grupo15.repositories;

import com.oo2.grupo15.entities.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IProfesionalRepository extends JpaRepository<Profesional, Long> {
    List<Profesional> findByMatricula(String matricula);
}


package com.oo2.grupo15.repositories;

import com.oo2.grupo15.entities.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProfesionalRepository extends JpaRepository<Profesional, Long> {
    // Consultas por atributos específicos
    List<Profesional> findByMatricula(String matricula);
}

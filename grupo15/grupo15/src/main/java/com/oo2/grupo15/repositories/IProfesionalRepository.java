package com.oo2.grupo15.repositories;

import com.oo2.grupo15.entities.Profesional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfesionalRepository extends JpaRepository<Profesional, Long> {

    @EntityGraph(attributePaths = {
        "contacto",
        "contacto.direccion",
        "contacto.direccion.localidad",
        "contacto.direccion.localidad.provincia",
        "especialidades"
    })
    Optional<Profesional> findByMatricula(String matricula);

    @Override
    @EntityGraph(attributePaths = {
        "contacto",
        "contacto.direccion",
        "contacto.direccion.localidad",
        "contacto.direccion.localidad.provincia",
        "especialidades"
    })
    java.util.List<Profesional> findAll();

    @Override
    @EntityGraph(attributePaths = {
        "contacto",
        "contacto.direccion",
        "contacto.direccion.localidad",
        "contacto.direccion.localidad.provincia",
        "especialidades"
    })
    Optional<Profesional> findById(Long id);
}

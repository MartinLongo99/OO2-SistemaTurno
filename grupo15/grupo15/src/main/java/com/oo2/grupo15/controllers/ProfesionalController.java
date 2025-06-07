
package com.oo2.grupo15.controllers;

import com.oo2.grupo15.entities.Profesional;
import com.oo2.grupo15.services.IProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalController {

    @Autowired
    private IProfesionalService profesionalService;

    @GetMapping("/buscar")
    public List<Profesional> buscarPorMatricula(@RequestParam String matricula) {
        List<Profesional> resultados = profesionalService.findByMatricula(matricula);

        if (resultados.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontraron profesionales con esa matrícula."
            );
        }

        return resultados;
    }
}

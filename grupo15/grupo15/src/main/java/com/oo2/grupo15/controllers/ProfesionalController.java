package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.IProfesionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/profesionales")
public class ProfesionalController {

    @Autowired
    private IProfesionalService profesionalService;

    // Vista HTML
    @GetMapping("")
    public String index() {
        return ViewRouteHelper.PROFESIONAL_INDEX;
    }

    // API para buscar por matrícula
    @GetMapping("/api/buscar")
    @ResponseBody
    public List<ProfesionalDTO> buscarPorMatricula(@RequestParam String matricula) {
        List<ProfesionalDTO> resultados = profesionalService.findDTOsByMatricula(matricula);

        if (resultados.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron profesionales con esa matrícula");
        }

        return resultados;
    }
}

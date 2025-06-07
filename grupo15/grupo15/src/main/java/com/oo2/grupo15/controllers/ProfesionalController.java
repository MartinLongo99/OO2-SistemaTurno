package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ProfesionalDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.IProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profesionales")
public class ProfesionalController {

    @Autowired
    private IProfesionalService profesionalService;

    // 🔹 Vista HTML
    @GetMapping
    public String vistaProfesionales() {
        return ViewRouteHelper.PROFESIONAL_INDEX; // Asegurate que tenga valor "profesional/index"
    }

    // 🔹 API REST
    @ResponseBody
    @GetMapping("/api/buscar")
    public List<ProfesionalDTO> buscarPorMatricula(@RequestParam String matricula) {
        return profesionalService.buscarPorMatricula(matricula);
    }

    @ResponseBody
    @GetMapping("/api")
    public List<ProfesionalDTO> obtenerTodos() {
        return profesionalService.obtenerTodos();
    }
}

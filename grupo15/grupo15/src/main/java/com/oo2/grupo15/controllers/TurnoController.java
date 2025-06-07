package com.oo2.grupo15.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo15.repositories.ITurnoRepository;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoRepository turnoRepository;

    @GetMapping
    public String index() {
        return "turno/index";
    }

    // Endpoint simplificado que solo retorna IDs y fechas
    @GetMapping("/disponibilidad")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> verificarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<Object[]> resultados = turnoRepository.buscarTurnosSimplificados(fechaInicio, fechaFin);
        List<Map<String, Object>> turnos = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Map<String, Object> turno = new HashMap<>();
            turno.put("id", resultado[0]);
            turno.put("fechaHora", resultado[1]);
            turno.put("estado", resultado[2]);

            turnos.add(turno);
        }

        return ResponseEntity.ok(turnos);
    }
}
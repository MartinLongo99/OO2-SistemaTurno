package com.oo2.grupo15.controllers;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.services.ITurnoService;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @GetMapping
    public String index() {
        return "turno/index";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TurnoDTO> crear(@RequestBody TurnoDTO dto) {
        return ResponseEntity.ok(turnoService.crearTurno(dto));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TurnoDTO> actualizar(@PathVariable Long id, @RequestBody TurnoDTO dto) {
        return ResponseEntity.ok(turnoService.actualizarTurno(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lista")
    @ResponseBody
    public ResponseEntity<List<TurnoDTO>> listar() {
        return ResponseEntity.ok(turnoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TurnoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<TurnoDTO>> buscarEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(turnoService.obtenerTurnosEntreFechas(fechaInicio, fechaFin));
    }
}
package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.services.IServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "API de Servicios", description = "Operaciones de la API para la gestión de servicios")
public class ServicioRestController {

    @Autowired
    private IServicioService servicioService;

    @Operation(summary = "Listar todos los servicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de servicios recuperada exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ServicioDTO>> getAllServicios() {
        return ResponseEntity.ok(servicioService.findAll());
    }

    @Operation(summary = "Obtener un servicio por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio encontrado"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> getServicioById(@PathVariable Long id) {
        ServicioDTO servicio = servicioService.findById(id);
        if (servicio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(servicio);
    }

    @Operation(summary = "Crear o actualizar un servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Servicio creado o actualizado exitosamente")
    })

    @PostMapping
    public ResponseEntity<ServicioDTO> saveServicio(@RequestBody ServicioDTO dto) {
        try {
            ServicioDTO savedServicio = (dto.id() != null)
                    ? servicioService.update(dto.id(), dto)
                    : servicioService.save(dto);

            if (savedServicio == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            return new ResponseEntity<>(savedServicio, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Eliminar un servicio por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.ok().build();
    }
}
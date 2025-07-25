package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.dtos.LugarDTO;
import com.oo2.grupo15.exceptions.LugarDuplicadoException;
import com.oo2.grupo15.services.ILocalidadService;
import com.oo2.grupo15.services.ILugarService;
import com.oo2.grupo15.services.IProvinciaService;
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
@RequestMapping("/api/lugares")
@Tag(name = "API de Lugares", description = "Operaciones para la gestión de lugares")
public class LugarRestController {

    @Autowired
    private ILocalidadService localidadService;

    @Autowired
    private IProvinciaService provinciaService;

    @Autowired
    private ILugarService lugarService;

    @Operation(summary = "Obtener todos los lugares")
    @ApiResponse(responseCode = "200", description = "Lugares obtenidos exitosamente")
    @GetMapping
    public ResponseEntity<List<LugarDTO>> getAllLugares() {
        List<LugarDTO> lugares = lugarService.getAll();
        return ResponseEntity.ok(lugares);
    }

    @Operation(summary = "Obtener lugar por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lugar encontrado"),
        @ApiResponse(responseCode = "404", description = "Lugar no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LugarDTO> getLugarById(@PathVariable("id") int id) {
        LugarDTO lugar = lugarService.getById(id);
        if (lugar == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lugar);
    }

    @Operation(summary = "Obtener localidades por provincia")
    @GetMapping("/porProvincia")
    public ResponseEntity<List<LocalidadDTO>> getLocalidadesPorProvincia(@RequestParam("provinciaId") int provinciaId) {
        List<LocalidadDTO> localidades = (provinciaId != 0)
                ? localidadService.getLocalidadesByProvincia(provinciaId)
                : List.of();
        return ResponseEntity.ok(localidades);
    }

    @Operation(summary = "Obtener lugares por localidad")
    @GetMapping("/porLocalidad")
    public ResponseEntity<List<LugarDTO>> getLugaresByLocalidad(@RequestParam("localidadId") int localidadId) {
        List<LugarDTO> lugares = (localidadId != 0)
                ? lugarService.getLugaresByLocalidad(localidadId)
                : List.of();
        return ResponseEntity.ok(lugares);
    }

    @Operation(summary = "Crear un nuevo lugar")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lugar creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Lugar duplicado")
    })
    @PostMapping
    public ResponseEntity<LugarDTO> createLugar(@RequestBody LugarDTO lugarDTO) {
        if (lugarService.existeLugarDuplicado(lugarDTO)) {
            throw new LugarDuplicadoException("Ya existe un lugar con el mismo nombre, calle y localidad.");
        }
        LugarDTO nuevo = lugarService.save(lugarDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @Operation(summary = "Actualizar un lugar existente")
    @PutMapping("/{id}")
    public ResponseEntity<LugarDTO> updateLugar(@PathVariable("id") int id, @RequestBody LugarDTO lugarDTO) {
        LugarDTO existente = lugarService.getById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        LugarDTO lugarConId = new LugarDTO(id, lugarDTO.nombre(), lugarDTO.direccion());
        LugarDTO actualizado = lugarService.save(lugarConId);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un lugar por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLugar(@PathVariable("id") int id) {
        LugarDTO existente = lugarService.getById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        lugarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}//prueba
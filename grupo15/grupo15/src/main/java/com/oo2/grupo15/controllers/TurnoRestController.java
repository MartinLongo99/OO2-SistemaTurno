package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.SolicitanteDTO;
import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.entities.Servicio;
import com.oo2.grupo15.repositories.ITurnoRepository;
import com.oo2.grupo15.services.IServicioService;
import com.oo2.grupo15.services.ITurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TurnoBookingRequestDTO {
    private LocalDateTime fechaHora;
    private Long servicioLugarId;
    private String nombreSolicitante;
    private String apellidoSolicitante;
    private long dniSolicitante;
    private String emailSolicitante;
    private String telefonoSolicitante;

    public TurnoBookingRequestDTO() {}

    public TurnoBookingRequestDTO(LocalDateTime fechaHora, Long servicioLugarId, String nombreSolicitante,
                                 String apellidoSolicitante, long dniSolicitante, String emailSolicitante,
                                 String telefonoSolicitante) {
        this.fechaHora = fechaHora;
        this.servicioLugarId = servicioLugarId;
        this.nombreSolicitante = nombreSolicitante;
        this.apellidoSolicitante = apellidoSolicitante;
        this.dniSolicitante = dniSolicitante;
        this.emailSolicitante = emailSolicitante;
        this.telefonoSolicitante = telefonoSolicitante;
    }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Long getServicioLugarId() { return servicioLugarId; }
    public void setServicioLugarId(Long servicioLugarId) { this.servicioLugarId = servicioLugarId; }
    public String getNombreSolicitante() { return nombreSolicitante; }
    public void setNombreSolicitante(String nombreSolicitante) { this.nombreSolicitante = nombreSolicitante; }
    public String getApellidoSolicitante() { return apellidoSolicitante; }
    public void setApellidoSolicitante(String apellidoSolicitante) { this.apellidoSolicitante = apellidoSolicitante; }
    public long getDniSolicitante() { return dniSolicitante; }
    public void setDniSolicitante(long dniSolicitante) { this.dniSolicitante = dniSolicitante; }
    public String getEmailSolicitante() { return emailSolicitante; }
    public void setEmailSolicitante(String emailSolicitante) { this.emailSolicitante = emailSolicitante; }
    public String getTelefonoSolicitante() { return telefonoSolicitante; }
    public void setTelefonoSolicitante(String telefonoSolicitante) { this.telefonoSolicitante = telefonoSolicitante; }
}


@RestController 
@RequestMapping("/api/turnos") 
@Tag(name = "API de Turnos", description = "Operaciones de la API para la gestión de turnos")
public class TurnoRestController {

    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private ITurnoService turnoService;

    @Operation(summary = "Obtener turnos disponibles por servicio y fecha",
               description = "Recupera una lista de horarios disponibles para reservar un turno, " +
                             "filtrados por el ID del servicio y una fecha específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de turnos disponibles recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Map.class))), 
            @ApiResponse(responseCode = "400", description = "Parámetros de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado o inactivo")
    })
    @GetMapping("/disponibles")
    public ResponseEntity<List<Map<String, Object>>> getTurnosDisponibles(
            @Parameter(description = "ID del servicio para el cual buscar turnos", required = true)
            @RequestParam Long servicioId,
            @Parameter(description = "Fecha en formato YYYY-MM-DD para buscar turnos", required = true, example = "2025-07-25")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        System.out.println("Buscando turnos para servicioId: " + servicioId + " y fecha: " + fecha);

        Servicio servicio = servicioService.findEntityById(servicioId);

        if (servicio == null || !servicio.isEstado()) {
            System.out.println("Servicio no encontrado o inactivo, retornando 404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }

        System.out.println("Servicio encontrado: " + servicio.getNombre());
        System.out.println("Estado del servicio: " + servicio.isEstado());
        System.out.println("Días habilitados en el servicio: " + servicio.getDiasSemana());

        DayOfWeek diaSemana = fecha.getDayOfWeek();
        System.out.println("Día de la semana para la fecha " + fecha + ": " + diaSemana);

        boolean operable = servicio.getDiasSemana() != null && servicio.getDiasSemana().contains(diaSemana);
        System.out.println("¿El servicio opera ese día? " + operable);

        if (!operable) {
            System.out.println("El servicio no opera ese día, retornando lista vacía");
            return ResponseEntity.ok(new ArrayList<>());
        }

        LocalDateTime fechaInicio = LocalDateTime.of(fecha, servicio.getHorarioInicio());
        LocalDateTime fechaFin = LocalDateTime.of(fecha, servicio.getHorarioFin());
        System.out.println("Buscando turnos entre: " + fechaInicio + " y " + fechaFin);

        List<Object[]> turnosOcupados = turnoRepository.buscarTurnosSimplificados(fechaInicio, fechaFin);
        System.out.println("Turnos ocupados encontrados: " + turnosOcupados.size());

        List<Map<String, Object>> turnosDisponibles = new ArrayList<>();
        LocalDateTime horarioActual = fechaInicio;

        while (horarioActual.plusMinutes(servicio.getDuracionMinutos()).isBefore(fechaFin) ||
                horarioActual.plusMinutes(servicio.getDuracionMinutos()).equals(fechaFin)) {
            final LocalDateTime horarioTurno = horarioActual;

            boolean estaOcupado = turnosOcupados.stream()
                    .anyMatch(t -> ((LocalDateTime)t[1]).equals(horarioTurno));

            if (!estaOcupado) {
                Map<String, Object> turnoDisponible = new HashMap<>();
                turnoDisponible.put("fechaHora", horarioTurno);
                turnoDisponible.put("servicioId", servicioId);
                turnosDisponibles.add(turnoDisponible);
                System.out.println("Agregando turno disponible: " + horarioTurno);
            } else {
                System.out.println("Horario ocupado: " + horarioTurno);
            }

            horarioActual = horarioActual.plusMinutes(servicio.getDuracionMinutos());
        }

        System.out.println("Total de turnos disponibles generados: " + turnosDisponibles.size());
        return ResponseEntity.ok(turnosDisponibles);
    }

    @Operation(summary = "Reservar un nuevo turno",
               description = "Permite a un solicitante reservar un turno disponible en el sistema. " +
                             "Requiere los detalles del turno y la información del solicitante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno reservado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TurnoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o turno no disponible"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la reserva")
    })
    @PostMapping
    public ResponseEntity<TurnoDTO> reservarTurno(
            @Parameter(description = "Datos de la reserva del turno, incluyendo fecha/hora, servicio/lugar y detalles del solicitante", required = true)
            @RequestBody TurnoBookingRequestDTO bookingRequest) {
        try {
            TurnoDTO turnoDTO = new TurnoDTO();
            turnoDTO.setFechaHora(bookingRequest.getFechaHora());
            turnoDTO.setServicioLugarId(bookingRequest.getServicioLugarId());
            turnoDTO.setEstado(true); 

            SolicitanteDTO solicitanteDTO = new SolicitanteDTO();
            solicitanteDTO.setNombre(bookingRequest.getNombreSolicitante());
            solicitanteDTO.setApellido(bookingRequest.getApellidoSolicitante());
            solicitanteDTO.setDni(bookingRequest.getDniSolicitante());
            solicitanteDTO.setEmail(bookingRequest.getEmailSolicitante());
            solicitanteDTO.setTelefono(bookingRequest.getTelefonoSolicitante());

            TurnoDTO turnoCreado = turnoService.crearTurnoConDni(turnoDTO, solicitanteDTO);

            return new ResponseEntity<>(turnoCreado, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

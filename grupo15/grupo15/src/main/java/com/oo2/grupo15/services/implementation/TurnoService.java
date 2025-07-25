package com.oo2.grupo15.services.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.oo2.grupo15.dtos.SolicitanteDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.repositories.IContactoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.entities.Solicitante;
import com.oo2.grupo15.entities.Turno;
import com.oo2.grupo15.repositories.IServicioLugarRepository;
import com.oo2.grupo15.repositories.ISolicitanteRepository;
import com.oo2.grupo15.repositories.ITurnoRepository;
import com.oo2.grupo15.services.IEmailService;
import com.oo2.grupo15.services.ITurnoService;

// Importaciones para ModelMapper y Records
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.record.RecordValueReader;


@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IServicioLugarRepository servicioLugarRepository;

    @Autowired
    private ISolicitanteRepository solicitanteRepository;
    @Autowired
    private IContactoRepository contactoRepository;
    
    @Autowired
    private IEmailService emailService;

    private final ModelMapper modelMapper; // Ahora es final y se inicializa en el constructor

    public TurnoService() {
        this.modelMapper = new ModelMapper();
        // Configurar ModelMapper para trabajar con Record Classes
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT) // O el que uses, STRICT es buena práctica
                .addValueReader(new RecordValueReader());
        
        this.modelMapper.createTypeMap(Turno.class, TurnoDTO.class)
        .setProvider(request -> {
            Turno source = (Turno) request.getSource();
            return new TurnoDTO(
                source.getId(),
                source.getFechaHora(),
                source.isEstado(),
                source.getServicioLugar() != null ? source.getServicioLugar().getId() : null,
                source.getSolicitante() != null ? source.getSolicitante().getId() : null
            );
        });
    }

    @Override
    public List<TurnoDTO> obtenerTurnosPorEmailSolicitante(String email) {
        // Buscamos todos los turnos del solicitante por su email
        List<Turno> turnos = turnoRepository.findAllBySolicitante_Email(email);

        // Convertimos la lista de entidades Turno a TurnoDTO
        return turnos.stream()
                .map(turno -> new TurnoDTO(
                        turno.getId(),
                        turno.getFechaHora(),
                        turno.isEstado(),
                        turno.getServicioLugar() != null ? turno.getServicioLugar().getId() : null,
                        turno.getSolicitante() != null ? turno.getSolicitante().getId() : null
                ))
                .collect(Collectors.toList());
    }

    
    @Override
    public TurnoDTO crearTurno(TurnoDTO dto) {
        Turno turno = new Turno();
        turno.setFechaHora(dto.fechaHora());
        turno.setEstado(dto.estado());

        if (dto.servicioLugarId() != null) {
            turno.setServicioLugar(servicioLugarRepository.findById(dto.servicioLugarId())
                    .orElseThrow(() -> new RuntimeException("ServicioLugar no encontrado")));
        }

        if (dto.solicitanteId() != null) {
            turno.setSolicitante(solicitanteRepository.findById(dto.solicitanteId())
                    .orElseThrow(() -> new RuntimeException("Solicitante no encontrado")));
        }

        Turno saved = turnoRepository.save(turno);
        
        try {
            Solicitante solicitante = turno.getSolicitante();
            String email = solicitante.getEmail();

            if (email != null && !email.isEmpty()) {
                org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
                context.setVariable("solicitante", solicitante);
                context.setVariable("servicioLugar", turno.getServicioLugar());
                context.setVariable("fechaHora", turno.getFechaHora());
                context.setVariable("profesional", turno.getServicioLugar().getProfesional());

                emailService.sendMailWithThymeleafTemplate(email, "Confirmación de Turno", "mail/turno-confirmacion.html", context);
            } else {
                System.out.println("El solicitante no tiene un email válido, no se envía correo.");
            }

        } catch (Exception e) {
            System.err.println("Error al intentar enviar el correo de confirmación: " + e.getMessage());
        }

        
        return modelMapper.map(saved, TurnoDTO.class);
    }

    public boolean tieneTurnosActivos(Long usuarioId) {
        return turnoRepository.countBySolicitanteIdAndEstadoActivo(usuarioId) > 0;
    }

    @Override
    public TurnoDTO actualizarTurno(Long id, TurnoDTO dto) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setFechaHora(dto.fechaHora());
        turno.setEstado(dto.estado());

        if (dto.servicioLugarId() != null) {
            turno.setServicioLugar(servicioLugarRepository.findById(dto.servicioLugarId())
                    .orElseThrow(() -> new RuntimeException("ServicioLugar no encontrado")));
        }

        if (dto.solicitanteId() != null) {
            turno.setSolicitante(solicitanteRepository.findById(dto.solicitanteId())
                    .orElseThrow(() -> new RuntimeException("Solicitante no encontrado")));
        }

        Turno updated = turnoRepository.save(turno);
        return modelMapper.map(updated, TurnoDTO.class);
    }

    @Override
    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public List<TurnoDTO> obtenerTodos() {
        return turnoRepository.findAll().stream()
                .map(turno -> modelMapper.map(turno, TurnoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TurnoDTO obtenerPorId(Long id) {
        return modelMapper.map(
                turnoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Turno no encontrado")),
                TurnoDTO.class);
    }

    @Override
    public List<TurnoDTO> obtenerTurnosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = turnoRepository.buscarTurnosSimplificados(fechaInicio, fechaFin);
        return resultados.stream()
                .map(resultado -> {
                    return new TurnoDTO(
                        (Long) resultado[0],
                        (LocalDateTime) resultado[1],
                        (Boolean) resultado[2],
                        null,
                        null
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public TurnoDTO crearTurnoConDni(TurnoDTO dto, SolicitanteDTO solicitanteDTO) {
        Solicitante solicitanteExistente = solicitanteRepository.findByContactoDni(solicitanteDTO.dni());

        if (solicitanteExistente != null) {
            System.out.println("Solicitante encontrado con DNI " + solicitanteDTO.dni() + ", ID: " + solicitanteExistente.getId());
            dto = new TurnoDTO(
                dto.id(),
                dto.fechaHora(),
                dto.estado(),
                dto.servicioLugarId(),
                solicitanteExistente.getId()
            );
        } else {
            System.out.println("No se encontró solicitante con DNI: " + solicitanteDTO.dni() + ". Creando nuevo solicitante...");

            try {
                Solicitante nuevoSolicitante = new Solicitante(false);

                nuevoSolicitante.setEmail(solicitanteDTO.email());
                nuevoSolicitante.setPassword("password_temporal");

                Contacto contacto = new Contacto();
                contacto.setNombre(solicitanteDTO.nombre());
                contacto.setApellido(solicitanteDTO.apellido());
                contacto.setDni(solicitanteDTO.dni());

                Contacto contactoGuardado = contactoRepository.save(contacto);

                nuevoSolicitante.setContacto(contactoGuardado);

                Solicitante saved = solicitanteRepository.save(nuevoSolicitante);
                System.out.println("Nuevo solicitante creado con ID: " + saved.getId());

                dto = new TurnoDTO(
                    dto.id(),
                    dto.fechaHora(),
                    dto.estado(),
                    dto.servicioLugarId(),
                    saved.getId()
                );
            } catch (Exception e) {
                System.err.println("Error al crear solicitante: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Creando turno con solicitanteId: " + dto.solicitanteId());

        return crearTurno(dto);
    }
}

package com.oo2.grupo15.controllers;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.services.IServicioService;
import com.oo2.grupo15.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

	@Autowired
	private IServicioService servicioService;

	@GetMapping
	public String index(Model model) {
		return ViewRouteHelper.SERVICIO_INDEX;
	}

	 @GetMapping("/todos")
	    public ModelAndView all(
	            @RequestParam(name = "nombre", required = false) String nombre,
	            @RequestParam(name = "estado", required = false) Boolean estado, 
	            @RequestParam(name = "duracion", required = false) Integer duracion
	    ) {
	        ModelAndView mAV = new ModelAndView(ViewRouteHelper.SERVICIO_ALL);
	        List<ServicioDTO> servicios;

	        if (nombre != null && !nombre.isEmpty()) {
	            servicios = servicioService.findByNombre(nombre);
	        } else if (estado != null) { 
	            servicios = servicioService.findByEstado(estado);
	        } else if (duracion != null) { 
	            servicios = servicioService.findByDuracionMinutos(duracion);
	        } else {
	            servicios = servicioService.getAll();
	        }

	        mAV.addObject("servicios", servicios);

	        mAV.addObject("filtroNombre", nombre);
	        mAV.addObject("filtroEstado", estado);
	        mAV.addObject("filtroDuracion", duracion);

	        return mAV;
	    }

	@GetMapping("/new")
	public String nuevo(Model model) {
		model.addAttribute("servicio", new ServicioDTO());
		model.addAttribute("diasSemana", Arrays.asList(DayOfWeek.values()));
		return ViewRouteHelper.SERVICIO_FORM;
	}

	@PostMapping("/save")
	public String save(@ModelAttribute("servicio") ServicioDTO dto,
			@RequestParam(value = "diasSemana", required = false) List<String> diasSeleccionados) {

		Set<DayOfWeek> dias = diasSeleccionados != null ? diasSeleccionados.stream().map(String::toUpperCase)
				.map(DayOfWeek::valueOf).collect(Collectors.toSet()) : Set.of();

		dto.setDiasSemana(dias);

		if (dto.getId() != null) {
			servicioService.update(dto.getId(), dto);
		} else {
			servicioService.save(dto);
		}

		return "redirect:/servicios";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable Long id, Model model) {
		ServicioDTO servicio = servicioService.findById(id);
		model.addAttribute("servicio", servicio);
		model.addAttribute("diasSemana", Arrays.asList(DayOfWeek.values()));
		return ViewRouteHelper.SERVICIO_FORM;
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable Long id) {
		servicioService.delete(id);
		return "redirect:/servicios";
	}

	// 🚀 ENDPOINTS API PARA JAVASCRIPT
	@GetMapping("/api")
	@ResponseBody
	public List<ServicioDTO> listarServicios() {
		return servicioService.findAll();
	}

	@GetMapping("/api/{id}")
	@ResponseBody
	public ServicioDTO buscarPorId(@PathVariable Long id) {
		return servicioService.findById(id);
	}

	@PostMapping("/api")
	@ResponseBody
	public ResponseEntity<ServicioDTO> guardarServicioDesdeAPI(@RequestBody ServicioDTO dto) {
		if (dto.getId() != null) {
			servicioService.update(dto.getId(), dto);
		} else {
			servicioService.save(dto);
		}
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/api/{id}")
	@ResponseBody
	public ResponseEntity<Void> eliminarDesdeAPI(@PathVariable Long id) {
		servicioService.delete(id);
		return ResponseEntity.ok().build();
	}

}

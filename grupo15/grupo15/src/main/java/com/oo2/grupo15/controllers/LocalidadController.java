package com.oo2.grupo15.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.dtos.LugarDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.ILocalidadService;
import com.oo2.grupo15.services.ILugarService;
import com.oo2.grupo15.services.IProvinciaService;

@Controller
@RequestMapping("/lugares")
public class LocalidadController {

	private ILocalidadService localidadService;
	private IProvinciaService provinciaService;
	private ILugarService lugarService;

	public LocalidadController(ILocalidadService localidadService, IProvinciaService provinciaService,
			ILugarService lugarService) {
		this.localidadService = localidadService;
		this.provinciaService = provinciaService;
		this.lugarService = lugarService;
	}

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LUGAR_INDEX);
		mAV.addObject("provincias", provinciaService.getAll());
		mAV.addObject("localidades", List.of());
		mAV.addObject("lugares", List.of());
		return mAV;
	}
	
    @GetMapping("/todos")
    public ModelAndView mostrarTodosLosLugares() {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.LUGAR_ALL); // Usar una constante para la vista
        List<LugarDTO> lstLugares = lugarService.getAll(); // Obtener todos los lugares
        mAV.addObject("lugares", lstLugares);
        return mAV;
    }

	@GetMapping("/porProvincia")
	@ResponseBody
	public List<LocalidadDTO> getLocalidadesPorProvincia(@RequestParam("provinciaId") int provinciaId) {
		List<LocalidadDTO> lstLocalidades = List.of();

		if (provinciaId != 0) {
			lstLocalidades = localidadService.getLocalidadesByProvincia(provinciaId);
		}

		return lstLocalidades;

	}

	@GetMapping("/porLocalidad")
	@ResponseBody
	public List<LugarDTO> getLugaresByLocalidad(@RequestParam("localidadId") int localidadId) {
		List<LugarDTO> lstLugares = List.of();

		if (localidadId != 0) {
			lstLugares = lugarService.getLugaresByLocalidad(localidadId);

		}
		return lstLugares;
	}
	

}

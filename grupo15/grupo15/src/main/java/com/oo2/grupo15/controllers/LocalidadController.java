package com.oo2.grupo15.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oo2.grupo15.dtos.DireccionDTO;
import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.IDireccionService;
import com.oo2.grupo15.services.ILocalidadService;
import com.oo2.grupo15.services.IProvinciaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/localidades")
public class LocalidadController {

	private ILocalidadService localidadService;
	private IProvinciaService provinciaService;
	private IDireccionService direccionService;
	
	public LocalidadController(ILocalidadService localidadService, IProvinciaService provinciaService,IDireccionService direccionService) {
		this.localidadService = localidadService;
		this.provinciaService = provinciaService;
		this.direccionService = direccionService;
	}

	@GetMapping("/")
    public ModelAndView index(){
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCALIDAD_INDEX);
		mAV.addObject("provincias", provinciaService.getAll()); 
        mAV.addObject("localidades", List.of());
        mAV.addObject("direcciones", List.of());
        mAV.addObject("localidad", new LocalidadDTO());
        return mAV;
    }
	
    @GetMapping("/porProvincia")
    @ResponseBody 
    public List<LocalidadDTO> getLocalidadesPorProvincia(@RequestParam("provinciaId") int provinciaId) {
        if (provinciaId == 0) { 
            return List.of(); 
        }
        return localidadService.getLocalidadesByProvincia(provinciaId);
    }
    
    @GetMapping("/porLocalidad")
    @ResponseBody 
    public List<DireccionDTO> getDireccionesByLocalidad(@RequestParam("localidadId") int localidadId) {
        if (localidadId == 0) { 
            return List.of(); 
        }
        return direccionService.getDireccionesByLocalidad(localidadId);
    }
    
    @DeleteMapping("/{id}") // Mapea a /localidades/{id} con método DELETE
    public ResponseEntity<String> deleteLocalidad(@PathVariable("id") int id) {
        boolean deleted = localidadService.delete(id);
        if (deleted) {
            return new ResponseEntity<>("Localidad eliminada exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al eliminar localidad o no encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@PostMapping("/")
	public RedirectView create(@ModelAttribute("localidad") LocalidadDTO localidadDTO) {
		localidadService.insertOrUpdate(localidadDTO);
		return new RedirectView(ViewRouteHelper.LOCALIDAD_ROOT);
	}
	

	@GetMapping("/form")
	public String localidad(Model model) {
		model.addAttribute("localidad", new LocalidadDTO());
		return ViewRouteHelper.LOCALIDAD_FORM;
	}

	@PostMapping("/new")
	public ModelAndView newlocalidad(@Valid @ModelAttribute("localidad") LocalidadDTO localidad, BindingResult bindingResult) {
		ModelAndView mV = new ModelAndView();
		if (bindingResult.hasErrors()) {
			mV.setViewName(ViewRouteHelper.LOCALIDAD_FORM);
		} else {
			mV.setViewName(ViewRouteHelper.LOCALIDAD_NEW);
			mV.addObject("localidad", localidad);
		}
		return mV;
	}
}

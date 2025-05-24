package com.oo2.grupo15.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.ILocalidadService;


import jakarta.validation.Valid;

@Controller
@RequestMapping("/localidades")
public class LocalidadController {

	private ILocalidadService localidadService;
	
	public LocalidadController(ILocalidadService localidadService) {
		this.localidadService = localidadService;
	}

	@GetMapping("/")
    public ModelAndView index(){
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCALIDAD_INDEX);
		System.out.println("Localidades encontradas: " + localidadService.getAll().size());
        mAV.addObject("localidades", localidadService.getAll());
        mAV.addObject("localidad", new LocalidadDTO());
        return mAV;
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

package com.oo2.grupo15.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.IServicioService;

@Controller 
@RequestMapping("/servicios")
public class ServicioController {
	private IServicioService servicioService;

	public ServicioController(IServicioService servicioService) {
		super();
		this.servicioService = servicioService;
	}
	
	@GetMapping("/")
	public ModelAndView all() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.SERVICIO_ALL);
		mAV.addObject("servicios", servicioService.getAll());
		return mAV;
	}
}

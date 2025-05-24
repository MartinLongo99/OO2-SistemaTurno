package com.oo2.grupo15.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.implementation.LocalidadService;

@Controller
@RequestMapping("/localidades")
public class LocalidadController {

	@Autowired
	private LocalidadService localidadService;

	@GetMapping("/")
    public ModelAndView index(){
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCALIDAD_INDEX);
        mAV.addObject("localidades", localidadService.getAll());
        mAV.addObject("localidad", new LocalidadDTO());
        return mAV;
    }
}

package com.oo2.grupo15.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oo2.grupo15.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping("/index")
	public ModelAndView index(Authentication authentication) {
		ModelAndView mV = new ModelAndView(ViewRouteHelper.INDEX);
		// Agregar información sobre el rol a la vista
		if (authentication != null) {
			boolean isAdmin = authentication.getAuthorities()
					.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
			mV.addObject("isAdmin", isAdmin);
			mV.addObject("username", authentication.getName());
		}
		return mV;
	}

	@GetMapping("/hello")
	public ModelAndView helloParams1(
			@RequestParam(name = "name", required = false, defaultValue = "null") String name,
			Authentication authentication) {
		ModelAndView mV = new ModelAndView(ViewRouteHelper.HELLO);
		mV.addObject("name", name);

		// Agregar información sobre el rol a la vista
		if (authentication != null) {
			boolean isAdmin = authentication.getAuthorities()
					.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
			mV.addObject("isAdmin", isAdmin);
			mV.addObject("username", authentication.getName());
		}

		return mV;
	}

	@GetMapping("/hello/{name}")
	public ModelAndView helloParams2(
			@PathVariable("name") String name,
			Authentication authentication) {
		ModelAndView mV = new ModelAndView(ViewRouteHelper.HELLO);
		mV.addObject("name", name);

		// Agregar información sobre el rol a la vista
		if (authentication != null) {
			boolean isAdmin = authentication.getAuthorities()
					.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
			mV.addObject("isAdmin", isAdmin);
			mV.addObject("username", authentication.getName());
		}

		return mV;
	}

	@GetMapping("/")
	public RedirectView redirectToHomeIndex(){
		return new RedirectView(ViewRouteHelper.ROUTE_INDEX);
	}
}
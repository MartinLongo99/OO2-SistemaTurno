package com.oo2.grupo15.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/firstSpring")
public class FirstSpringController {

	@GetMapping("helloworld")
	public String helloWorld() {
		return "hello";
	}
}

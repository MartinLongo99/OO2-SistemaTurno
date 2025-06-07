package com.oo2.grupo15.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailContentBuilder {

	@Autowired
	private SpringTemplateEngine emailTemplateEngine; // Inyecta el motor de plantillas de correo

	public String build(String templateName, Context context) {
		return emailTemplateEngine.process(templateName, context);
	}
}

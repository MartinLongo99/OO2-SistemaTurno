package com.oo2.grupo15.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafMailConfig {

	@Bean
	@Description("Thymeleaf Template Resolver para Email")
	public ITemplateResolver emailTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("mail/"); // Carpeta donde estarán tus plantillas de correo
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setOrder(1); // Importante si tienes múltiples template resolvers
		templateResolver.setCheckExistence(true); // Verifica si la plantilla existe
		templateResolver.setCacheable(false); // Deshabilita el cache para desarrollo, habilita para prod
		return templateResolver;
	}

	@Bean
	@Description("Thymeleaf Template Engine para Email")
	public SpringTemplateEngine emailTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(emailTemplateResolver());
		// Opcional: Si necesitas mensajes de internacionalización en tus plantillas de
		// correo
		// templateEngine.setTemplateEngineMessageSource(emailMessageSource());
		return templateEngine;
	}

	// Opcional: Si necesitas mensajes de internacionalización para tus plantillas
	// de correo
	@Bean
	public ResourceBundleMessageSource emailMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("mailMessages"); // Archivo de propiedades para mensajes (ej. mailMessages.properties)
		return messageSource;
	}
}
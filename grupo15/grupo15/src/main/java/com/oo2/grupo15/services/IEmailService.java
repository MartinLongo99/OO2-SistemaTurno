package com.oo2.grupo15.services;

import org.thymeleaf.context.Context;

public interface IEmailService {
	
	void sendSimpleMail(String to, String subject, String text);

	void sendHtmlMail(String to, String subject, String htmlBody);
	
	void sendMailWithThymeleafTemplate(String to, String subject, String templateName, Context context);
}

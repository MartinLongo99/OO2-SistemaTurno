package com.oo2.grupo15.services;

public interface IEmailService {
	
	void sendSimpleMail(String to, String subject, String text);

	void sendHtmlMail(String to, String subject, String htmlBody);
}

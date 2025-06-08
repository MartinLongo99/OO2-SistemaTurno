package com.oo2.grupo15.services.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import com.oo2.grupo15.services.IEmailService;

@Service
public class EmailService implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private EmailContentBuilder emailContentBuilder; // Inyecta el constructor de contenido de correo

	@Override
	public void sendSimpleMail(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("sistemadeturnos15@gmail.com");
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			mailSender.send(message);
			System.out.println("Correo simple enviado a: " + to);
		} catch (MailException e) {
			System.err.println("Error al enviar correo simple: " + e.getMessage());
		}
	}

	@Override
	public void sendHtmlMail(String to, String subject, String htmlBody) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom("sistemadeturnos15@gmail.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlBody, true);

			mailSender.send(message);
			System.out.println("Correo HTML enviado a: " + to);
		} catch (MessagingException | MailException e) {
			System.err.println("Error al enviar correo HTML: " + e.getMessage());
		}
	}

	public void sendMailWithThymeleafTemplate(String to, String subject, String templateName, Context context) {
		String htmlBody = emailContentBuilder.build(templateName, context);
		sendHtmlMail(to, subject, htmlBody);
	}
}
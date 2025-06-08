package com.oo2.grupo15.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

	 @Bean
	    public JavaMailSender javaMailSender() {
	        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	        mailSender.setHost("smtp.gmail.com");
	        mailSender.setPort(587);
	        mailSender.setUsername(System.getenv("USER_EMAIL"));
	        mailSender.setPassword(System.getenv("PASSWORD_EMAIL"));


	        Properties props = mailSender.getJavaMailProperties();
	        props.put("mail.smtp.auth", true);
	        props.put("mail.smtp.starttls.enable", true);
	        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

	        return mailSender;
	    }
}

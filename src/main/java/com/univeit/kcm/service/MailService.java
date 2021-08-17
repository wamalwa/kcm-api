package com.univeit.kcm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	private JavaMailSender jMailSender;
	
	@Autowired
	public MailService(JavaMailSender jMailSender) {
		this.jMailSender = jMailSender;
	}
	
	public void sendEmail(String mailTo, String mailSubject, String mailText) throws MailException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("KCM Accounts <accounts.kcm@univeit.com>");
		mail.setTo(mailTo);
		mail.setSubject(mailSubject);
		mail.setText(mailText);
		
		jMailSender.send(mail);
	}
}

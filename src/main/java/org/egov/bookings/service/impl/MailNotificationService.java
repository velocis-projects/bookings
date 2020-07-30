package org.egov.bookings.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService 
{
	/** The sender. */
	@Autowired									
	JavaMailSender sender;
	
	public void sendMail(String emailId, String emailMessage, String emailSubject) 
	{
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try 
		{
			helper.setFrom("075sssamit@gmail.com");
			helper.setTo(emailId);
			helper.setText(emailMessage);
			helper.setSubject(emailSubject);
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
		}
		sender.send(message);
	}

}

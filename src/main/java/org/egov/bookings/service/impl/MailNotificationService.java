package org.egov.bookings.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailNotificationService 
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(MailNotificationService.class.getName());
	
	/** The sender. */
	@Autowired									
	JavaMailSender sender;
	
	public void sendMail(String emailId, String emailMessage, String emailSubject) 
	{
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		if(BookingsFieldsValidator.isNullOrEmpty(emailId))
		{
			throw new IllegalArgumentException("Invalid emailId");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(emailMessage))
		{
			throw new IllegalArgumentException("Invalid emailMessage");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(emailSubject))
		{
			throw new IllegalArgumentException("Invalid emailSubject");
		}
		try 
		{
			helper.setFrom("ck066114@gmail.com");
			helper.setTo(emailId);
			helper.setText(emailMessage);
			helper.setSubject(emailSubject);
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();
			LOGGER.error("Exception occur during sendMail " + e);
		}
		sender.send(message);
	}

}

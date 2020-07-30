package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.springframework.stereotype.Service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;

/**
 * The Class SMSNotificationService.
 */
@Service
public class SMSNotificationService {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(SMSNotificationService.class.getName());
	
	/** The Constant ACCOUNT_SID. */
	public static final String ACCOUNT_SID = "AC1972d9d3dc8c2988a1e3b12c2a182d02";
	
	/** The Constant AUTH_TOKEN. */
	public static final String AUTH_TOKEN = "915c417c4efeb87eaa75a0d67c1f4d52";
	
	/** The Constant TWILIO_NUMBER. */
	public static final String TWILIO_NUMBER = "+17609907083";
	
	/** The Constant USER_NUMBER. */
	public static final String USER_NUMBER = "+918115566943";
	
	/** The Constant BODY. */
	public static final String BODY = "Body";
	
	/** The Constant TO. */
	public static final String TO = "To";
	
	/** The Constant FROM. */
	public static final String FROM = "From";
	
	/**
	 * Send SMS.
	 *
	 * @param notificationMsg the notification msg
	 */
	public void sendSMS( String notificationMsg )
	{
		if(BookingsFieldsValidator.isNullOrEmpty(notificationMsg))
		{
			throw new IllegalArgumentException("Invalid notificationMsg");
		}
		try
		{
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(BODY, notificationMsg));
			params.add(new BasicNameValuePair(TO, USER_NUMBER));
			params.add(new BasicNameValuePair(FROM, TWILIO_NUMBER));
			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			messageFactory.create(params);
		}
		catch ( TwilioRestException e )
		{
			LOGGER.error("Exception occur during sendSMS " + e);
		}
	}
}

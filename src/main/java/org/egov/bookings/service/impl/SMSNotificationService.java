package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.egov.bookings.model.BookingsModel;
import org.springframework.stereotype.Service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

@Service
public class SMSNotificationService {
	
	public static final String ACCOUNT_SID = "AC1972d9d3dc8c2988a1e3b12c2a182d02";
	public static final String AUTH_TOKEN = "d6a43beb5502a7706ed4346e4c95614f";
	public static final String TWILIO_NUMBER = "+17609907083";
	public static final String USER_NUMBER = "+918115566943";
	public static final String BODY = "Body";
	public static final String TO = "To";
	public static final String FROM = "From";
	
	
	
	
	public String sendSMS( String notificationMsg )
	{
		try
		{
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(BODY, notificationMsg));
			params.add(new BasicNameValuePair(TO, USER_NUMBER));
			params.add(new BasicNameValuePair(FROM, TWILIO_NUMBER));
			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message message = messageFactory.create(params);
			System.out.println( message.getSid() );
		}
		catch ( TwilioRestException e )
		{
			System.out.println( e.getErrorMessage() );
		}
		return null;
	}
}

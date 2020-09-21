package org.egov.bookings.utils;

import org.egov.bookings.config.BookingsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CalculationUtils {

	@Autowired
	private BookingsConfiguration config;
	
	  public String getBillGenerateURI(){
	        StringBuilder url = new StringBuilder(config.getBillingHost());
	        url.append(config.getBillGenerateEndpoint());
	        url.append("?");
	        url.append("tenantId=");
	        url.append("{1}");
	        url.append("&");
	        url.append("consumerCode=");
	        url.append("{2}");
	        url.append("&");
	        url.append("businessService=");
	        url.append("{3}");

	        return url.toString();
	    }
	
	
}

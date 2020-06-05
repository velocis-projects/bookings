package org.egov.bookings.validator;

import org.springframework.stereotype.Component;

@Component
public class BookingsFieldsValidator {

	public void validateTenantId(String tenantId) {
		if(tenantId == null || tenantId == "")
		 throw new IllegalArgumentException("Invalid TenantId");
	}
	
	
	public void validateBusinessService(String businessService) {
		if(businessService == null || businessService == "")
			 throw new IllegalArgumentException("Invalid businessService");
	}
	
	public void validateAction(String action) {
		if(action == null || action == "")
			 throw new IllegalArgumentException("Invalid Action");
	}
}

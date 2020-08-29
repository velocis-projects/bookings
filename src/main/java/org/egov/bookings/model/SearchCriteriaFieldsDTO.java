package org.egov.bookings.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new booking search criteria fields.
 */
@NoArgsConstructor

/**
 * Instantiates a new booking search criteria fields.
 *
 * @param tenantId the tenant id
 * @param applicationNumber the application number
 * @param fromDate the from date
 * @param toDate the to date
 * @param mobileNumber the mobile number
 * @param status the status
 */
@AllArgsConstructor
public class SearchCriteriaFieldsDTO 
{
	/** The tenant id. */
	private String tenantId;
	
	/** The application number. */
	private String applicationNumber;
	
	/** The from date. */
	private Date fromDate;
	
	/** The to date. */
	private Date toDate;
	
	/** The mobile number. */
	private String mobileNumber;
	
	/** The status. */
	private boolean status;
}

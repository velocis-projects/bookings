package org.egov.bookings.contract;

import java.io.Serializable;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * Instantiates a new availability response.
 *
 * @param fromDate the from date
 * @param toDate the to date
 */
@AllArgsConstructor

/**
 * Instantiates a new availability response.
 */
@NoArgsConstructor

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
@Builder
public class AvailabilityResponse implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3683257254333784296L;
	
	/** The from date. */
	private Date fromDate;
	
	/** The to date. */
	private Date toDate;
	
	
}

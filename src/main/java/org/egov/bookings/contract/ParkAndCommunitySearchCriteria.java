package org.egov.bookings.contract;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkAndCommunitySearchCriteria implements Serializable{

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The booking type. */
	private String bookingType;

	/** The booking venue. */
	private String bookingVenue;

	private String fromDate;

	private String toDate;

}

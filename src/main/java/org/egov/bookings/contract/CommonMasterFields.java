package org.egov.bookings.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMasterFields {
	
	/** The id. */
	private String id;
	
	/** The sector. */
	private String sector;
	
	/** The uuid. */
	private String uuid;
	
	/** The last modified date. */
	private String lastModifiedDate;
	
	/** The created date. */
	private String createdDate;
	
	/** The role code. */
	private String roleCode;
	
	/** The village city. */
	private String villageCity;

	/** The residential commercial. */
	private String residentialCommercial;

	/** The storage. */
	private String storage;

	/** The duration in months. */
	private String durationInMonths;

	/** The construction type. */
	private String constructionType;

	/** The amount. */
	private Long amount;
	
	/** The slab. */
	private String slab;
	
	/** The area from. */
	private Long areaFrom;
	
	/** The area to. */
	private Long areaTo;
	
	/** The rate per sqr feet per day. */
	private Long ratePerSqrFeetPerDay;
	
	/** The locality. */
	private String locality;
	
	/** The category. */
	private String category;
	
	/** The rate per day. */
	private Long ratePerDay;
	
	/** The booking venue. */
	private String bookingVenue;
}

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
	
	private String id;
	
	private String sector;
	
	private String uuid;
	
	private String lastModifiedDate;
	
	private String createdDate;
	
	private String roleCode;
	
	private String villageCity;

	private String residentialCommercial;

	private String storage;

	private String durationInMonths;

	private String constructionType;

	private Long amount;
	
	private String slab;
	
	private Long areaFrom;
	
	private Long areaTo;
	
	private Long ratePerSqrFeetPerDay;
}

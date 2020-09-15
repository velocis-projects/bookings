package org.egov.bookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CommercialGroundFeeModel")
@Table(name = "TM_COMMERCIAL_GROUND_FEE")
public class CommercialGroundFeeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private String id;
	
	@Column(name = "LOCALITY")
	private String locality;
	
	@Column(name = "CATEGORY")
	private String category;
	
	@Column(name = "RATE_PER_DAY")
	private Long ratePerDay;
	
	@Column(name = "BOOKING_VENUE")
	private String bookingVenue;
}

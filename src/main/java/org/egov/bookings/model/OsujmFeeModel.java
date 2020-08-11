package org.egov.bookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "OsujmFeeModel")
@Table(name = "TT_OSUJM_FEE")
public class OsujmFeeModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "SECTOR")
	private String sector;

	@Column(name = "SLAB")
	private String slab;
	
	@Column(name = "AREA")
	private String area;
	
	@Column(name = "RATE_PER_SQR_FEET_PER_DAY")
	private String ratePerSqrFeetPerDay;
	
}

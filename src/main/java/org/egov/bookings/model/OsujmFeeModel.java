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
@Table(name = "TM_OSUJM_FEE")
public class OsujmFeeModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "SECTOR")
	private String sector;

	@Column(name = "SLAB")
	private String slab;
	
	@Column(name = "AREA_FROM")
	private Long areaFrom;
	
	@Column(name = "AREA_TO")
	private Long areaTo;
	
	@Column(name = "RATE_PER_SQR_FEET_PER_DAY")
	private Long ratePerSqrFeetPerDay;
	
}

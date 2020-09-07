package org.egov.bookings.model;

import java.util.Date;

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
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Long id;
	
	/** The sector. */
	@Column(name = "SECTOR")
	private String sector;

	/** The slab. */
	@Column(name = "SLAB")
	private String slab;
	
	/** The area from. */
	@Column(name = "AREA_FROM")
	private Long areaFrom;
	
	/** The area to. */
	@Column(name = "AREA_TO")
	private Long areaTo;
	
	/** The rate per sqr feet per day. */
	@Column(name = "RATE_PER_SQR_FEET_PER_DAY")
	private Long ratePerSqrFeetPerDay;
	
	/** The last modified date. */
	@Column(name = "LAST_MODIFIED_DATE")
	private Date lastModifiedDate;
	
	/** The created date. */
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
}

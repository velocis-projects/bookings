package org.egov.bookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ParkCommunityHallV1MasterModel")
@Table(name = "TM_PARK_COMMUNITY_HALL_v1")
public class ParkCommunityHallV1MasterModel {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "SCCID")
	private String SCCID;

	@Column(name = "SCID")
	private String SCID;

	@Column(name = "X")
	private String X;

	@Column(name = "Y")
	private String Y;

	@Column(name = "Amount")
	private String Amount;

	@Column(name = "DimensionSqrYards")
	private String DimensionSqrYards;

	@Column(name = "Rent")
	private String Rent;

	@Column(name = "CleaningCharges")
	private String CleaningCharges;

	@Column(name = "surcharge")
	private String surcharge;

	@Column(name = "LuxuryTax")
	private String LuxuryTax;

	@Column(name = "Name")
	private String Name;

	@Column(name = "Radius")
	private String Radius;

	@Column(name = "LocationChangeAmount")
	private String LocationChangeAmount;

	@Column(name = "Isactive")
	private Boolean Isactive;

	@Column(name = "UTGSTRate")
	private String UTGSTRate;

	@Column(name = "CGSTRate")
	private String CGSTRate;

	@Column(name = "RefundabelSecurity")
	private String RefundabelSecurity;

	@Column(name = "NormalType")
	private String NormalType;

	@Column(name = "Reviserate1")
	private String Reviserate1;

	@Column(name = "Oldrent1")
	private String Oldrent1;

	@Column(name = "RentNextSession")
	private String RentNextSession;

	@Column(name = "ImagePath")
	private String ImagePath;

	@Column(name = "VenueType")
	private String VenueType;

	@Column(name = "BookingAllowedFor")
	private String BookingAllowedFor;

}

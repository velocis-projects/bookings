package org.egov.bookings.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
@Entity(name = "Bookings")
@Table(name = "TP_BOOKINGS")
public class BookingsModel {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BK_BOOKING_ID")
	private Long bkBookingId;
	
	@Column(name = "BK_HOUSE_SITE_NO")
	private String bkHouseSiteNo;
	
	@Column(name = "BK_ADDRESS")
	private String bkAddress;
	
	@Column(name = "BK_SECTOR")
	private String bkSector;
	
	@Column(name = "BK_VILL_CITY")
	private String bkVillCity;

	@Column(name = "BK_AREA_REQUIRED")
	private String bkAreaRequired;
	
	@Column(name = "BK_DURATION")
	private String bkDuration;
	
	@Column(name = "BK_CATEGORY")
	private String bkCategory;
	
	
	@Column(name = "BK_EMAIL")
	private String bkEmail;
	
	@Column(name = "BK_CONTACT_NO")
	private String bkContactNo;
	
	@Column(name = "BK_DOCUMENT_UPLOADED_URL")
	private String bkDocumentUploadedUrl;
	
	@Column(name = "BK_DATE_CREATED")
	private Date bkDateCreated;
	
	@Column(name = "BK_CREATED_BY")
	private Long bkCreatedBy;
	
	@Column(name = "BK_WF_STATUS")
	private String bkWfStatus;
	
	@Column(name = "BK_AMOUNT")
	private String bkAmount;
	
	@Column(name = "BK_PAYMENT_STATUS")
	private String bkPaymentStatus;
	
	@Column(name = "BK_PAYMENT_DATE")
	private Date bkPaymentDate;
	
	@Column(name = "BK_BOOKING_TYPE")
	private String bkBookingType;
	
}

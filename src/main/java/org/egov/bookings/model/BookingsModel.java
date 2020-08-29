package org.egov.bookings.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity(name = "BookingsModel")
@Table(name = "TT_BOOKINGS")
public class BookingsModel {

	@Id
	@Column(name = "BK_APPLICATION_NUMBER")
	private String bkApplicationNumber;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "BK_APPLICATION_NUMBER", referencedColumnName = "BK_APPLICATION_NUMBER")
	private List<BookingsRemarks> bookingsRemarks;

	@Column(name = "BK_HOUSE_NO")
	private String bkHouseNo;

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

	@Column(name = "BK_FROM_DATE")
	private Date bkFromDate;

	@Column(name = "BK_TO_DATE")
	private Date bkToDate;

	@Column(name = "BK_APPLICANT_NAME")
	private String bkApplicantName;

	@Column(name = "BK_BOOKING_PURPOSE")
	private String bkBookingPurpose;

	@Column(name = "BK_VILLAGE")
	private String bkVillage;

	@Column(name = "BK_DIMENSION")
	private String bkDimension;

	@Column(name = "BK_LOCATION")
	private String bkLocation;

	@Column(name = "BK_STARTING_DATE")
	private Date bkStartingDate;

	@Column(name = "BK_ENDING_DATE")
	private Date bkEndingDate;

	@Column(name = "BK_TYPE")
	private String bkType;

	@Column(name = "BK_RESIDENCE_PROOF")
	private String bkResidenceProof;

	@Column(name = "BK_CLEANSING_CHARGES")
	private String bkCleansingCharges;

	@Column(name = "BK_RENT")
	private String bkRent;

	@Column(name = "BK_SURCHARGE_RENT")
	private String bkSurchargeRent;

	@Column(name = "BK_FACILITATION_CHARGES")
	private String bkFacilitationCharges;

	@Column(name = "BK_UTGST")
	private String bkUtgst;

	@Column(name = "BK_CGST")
	private String bkCgst;

	@Column(name = "BK_MOBILE_NUMBER")
	private String bkMobileNumber;

	@Column(name = "BK_CUSTOMER_GST_NO")
	private String bkCustomerGstNo;

	@Column(name = "BK_CURRENT_CHARGES")
	private String bkCurrentCharges;

	@Column(name = "BK_LOCATION_CHANGE_AMOUNT")
	private String bkLocationChangeAmount;

	@Column(name = "BK_VENUE")
	private String bkVenue;

	@Column(name = "BK_DATE")
	private Date bkDate;

	@Column(name = "BK_FATHER_NAME")
	private String bkFatherName;

	@Column(name = "BK_BOOKING_VENUE")
	private String bkBookingVenue;

	@Column(name = "BK_BOOKING_DURATION")
	private String bkBookingDuration;

	@Column(name = "BK_ID_PROOF")
	private String bkIdProof;

	@Column(name = "BK_APPLICANT_CONTACT")
	private String bkApplicantContact;

	@Column(name = "BK_OPEN_SPACE_LOCATION")
	private String bkOpenSpaceLocation;

	@Column(name = "BK_LANDMARK")
	private String bkLandmark;

	@Column(name = "BK_REQUIREMENT_AREA")
	private String bkRequirementArea;

	@Column(name = "BK_LOCATION_PICTURES")
	private String bkLocationPictures;

	@Column(name = "BK_BOOKING_REFERENCE_NUMBER")
	private String bkBookingReferenceNumber;

	@Column(name = "BK_PAYMENT_RECEIPT_NUMBER")
	private String bkPaymentReceiptNumber;

	@Column(name = "BK_PARK_OR_COMMUNITY_CENTER")
	private String bkParkOrCommunityCenter;

	@Column(name = "BK_REFUND_AMOUNT")
	private String bkRefundAmount;

	@Column(name = "BK_BANK_ACCOUNT_NUMBER")
	private String bkBankAccountNumber;

	@Column(name = "BK_BANK_NAME")
	private String bkBankName;

	@Column(name = "BK_IFSC_CODE")
	private String bkIfscCode;

	@Column(name = "BK_ACCOUNT_TYPE")
	private String bkAccountType;

	@Column(name = "BK_PROPERTY_OWNER_NAME")
	private String bkPropertyOwnerName;

	@Column(name = "BK_COMPLETE_ADDRESS")
	private String bkCompleteAddress;

	@Column(name = "BK_RESIDENTIAL_OR_COMMERCIAL")
	private String bkResidentialOrCommercial;

	@Column(name = "BK_MATERIAL_STORAGE_AREA")
	private String bkMaterialStorageArea;

	@Column(name = "BK_PLOT_SKETCH")
	private String bkPlotSketch;

	@Column(name = "BK_APPLICATION_STATUS")
	private String bkApplicationStatus;

	@Column(name = "BK_TIME")
	private String bkTime;

	@Column(name = "BK_STATUS_UPDATE_REQUEST")
	private String bkStatusUpdateRequest;

	@Column(name = "BK_STATUS")
	private String bkStatus;

	@Column(name = "BK_DRIVER_NAME")
	private String bkDriverName;

	@Column(name = "BK_VEHICLE_NUMBER")
	private String bkVehicleNumber;

	@Column(name = "BK_ESTIMATED_DELIVERY_TIME")
	private String bkEstimatedDeliveryTime;

	@Column(name = "BK_ACTUAL_DELIVERY_TIME")
	private String bkActualDeliveryTime;

	@Column(name = "BK_NORMAL_WATER_FAILURE_REQUEST")
	private String bkNormalWaterFailureRequest;

	@Column(name = "BK_UPDATE_STATUS_OPTION")
	private String bkUpdateStatusOption;

	@Column(name = "BK_ADD_SPECIAL_REQUEST_DETAILS")
	private String bkAddSpecialRequestDetails;

	@Column(name = "BK_BOOKING_TIME")
	private String bkBookingTime;

	@Column(name = "BK_APPROVED_BY")
	private String bkApprovedBy;

	@Column(name = "BK_MODULE_TYPE")
	private String bkModuleType;

	@Column(name = "UUID")
	private String uuid;

	
	@Column(name = "TENANT_ID")
	private String tenantId;

	
	@Column(name = "BK_ACTION")
	private String bkAction;

	@Column(name = "BK_CONSTRUCTION_TYPE")
	private String bkConstructionType;
	
	@Column(name = "BUSINESS_SERVICE")
	private String businessService;
	
	@Column(name = "APPROVER_NAME")
	private String bkApproverName;

	@Transient
	private BigDecimal discount;
	
	@Size(max = 64)
	@JsonProperty("assignee")
	@Transient
	private String assignee = null;

	@Valid
	@JsonProperty("wfDocuments")
	@Transient
	private List<Document> wfDocuments;
	
	
    @Size(max=64)
    @JsonProperty("financialYear")
    private String financialYear = null;
	

}

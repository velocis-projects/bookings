package org.egov.bookings.validator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.contract.OsbmSearchCriteria;
import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.model.ViewPdfDetailsModel;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.stereotype.Component;

@Component
public class BookingsFieldsValidator {

	public void validateTenantId(String tenantId) {
		if(tenantId == null || tenantId == "")
		 throw new IllegalArgumentException("Invalid TenantId");
	}
	
	
	public void validateBusinessService(String businessService) {
		if(businessService == null || businessService == "")
			 throw new IllegalArgumentException("Invalid businessService");
	}
	
	public void validateAction(String action) {
		if(action == null || action == "")
			 throw new IllegalArgumentException("Invalid Action");
	}
	
	
	
	public static boolean isNullOrEmpty( final Object object )
	   {
	      if ( object == null )
	         return true;
	      if ( object instanceof String )
	         return ( ( String ) object ).length() == 0;
	      if ( object instanceof Collection )
	         return ( ( Collection< ? > ) object ).isEmpty();
	      if ( object instanceof Map )
	         return ( ( Map< ?, ? > ) object ).isEmpty();
	      if ( object.getClass().isArray() )
	      {
	         if( Array.getLength( object ) == 0 ) 
	         {
	            return true;
	         } 
	         else 
	         {
	            // test 1st dim array
	            for( int i = 0; i < Array.getLength( object ); i++ ) 
	            {
	               if( Array.get( object, i ) != null ) 
	               {
	                  // check if 2 dim array
	                  if( Array.get( object, i ).getClass().isArray() ) {
	                     if( Array.getLength( Array.get( object, i ) ) == 0 ) 
	                     {
	                        return true;
	                     }
	                     for( int j = 0; j < Array.getLength( Array.get( object, i ) ); j++ ) {
	                        if( Array.get( Array.get( object, j ), i ) != null ) 
	                        {
	                           // means found at least one data not null
	                           return false;
	                        }
	                     }
	                     // means all data of a row are null
	                     return true;
	                  } 
	                  else 
	                  {
	                     // means 1 dim array and one data not null
	                     return false;
	                  }
	               }
	            }
	            // all data are null
	            return true;
	         }
	      }
	      return false;
	   }


	/**
	 * Validate osbm search criteria.
	 *
	 * @param osbmSearchCriteria the osbm search criteria
	 */
	public void validateOsbmSearchCriteria(OsbmSearchCriteria osbmSearchCriteria) {
		
		if(null == osbmSearchCriteria) {
			throw new IllegalArgumentException("Invalid Search Criteria Field");
		}
		else if(null == osbmSearchCriteria.getConstructionType() || osbmSearchCriteria.getConstructionType() == "") {
			throw new IllegalArgumentException("Invalid constructionType");
		}
		
		else if(null == osbmSearchCriteria.getDurationInMonths() || osbmSearchCriteria.getDurationInMonths() == "") {
			throw new IllegalArgumentException("Invalid durationInMonths");
		}
		
		else if(null == osbmSearchCriteria.getResidentialCommercial() || osbmSearchCriteria.getResidentialCommercial() == "") {
			throw new IllegalArgumentException("Invalid residentialCommercial");
		}
		
		
		else if(null == osbmSearchCriteria.getStorage() || osbmSearchCriteria.getStorage() == "") {
			throw new IllegalArgumentException("Invalid getStorage");
		}
		
		else if(null == osbmSearchCriteria.getVillageCity() || osbmSearchCriteria.getVillageCity() == "") {
			throw new IllegalArgumentException("Invalid villageCity");
		}
		
	}


	public void validateCommercialGroundCriteria(CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria) {
		if(null == commercialGroundFeeSearchCriteria) {
			throw new IllegalArgumentException("Invalid Commercial Ground Fee Search Criteria");
		}
		else if(null == commercialGroundFeeSearchCriteria.getCategory() || commercialGroundFeeSearchCriteria.getCategory() == "") {
			throw new IllegalArgumentException("Invalid Category");
		}
		
		else if(null == commercialGroundFeeSearchCriteria.getBookingVenue() || commercialGroundFeeSearchCriteria.getBookingVenue() == "") {
			throw new IllegalArgumentException("Invalid Booking Venue");
		}
	}


	/**
	 * Validate commercial ground availability criteria.
	 *
	 * @param commercialGroundAvailabiltySearchCriteria the commercial ground availabilty search criteria
	 */
	public void validateCommercialGroundAvailabilityCriteria(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria) {
		if(null == commercialGroundAvailabiltySearchCriteria) {
			throw new IllegalArgumentException("Invalid Commercial Ground Availability Search Criteria");
		}
		else if(null == commercialGroundAvailabiltySearchCriteria.getBookingVenue()) {
			throw new IllegalArgumentException("Invalid Commercial Ground Booking Venue");
		}
	}


	/**
	 * Validate osbm approver body.
	 *
	 * @param osbmApproverRequest the osbm approver request
	 */
	public void validateOsbmApproverBody(OsbmApproverRequest osbmApproverRequest) {
		if(null == osbmApproverRequest) {
			throw new IllegalArgumentException("Invalid Osbm Approver Request Body");
		}
		else if(null == osbmApproverRequest.getSector() || osbmApproverRequest.getSector().equals("")) {
			throw new IllegalArgumentException("Invalid Sector");
		}
		else if(null == osbmApproverRequest.getUuid() || osbmApproverRequest.getUuid().equals("")) {
			throw new IllegalArgumentException("Invalid Uuid");
		}
	}


	public void validatePdfDetails(ViewPdfDetailsModel viewPdfDetailsModel) {
		
		if(null == viewPdfDetailsModel) {
			throw new IllegalArgumentException("Invalid Pdf Details Request Body");
		}
		else if(null == viewPdfDetailsModel.getBkApplicationNumber() || viewPdfDetailsModel.getBkApplicationNumber().equals("")) {
			throw new IllegalArgumentException("Invalid application number");
		}
		else if(null == viewPdfDetailsModel.getFileStoreId() || viewPdfDetailsModel.getFileStoreId().equals("")) {
			throw new IllegalArgumentException("Invalid File Store Id");
		}
		else if(null == viewPdfDetailsModel.getKey() || viewPdfDetailsModel.getKey().equals("")) {
			throw new IllegalArgumentException("Invalid Key");
		}
	
	}


	public void validateNewLocationRequest(NewLocationRequest newLocationRequest) {
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest)) {
			throw new IllegalArgumentException("Invalid New Location Request");
		}
		
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getRequestInfo())) {
			throw new IllegalArgumentException("Invalid Request Info");
		}
		
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel())) {
			throw new IllegalArgumentException("Invalid New NewLocationModel object");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getAction())) {
			throw new IllegalArgumentException("Invalid Action");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getApplicantAddress())) {
			throw new IllegalArgumentException("Invalid APPLICANT ADDRESS");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getApplicantName())) {
			throw new IllegalArgumentException("Invalid APPLICANT NAME");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getAreaRequirement())) {
			throw new IllegalArgumentException("Invalid AREA REQUIREMENT");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getBusinessService())) {
			throw new IllegalArgumentException("Invalid BUSINESS SERVICE");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getContact())) {
			throw new IllegalArgumentException("Invalid CONTACT");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getIdProof())) {
			throw new IllegalArgumentException("Invalid ID PROOF");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getLandmark())) {
			throw new IllegalArgumentException("Invalid LANDMARK");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getLocalityAddress())) {
			throw new IllegalArgumentException("Invalid LOCALITY ADDRESS");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getMailAddress())) {
			throw new IllegalArgumentException("Invalid MAIL ADDRESS");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getSector())) {
			throw new IllegalArgumentException("Invalid SECTOR");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getTenantId())) {
			throw new IllegalArgumentException("Invalid TENANT ID");
		}
	}


	public void validateNewLocationRequestForUpdate(NewLocationRequest newLocationRequest) {
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest)) {
			throw new IllegalArgumentException("Invalid New Location Request");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel())) {
			throw new IllegalArgumentException("Invalid New NewLocationModel object");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getApplicationNumber())) {
			throw new IllegalArgumentException("Invalid APPLICATION NUMBER");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getAction())) {
			throw new IllegalArgumentException("Invalid Action");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getApplicantAddress())) {
			throw new IllegalArgumentException("Invalid APPLICANT ADDRESS");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getApplicantName())) {
			throw new IllegalArgumentException("Invalid APPLICANT NAME");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getAreaRequirement())) {
			throw new IllegalArgumentException("Invalid AREA REQUIREMENT");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getBusinessService())) {
			throw new IllegalArgumentException("Invalid BUSINESS SERVICE");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getContact())) {
			throw new IllegalArgumentException("Invalid CONTACT");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getIdProof())) {
			throw new IllegalArgumentException("Invalid ID PROOF");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getLandmark())) {
			throw new IllegalArgumentException("Invalid LANDMARK");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getLocalityAddress())) {
			throw new IllegalArgumentException("Invalid LOCALITY ADDRESS");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getMailAddress())) {
			throw new IllegalArgumentException("Invalid MAIL ADDRESS");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getSector())) {
			throw new IllegalArgumentException("Invalid SECTOR");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel().getTenantId())) {
			throw new IllegalArgumentException("Invalid TENANT ID");
		}
	}


	public void validateCommercialGroundAvailabilityModel(
			CommercialGrndAvailabilityModel commercialGrndAvailabilityModel) {
		if(BookingsFieldsValidator.isNullOrEmpty(commercialGrndAvailabilityModel)) {
			throw new IllegalArgumentException("Invalid commercialGrndAvailabilityModel object");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(commercialGrndAvailabilityModel.getBookingVenue())) {
			throw new IllegalArgumentException("Invalid Booking Venue");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(commercialGrndAvailabilityModel.getFromDate())) {
			throw new IllegalArgumentException("Invalid From Date");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(commercialGrndAvailabilityModel.getToDate())) {
			throw new IllegalArgumentException("Invalid To Date");
		}
		if(BookingsFieldsValidator.isNullOrEmpty(commercialGrndAvailabilityModel.isLocked())) {
			throw new IllegalArgumentException("Invalid isLocked");
		}
	}
}

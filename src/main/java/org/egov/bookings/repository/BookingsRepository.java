package org.egov.bookings.repository;

import java.util.Date;
import java.util.List;

import org.egov.bookings.model.BookingsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface BookingsRepository.
 */
@Repository
public interface BookingsRepository
		extends CrudRepository<BookingsModel, Long> {
	
	/**
	 * Find by tenant id and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuId( String tenantId, String uuId );
	
	/**
	 * Find by tenant id and bk application number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuId( String tenantId, String applicationNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuId( String tenantId, String applicationNumber, String applicationStatus, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuId( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuId(String tenantId, String applicationNumber, String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuIdAndBkDateCreatedBetween( String tenantId, String applicationNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndUuId( String tenantId, String applicationStatus, String uuId );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuId( String tenantId, String applicationStatus, String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuId( String tenantId,String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk mobile number and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( String tenantId, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuIdAndBkDateCreatedBetween( String tenantId, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id.
	 *
	 * @param tenantId the tenant id
	 * @return the list
	 */
	List<BookingsModel> findByTenantId( String tenantId );
	
	/**
	 * Find by tenant id and bk application number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumber( String tenantId, String applicationNumber );
	
	/**
	 * Find by tenant id and bk application number and bk application status.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatus( String tenantId, String applicationNumber, String applicationStatus );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumber( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumber(String tenantId, String applicationNumber, String mobileNumber );
	
	/**
	 * Find by tenant id and bk application status.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatus( String tenantId, String applicationStatus );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumber( String tenantId, String applicationStatus, String mobileNumber );
	
	/**
	 * Find by tenant id and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumber( String tenantId,String mobileNumber );
	
	/**
	 * Find by tenant id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkDateCreatedBetween( String tenantId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkDateCreatedBetween( String tenantId, String applicationNumber, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String mobileNumber, Date fromDate, Date toDate );
	
	
	
	
}

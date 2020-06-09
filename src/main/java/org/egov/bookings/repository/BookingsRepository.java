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
	List<BookingsModel> findByTenantIdAndUuid( String tenantId, String uuId );
	
	/**
	 * Find by tenant id and bk application number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuid( String tenantId, String applicationNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuid( String tenantId, String applicationNumber, String applicationStatus, String uuId );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuid( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuid(String tenantId, String applicationNumber, String mobileNumber, String uuId );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidAndBkDateCreatedBetween( String tenantId, String applicationNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndUuid( String tenantId, String applicationStatus, String uuId );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuid( String tenantId, String applicationStatus, String mobileNumber, String uuId );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and uu id.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuid( String tenantId,String mobileNumber, String uuId );
	
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
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidAndBkDateCreatedBetween( String tenantId, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and uu id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidAndBkDateCreatedBetween( String tenantId, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id.
	 *
	 * @param tenantId the tenant id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkSector( String tenantId, String sector );
	
	/**
	 * Find by tenant id and bk application number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkSector( String tenantId, String applicationNumber, String sector );
	
	/**
	 * Find by tenant id and bk application number and bk application status.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkSector( String tenantId, String applicationNumber, String applicationStatus, String sector );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSector( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String sector );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSector(String tenantId, String applicationNumber, String mobileNumber, String sector );
	
	/**
	 * Find by tenant id and bk application status.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkSector( String tenantId, String applicationStatus, String sector );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSector( String tenantId, String applicationStatus, String mobileNumber, String sector );
	
	/**
	 * Find by tenant id and bk mobile number.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkSector( String tenantId,String mobileNumber, String sector );
	
	/**
	 * Find by tenant id and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkSectorAndBkDateCreatedBetween( String tenantId, String sector, Date fromDate, Date toDate );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, String sector, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkSectorAndBkDateCreatedBetween( String tenantId, String applicationNumber, String sector, Date fromDate, Date toDate );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String sector, Date fromDate, Date toDate );
	
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
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, String sector, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( String tenantId, String mobileNumber, String sector, Date fromDate, Date toDate );
	
	
	
	
}

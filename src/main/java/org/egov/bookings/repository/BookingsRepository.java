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
	 * Count by tenant id and uuid.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @return the int
	 */
	int countByTenantIdAndUuid( String tenantId, String uuId );
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
	 * Count by tenant id and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param sectorList the sector list
	 * @return the int
	 */
	int countByTenantIdAndBkSectorIn( String tenantId, List< String > sectorList );
	
	/**
	 * Count by tenant id and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the int
	 */
	int countByTenantIdAndBkBookingTypeAndBkSectorIn( String tenantId, String bookingType , List< String > sectorList );
	
	
	/**
	 * Count by tenant id and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumberList the application number list
	 * @return the int
	 */
	int countByTenantIdAndBkApplicationNumberIn( String tenantId, List< String > applicationNumberList );
	
	
	/**
	 * Find by tenant id and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberIn( String tenantId, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationNumberIn( String tenantId, String applicationNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkApplicationNumberIn( String tenantId, String applicationNumber, String applicationStatus, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberIn( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkApplicationNumberIn(String tenantId, String applicationNumber, String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application status and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkApplicationNumberIn( String tenantId, String applicationStatus, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberIn( String tenantId, String applicationStatus, String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkApplicationNumberIn( String tenantId,String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param aplicationNumberList the aplication number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, List< String > aplicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	
	
	/**
	 * Find by tenant id and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkBookingTypeAndBkSectorIn( String tenantId, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorIn( String tenantId, String applicationNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( String tenantId, String applicationNumber, String applicationStatus, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk booking type and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate ); 
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( String tenantId, String applicationNumber, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk booking type and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk booking type and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( String tenantId, String applicationStatus, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( String tenantId, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk booking type and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( String tenantId, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk mobile number and bk booking type and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( String tenantId, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk booking type and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( String tenantId, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Count by tenant id and bk booking type and uuid.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param uuid the uuid
	 * @return the int
	 */
	int countByTenantIdAndBkBookingTypeAndUuid( String tenantId, String bookingType, String uuid );
	
	
	/**
	 * Find by tenant id and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkSectorIn( String tenantId, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingType( String tenantId, String applicationNumber, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidAndBkBookingType( String tenantId, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application number and bk application status and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuidAndBkBookingType( String tenantId, String applicationNumber, String applicationStatus, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingType( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, String bookingType );

	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uuid and bk booking type and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingType(String tenantId, String applicationNumber, String mobileNumber, String uuId, String bookingType );

	/**
	 * Find by tenant id and bk application number and bk mobile number and uuid and bk booking type and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and uuid and bk booking type and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( String tenantId, String applicationNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndUuidAndBkBookingType( String tenantId, String applicationStatus, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingType( String tenantId, String applicationStatus, String mobileNumber, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uuid and bk booking type and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and uuid and bk booking type.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidAndBkBookingType( String tenantId,String mobileNumber, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk mobile number and uuid and bk booking type and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( String tenantId, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and uuid and bk booking type and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidAndBkBookingTypeAndBkDateCreatedBetween( String tenantId, String uuId, String bookingType, Date fromDate, Date toDate );
	
}
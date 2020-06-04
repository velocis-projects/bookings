package org.egov.bookings.repository;

import java.util.Date;
import java.util.List;

import org.egov.bookings.model.BookingsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository
		extends CrudRepository<BookingsModel, Long> {
	
	List<BookingsModel> findByTenantId( String tenantId );
	List<BookingsModel> findByTenantIdAndBkApplicationNumber( String tenantId, String applicationNumber );
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatus( String tenantId, String applicationNumber, String applicationStatus );
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumber( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber );
	List<BookingsModel> findByTenantIdAndBkApplicationStatus( String tenantId, String applicationStatus );
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumber( String tenantId, String applicationStatus, String mobileNumber );
	List<BookingsModel> findByTenantIdAndBkMobileNumber( String tenantId,String mobileNumber );
	
	@Query(
			value = "SELECT * FROM tt_bookings WHERE tenant_id = :tenantId AND bk_mobile_number = :mobileNumber AND bk_application_number = :applicationNumber AND bk_application_status = :applicationStatus AND bk_date_created BETWEEN :fromDate AND :toDate)) ORDER BY bk_date_created DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBooking1( @Param("tenantId") String tenantId, @Param("mobileNumber") String mobileNumber
				, @Param("applicationNumber") String applicationNumber, @Param("applicationStatus") String applicationStatus
				, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate );
	
	@Query(
			value = "SELECT * FROM tt_bookings WHERE tenant_id = :tenantId AND bk_mobile_number = :mobileNumber AND bk_application_status = :applicationStatus AND bk_date_created BETWEEN :fromDate AND :toDate)) ORDER BY bk_date_created DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBookingWithFromDate( @Param("tenantId") String tenantId, @Param("mobileNumber") String mobileNumber
				, @Param("applicationStatus") String applicationStatus
				, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate );
	
	@Query(
			value = "SELECT * FROM tt_bookings WHERE tenant_id = :tenantId AND bk_mobile_number = :mobileNumber AND bk_date_created BETWEEN :fromDate AND :toDate)) ORDER BY bk_date_created DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBookingWithToDate( @Param("tenantId") String tenantId, @Param("mobileNumber") String mobileNumber
				, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate );
	
	@Query(
			value = "SELECT * FROM tt_bookings WHERE tenant_id = :tenantId AND bk_date_created BETWEEN CAST(:fromDate AS date) AND CAST(:toDate AS date))) ORDER BY bk_date_created DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBookingWithFromAndToDate( @Param("tenantId") String tenantId
				, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate );

}

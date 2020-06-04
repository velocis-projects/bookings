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
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumber(String tenantId, String applicationNumber, String mobileNumber );
	List<BookingsModel> findByTenantIdAndBkApplicationStatus( String tenantId, String applicationStatus );
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumber( String tenantId, String applicationStatus, String mobileNumber );
	List<BookingsModel> findByTenantIdAndBkMobileNumber( String tenantId,String mobileNumber );
	List<BookingsModel> findByTenantIdAndBkDateCreatedBetween( String tenantId, Date fromDate, Date toDate );
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, Date fromDate, Date toDate );
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkDateCreatedBetween( String tenantId, String applicationNumber, Date fromDate, Date toDate );
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, Date fromDate, Date toDate );
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, Date fromDate, Date toDate );
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkDateCreatedBetween( String tenantId, String mobileNumber, Date fromDate, Date toDate );
}

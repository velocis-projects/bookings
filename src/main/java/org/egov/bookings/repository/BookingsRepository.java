package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.BookingsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository
		extends CrudRepository<BookingsModel, Long> {
	
	@Query(
			value = "SELECT * FROM tt_bookings where tenantId = 'ch' ORDER BY bk_application_number DESC", 
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBooking();

}

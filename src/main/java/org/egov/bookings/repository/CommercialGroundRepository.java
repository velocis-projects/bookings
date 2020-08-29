package org.egov.bookings.repository;

import java.sql.Date;
import java.util.Set;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.repository.querybuilder.BookingsQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommercialGroundRepository.
 */
@Repository
public interface CommercialGroundRepository extends CrudRepository<CommercialGroundFeeModel, Long> {

	/**
	 * Find by locality and category.
	 *
	 * @param locality the locality
	 * @param category the category
	 * @return the commercial ground fee model
	 */
	CommercialGroundFeeModel findByLocalityAndCategory(String locality, String category);

	CommercialGroundFeeModel findByBookingVenueAndCategory(String bookingVenue, String category);
	
	

}

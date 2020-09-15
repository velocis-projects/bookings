package org.egov.bookings.repository;

import org.egov.bookings.model.CommercialGroundFeeModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommercialGroundRepository.
 */
@Repository
public interface CommercialGroundRepository extends CrudRepository<CommercialGroundFeeModel, String> {

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

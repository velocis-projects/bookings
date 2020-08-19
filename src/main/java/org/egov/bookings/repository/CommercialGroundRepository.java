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
	
	/**
	 * Find all booked venues from now.
	 *
	 * @param bookingVenue the booking venue
	 * @param bookingType the booking type
	 * @param date the date
	 * @param APPLY the apply
	 * @return the sets the
	 */
	@Query(value = BookingsQueryBuilder.CHECK_COMMERCIAL_GROUND_AVAILABILITY, nativeQuery = true)
	public Set<BookingsModel> findAllBookedVenuesFromNow(
			@Param(BookingsConstants.BOOKING_VENUE) String bookingVenue,
			@Param(BookingsConstants.BOOKING_TYPE) String bookingType, @Param(BookingsConstants.DATE) Date date,@Param(BookingsConstants.APPLY) String APPLY);
	

}

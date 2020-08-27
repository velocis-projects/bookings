package org.egov.bookings.service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.ParkAndCommunitySearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.web.models.BookingsRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface ParkAndCommunityService.
 */
public interface ParkAndCommunityService {

	/**
	 * Creates the park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	BookingsModel createParkAndCommunityBooking(BookingsRequest bookingsRequest);

	/**
	 * Update park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	BookingsModel updateParkAndCommunityBooking(BookingsRequest bookingsRequest);

	/**
	 * Fetch park community master.
	 *
	 * @return the list
	 */
	List<ParkCommunityHallV1MasterModel> fetchParkCommunityMaster();

	Set<AvailabilityResponse> availabilitySearch(ParkAndCommunitySearchCriteria parkAndCommunitySearchCriteria);

	Set<Date> fetchBookedDates(BookingsRequest bookingsRequest);

	ParkCommunityHallV1MasterModel findParkAndCommunityFee(String string);

}

package org.egov.bookings.service;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;

public interface ParkAndCommunityService {

	BookingsModel createParkAndCommunityBooking(BookingsRequest bookingsRequest);

	BookingsModel updateParkAndCommunityBooking(BookingsRequest bookingsRequest);

}

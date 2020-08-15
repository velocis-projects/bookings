package org.egov.bookings.service;

import java.util.Set;

import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.JurisdictionAvailabilityRequest;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.web.models.BookingsRequest;

public interface OsujmService {

	OsujmFeeModel findJurisdictionFee(BookingsRequest bookingsRequest);

	Set<AvailabilityResponse> searchJurisdictionAvailability(
			JurisdictionAvailabilityRequest jurisdictionAvailabilityRequest);

}

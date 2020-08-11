package org.egov.bookings.service;

import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.web.models.BookingsRequest;

public interface OsujmFeeService {

	OsujmFeeModel findJurisdictionFee(BookingsRequest bookingsRequest);

}

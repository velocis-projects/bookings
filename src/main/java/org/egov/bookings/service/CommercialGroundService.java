package org.egov.bookings.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGroundFeeModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommercialGroundFeeService.
 */
public interface CommercialGroundService {

	/**
	 * Search commercial ground fee.
	 *
	 * @param commercialGroundFeeSearchCriteria the commercial ground fee search criteria
	 * @return the commercial ground fee model
	 */
	CommercialGroundFeeModel searchCommercialGroundFee(
			CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria);

	Set<AvailabilityResponse> searchCommercialGroundAvailabilty(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria);

}

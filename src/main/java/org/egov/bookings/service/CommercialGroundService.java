package org.egov.bookings.service;

import java.util.List;

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

	List<BookingsModel> searchCommercialGroundAvailabilty(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria);

}

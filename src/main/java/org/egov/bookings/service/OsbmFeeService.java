package org.egov.bookings.service;

import org.egov.bookings.contract.OsbmSearchCriteria;
import org.egov.bookings.model.OsbmFeeModel;

public interface OsbmFeeService {

	/**
	 * Search osbm fee.
	 *
	 * @param osbmSearchCriteria the osbm search criteria
	 * @return the osbm fee model
	 */
	OsbmFeeModel searchOsbmFee(OsbmSearchCriteria osbmSearchCriteria);

}

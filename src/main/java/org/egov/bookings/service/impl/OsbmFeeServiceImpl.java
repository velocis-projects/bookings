package org.egov.bookings.service.impl;

import javax.transaction.Transactional;

import org.egov.bookings.contract.OsbmSearchCriteria;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.service.OsbmFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OsbmFeeServiceImpl implements OsbmFeeService {

	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	@Autowired
	private EnrichmentService enrichmentService;
	
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsbmFeeService#searchOsbmFee(org.egov.bookings.contract.OsbmSearchCriteria)
	 */
	@Override
	public OsbmFeeModel searchOsbmFee(OsbmSearchCriteria osbmSearchCriteria) {

		OsbmFeeModel osbmFeeModel = osbmFeeRepository
				.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(
						osbmSearchCriteria.getVillageCity(), osbmSearchCriteria.getResidentialCommercial(),
						osbmSearchCriteria.getStorage(), osbmSearchCriteria.getDurationInMonths(),
						osbmSearchCriteria.getConstructionType());
		return osbmFeeModel;
	}

}

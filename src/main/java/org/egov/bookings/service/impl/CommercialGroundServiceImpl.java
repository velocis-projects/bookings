package org.egov.bookings.service.impl;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.service.CommercialGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class CommercialGroundFeeServiceImpl.
 */
@Service
@Transactional
public class CommercialGroundServiceImpl implements CommercialGroundService {

	/** The commercial ground fee repository. */
	@Autowired
	private CommercialGroundRepository commercialGroundRepository;

	@Autowired
	CommonRepository commonRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.CommercialGroundFeeService#
	 * searchCommercialGroundFee(org.egov.bookings.contract.
	 * CommercialGroundFeeSearchCriteria)
	 */
	@Override
	public CommercialGroundFeeModel searchCommercialGroundFee(
			CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria) {
		return commercialGroundRepository.findByLocalityAndCategory(commercialGroundFeeSearchCriteria.getLocality(),
				commercialGroundFeeSearchCriteria.getCategory());
	}

	@Override
	public List<BookingsModel> searchCommercialGroundAvailabilty(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria) {
		
		Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		
		return commonRepository.findAllByBkBookingVenueAndBetweenToDateAndFromDate(
				commercialGroundAvailabiltySearchCriteria.getBookingVenue(),
				commercialGroundAvailabiltySearchCriteria.getBookingType(),
				date);
	}

}

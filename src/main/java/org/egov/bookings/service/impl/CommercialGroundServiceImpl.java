package org.egov.bookings.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections.map.HashedMap;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommercialGrndAvailabilityRepository;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.service.CommercialGroundService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.tracer.model.CustomException;
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

	@Autowired
	private BookingsRepository bookingsRepository;
	
	private CommercialGrndAvailabilityRepository commercialGrndAvailabilityRepository;

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
		try {
		return commercialGroundRepository.findByBookingVenueAndCategory(commercialGroundFeeSearchCriteria.getBookingVenue(),
				commercialGroundFeeSearchCriteria.getCategory());
		}catch (Exception e) {
			throw new CustomException("DATABASE_FETCH_ERROR",e.getLocalizedMessage());
		}
	}

	@Override
	public Set<AvailabilityResponse> searchCommercialGroundAvailabilty(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria) {

		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
        LocalDate date = LocalDate.now();
        Date date1 = Date.valueOf(date);
        Set<AvailabilityResponse> bookedDates = new HashSet<>();
        Set<BookingsModel> bookingsModel = commonRepository.findAllBookedVenuesFromNow(
				commercialGroundAvailabiltySearchCriteria.getBookingVenue(),
				commercialGroundAvailabiltySearchCriteria.getBookingType(),
				date1,BookingsConstants.APPLY);
		for(BookingsModel bkModel : bookingsModel) {
			bookedDates.add(AvailabilityResponse.builder().fromDate(bkModel.getBkFromDate()).toDate(bkModel.getBkToDate()).build());
		}
		
		return bookedDates;

		
	}

	@Override
	public CommercialGrndAvailabilityModel lockCommercialAvailability(
			CommercialGrndAvailabilityModel commercialGrndAvailabilityModel) {
		CommercialGrndAvailabilityModel commGrndAvail = null;
		try {
			commGrndAvail = commercialGrndAvailabilityRepository.save(commercialGrndAvailabilityModel);
			return commGrndAvail;
		}catch (Exception e) {
			throw new CustomException("DATABASE_ERROR",e.getLocalizedMessage());
		}
	}

}

package org.egov.bookings.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import javax.transaction.Transactional;

import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.service.CommercialGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;

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
	public List<LocalDate> searchCommercialGroundAvailabilty(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria) {
		
		//Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		
		BookingsModel bookingsModel= bookingsRepository.findByBkBookingVenueAndBkToDateAndBkFromDateAndBkBookingType(
				commercialGroundAvailabiltySearchCriteria.getBookingVenue(),
				commercialGroundAvailabiltySearchCriteria.getToDate(),
				commercialGroundAvailabiltySearchCriteria.getFromDate(),
				commercialGroundAvailabiltySearchCriteria.getBookingType());
		
	//	LocalDate startDate = LocalDate.now();
	//	LocalDate endDate = startDate.plusMonths(2);
		
		LocalDate startDate = Date.valueOf(bookingsModel.getBkFromDate()+"").toLocalDate();
		LocalDate endDate = Date.valueOf(bookingsModel.getBkToDate()+"").toLocalDate();
		 
		long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
		          
		List<LocalDate> listOfDates2 = LongStream.range(0, numOfDays)
		                                .mapToObj(startDate::plusDays)
		                                .collect(Collectors.toList());
		 
		System.out.println(listOfDates2.size());     // 61
		
		
		return listOfDates2;
	}

}

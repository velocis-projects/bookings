package org.egov.bookings.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.JurisdictionAvailabilityRequest;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.service.OsujmService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class OsujmServiceImpl.
 */
@Service
@Transactional
public class OsujmServiceImpl implements OsujmService {

	/** The osujm fee repository. */
	@Autowired
	private OsujmFeeRepository osujmFeeRepository;

	/** The common repository. */
	@Autowired
	private CommonRepository commonRepository;
	
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsujmService#findJurisdictionFee(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public OsujmFeeModel findJurisdictionFee(BookingsRequest bookingsRequest) {
		OsujmFeeModel osujmFeeModel = null;
		try {
			Long area = Long.valueOf(bookingsRequest.getBookingsModel().getBkAreaRequired());
			String sector = bookingsRequest.getBookingsModel().getBkSector();
			if(!sector.equals("SECTOR-17") && !sector.equals("SECTOR-22"))
				sector = "OTHER";
			osujmFeeModel = osujmFeeRepository.findJurisdictionFee(area,sector);
			if(BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel)) {
				throw new IllegalArgumentException("There is not any amount for open space under jurisdiction criteria in database");
			}
			
		}
		catch (Exception e) {
			throw new CustomException("INVALID_REQUEST",e.getLocalizedMessage());
		}
		
		return osujmFeeModel;
	}

	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsujmService#searchJurisdictionAvailability(org.egov.bookings.contract.JurisdictionAvailabilityRequest)
	 */
	@Override
	public Set<AvailabilityResponse> searchJurisdictionAvailability(
			JurisdictionAvailabilityRequest jurisdictionAvailabilityRequest) {

        LocalDate date = LocalDate.now();
        Date date1 = Date.valueOf(date);
        Set<AvailabilityResponse> bookedDates = new HashSet<>();
        Set<BookingsModel> bookingsModel = commonRepository.searchJurisdictionAvailability(
        		jurisdictionAvailabilityRequest.getBookingVenue(),
        		jurisdictionAvailabilityRequest.getBookingType(),
        		jurisdictionAvailabilityRequest.getBkSector(),
				date1,BookingsConstants.APPLY);
		for(BookingsModel bkModel : bookingsModel) {
			bookedDates.add(AvailabilityResponse.builder().fromDate(bkModel.getBkFromDate()).toDate(bkModel.getBkToDate()).build());
		}
		
		return bookedDates;

		
	}
	
}
package org.egov.bookings.service.impl;

import javax.transaction.Transactional;

import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.service.OsujmFeeService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OsujmFeeServiceImpl implements OsujmFeeService {

	@Autowired
	private OsujmFeeRepository osujmFeeRepository;

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
			throw new CustomException("DATABASE_ERROR","Error while fetching data from osujmFee table");
		}
		
		return osujmFeeModel;
	}
	
}

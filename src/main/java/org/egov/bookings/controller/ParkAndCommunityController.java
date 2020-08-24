package org.egov.bookings.controller;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.service.OsujmNewLocationService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.service.impl.EnrichmentService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/park/community")
public class ParkAndCommunityController {


	@Autowired
	private ParkAndCommunityService parkAndCommunityService;
	
	@Autowired
	private Environment env;

	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	@PostMapping("_create")
	private ResponseEntity<?> createParkAndCommunityBooking(
			@RequestBody BookingsRequest bookingsRequest) {
		
		bookingsFieldsValidator.validatePAndCBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = parkAndCommunityService
				.createParkAndCommunityBooking(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Creating Park And Community Booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage(" Park And Community Booking Created Successfully");
			rs.setData(bookingsModel);
		}

		return ResponseEntity.ok(rs);
	}
	
	

	@PostMapping("_update")
	private ResponseEntity<?> updateParkAndCommunityBooking(
			@RequestBody BookingsRequest bookingsRequest) {
		
		bookingsFieldsValidator.validatePAndCBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = parkAndCommunityService
				.updateParkAndCommunityBooking(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Updating Park And Community Booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Park And Community Booking Updated Successfully ");
			rs.setData(bookingsModel);
		}

		return ResponseEntity.ok(rs);
	}
	
}

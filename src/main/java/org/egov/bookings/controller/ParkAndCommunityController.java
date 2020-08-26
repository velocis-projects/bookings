package org.egov.bookings.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.service.impl.EnrichmentService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class ParkAndCommunityController.
 */
@RestController
@RequestMapping("/park/community")
public class ParkAndCommunityController {

	/** The park and community service. */
	@Autowired
	private ParkAndCommunityService parkAndCommunityService;

	/** The env. */
	@Autowired
	private Environment env;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	/** The bookings fields validator. */
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;

	/**
	 * Creates the park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping("_create")
	private ResponseEntity<?> createParkAndCommunityBooking(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validatePAndCBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = parkAndCommunityService.createParkAndCommunityBooking(bookingsRequest);
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

	/**
	 * Update park and community booking.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping("_update")
	private ResponseEntity<?> updateParkAndCommunityBooking(@RequestBody BookingsRequest bookingsRequest) {

		bookingsFieldsValidator.validatePAndCBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = parkAndCommunityService.updateParkAndCommunityBooking(bookingsRequest);
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

	/**
	 * Fetch park community master.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @return the response entity
	 */
	@PostMapping("/master/_fetch")
	private ResponseEntity<?> fetchParkCommunityMaster(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {

		List<ParkCommunityHallV1MasterModel> parkCommunityHallV1MasterList = parkAndCommunityService
				.fetchParkCommunityMaster();
		ResponseModel rs = new ResponseModel();
		if (parkCommunityHallV1MasterList == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Fetching Park And Community Master Data");
			rs.setData(parkCommunityHallV1MasterList);
		} else {
			rs.setStatus("200");
			rs.setMessage("Park And Community Master Data Fetched Successfully ");
			rs.setData(parkCommunityHallV1MasterList);
		}

		return ResponseEntity.ok(rs);
	}

}

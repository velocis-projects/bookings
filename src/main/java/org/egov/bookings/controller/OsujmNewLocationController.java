package org.egov.bookings.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.OsujmNewLocationService;
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
@RequestMapping("/newLocation/")
public class OsujmNewLocationController {

	@Autowired
	private OsujmNewLocationService newLocationService;
	
	@Autowired
	private Environment env;

	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	
	private static final Logger LOGGER = LogManager.getLogger( BookingsController.class.getName() );
	
	
	@PostMapping("_create")
	private ResponseEntity<?> addNewLocation(
			@RequestBody NewLocationRequest newLocationRequest) {
		
		
		NewLocationRequest response = newLocationService
				.addNewLocation(newLocationRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(response);
		
		return ResponseEntity.ok(rs);
	}
	
	

	@PostMapping("_update")
	private ResponseEntity<?> updateNewLocation(
			@RequestBody NewLocationRequest newLocationRequest) {
		
		
		OsujmNewLocationModel response = newLocationService
				.updateNewLocation(newLocationRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(response);
		
		return ResponseEntity.ok(rs);
	}
	
	
	
}

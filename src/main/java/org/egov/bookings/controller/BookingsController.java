package org.egov.bookings.controller;

import java.util.List;
import java.util.Optional;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.impl.EnrichmentService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/api")
public class BookingsController {

	@Autowired
	private BookingsService bookingsService;
	
	@Autowired
	private Environment env;

	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	@PostMapping("_create")
	private ResponseEntity<?> saveBuildingMaterial(
			@RequestBody BookingsRequest bookingsRequest) {
		
		bookingsFieldsValidator.validateAction(bookingsRequest.getBookingsModel().getBkAction());
		bookingsFieldsValidator.validateBusinessService(bookingsRequest.getBookingsModel().getBusinessService());
		bookingsFieldsValidator.validateTenantId(bookingsRequest.getBookingsModel().getTenantId());
		enrichmentService.enrichTLCreateRequest(bookingsRequest);
		BookingsModel bookingsModel = bookingsService
				.save(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(bookingsModel);
		
		return ResponseEntity.ok(rs);
	}

	@GetMapping("_getAllBookingData")
	private ResponseEntity<?> getAllBuildingMaterial() {
		List<BookingsModel> bookingsModel = bookingsService.getAllBuildingMaterial();
		
		if (bookingsModel.size() == 0)
			throw new ResourceAccessException(env.getProperty("NOT_FOUND"));
		return ResponseEntity.ok(bookingsModel);
	}
		
	
	@GetMapping("_getBookingDataById/{id}")
	private ResponseEntity<?> getBuildingMaterialById(@PathVariable Long id) {
		BookingsModel bookingsModel = bookingsService.getBuildingMaterialById(id);
		
		Optional.ofNullable(bookingsModel).orElseThrow(() -> new ResourceAccessException(env.getProperty("NOT_FOUND")));
		return ResponseEntity.ok(bookingsModel);
		
	}
	
}

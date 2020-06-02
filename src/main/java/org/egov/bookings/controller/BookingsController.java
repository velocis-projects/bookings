package org.egov.bookings.controller;

import java.util.List;
import java.util.Optional;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.impl.EnrichmentService;
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
	
	@PostMapping("/save")
	private ResponseEntity<?> saveBuildingMaterial(
			@RequestBody BookingsRequest bookingsRequest) {
		
		enrichmentService.enrichTLCreateRequest(bookingsRequest);
		List<BookingsModel> bookingsModel = bookingsService
				.save(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(bookingsModel);
		
		return ResponseEntity.ok(rs);
	}

	@GetMapping("/getAllBookingData")
	private ResponseEntity<?> getAllBuildingMaterial() {
		List<BookingsModel> bookingsModel = bookingsService.getAllBuildingMaterial();
		
		if (bookingsModel.size() == 0)
			throw new ResourceAccessException(env.getProperty("NOT_FOUND"));
		return ResponseEntity.ok(bookingsModel);
	}
		
	
	@GetMapping("/getBookingDataById/{id}")
	private ResponseEntity<?> getBuildingMaterialById(@PathVariable Long id) {
		BookingsModel bookingsModel = bookingsService.getBuildingMaterialById(id);
		
		Optional.ofNullable(bookingsModel).orElseThrow(() -> new ResourceAccessException(env.getProperty("NOT_FOUND")));
		return ResponseEntity.ok(bookingsModel);
		
	}
	
	@PostMapping(value = "/_citizen/_search")
	public ResponseEntity<?> getCitizenSearchBooking( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		if( searchCriteriaFieldsDTO == null )
		{
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		if( searchCriteriaFieldsDTO.getTenantId() == null && searchCriteriaFieldsDTO.getTenantId() == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		List<BookingsModel> bookingsModel = bookingsService.getCitizenSearchBooking( searchCriteriaFieldsDTO );
		return ResponseEntity.ok(bookingsModel);
	}
	
	@PostMapping(value = "/_employee/_search")
	public ResponseEntity<?> getEmployeeSearchBooking( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		if( searchCriteriaFieldsDTO == null )
		{
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		if( searchCriteriaFieldsDTO.getTenantId() == null && searchCriteriaFieldsDTO.getTenantId() == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		List<BookingsModel> bookingsModel = bookingsService.getEmployeeSearchBooking( searchCriteriaFieldsDTO );
		return ResponseEntity.ok(bookingsModel);
	}
}

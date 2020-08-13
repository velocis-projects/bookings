package org.egov.bookings.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
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
		
		bookingsFieldsValidator.validateNewLocationRequest(newLocationRequest);
		OsujmNewLocationModel response = newLocationService
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
		
		bookingsFieldsValidator.validateNewLocationRequestForUpdate(newLocationRequest);
		OsujmNewLocationModel response = newLocationService
				.updateNewLocation(newLocationRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(response);
		
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Gets the employee newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee newlocation search
	 */
	@PostMapping(value = "/employee/osujm/_search")
	public ResponseEntity<?> getEmployeeNewlocationSearch( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		Booking booking = new Booking();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getTenantId())) 
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getUuid())) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			booking = newLocationService.getEmployeeNewlocationSearch(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getEmployeeNewlocationSearch " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen newlocation search
	 */
	@PostMapping(value = "/citizen/osujm/_search")
	public ResponseEntity<?> getCitizenNewlocationSearch( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		Booking booking = new Booking();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getTenantId())) 
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getUuid())) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			booking = newLocationService.getCitizenNewlocationSearch(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getCitizenNewlocationSearch " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Gets the all citizen newlocation.
	 *
	 * @return the all citizen newlocation
	 */
	@GetMapping(value = "/citizen/osujm/_all")
	public ResponseEntity<?> getAllCitizenNewlocation()
	{
		Booking booking = new Booking();
		try
		{
			booking = newLocationService.getAllCitizenNewlocation();
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getAllCitizenNewlocation " + e);
		}
		return ResponseEntity.ok(booking);
	}
}

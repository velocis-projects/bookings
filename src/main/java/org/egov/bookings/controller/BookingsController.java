package org.egov.bookings.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.ProcessInstanceSearchCriteria;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsController.
 */
@RestController
@RequestMapping("/api")
public class BookingsController {

	/** The bookings service. */
	@Autowired
	private BookingsService bookingsService;
	
	/** The env. */
	@Autowired
	private Environment env;

	/** The bookings fields validator. */
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger( BookingsController.class.getName() );
	
	
	/**
	 * Save building material.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping("_create")
	private ResponseEntity<?> saveBooking(
			@RequestBody BookingsRequest bookingsRequest) {

		
		bookingsFieldsValidator.validateCreateBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = bookingsService.save(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Failure while creating booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Data submitted successfully");
			rs.setData(bookingsModel);
		}
		return ResponseEntity.ok(rs);
	}
	
	
	/**
	 * Update building material.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping("_update")
	private ResponseEntity<?> updateBooking(
			@RequestBody BookingsRequest bookingsRequest) {
		
		bookingsFieldsValidator.validateUpdateBookingRequest(bookingsRequest);
		BookingsModel bookingsModel = bookingsService
				.update(bookingsRequest);
		ResponseModel rs = new ResponseModel();
		if (bookingsModel == null) {
			rs.setStatus("400");
			rs.setMessage("Failure while creating booking");
			rs.setData(bookingsModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("Data submitted successfully");
			rs.setData(bookingsModel);
		}
		
		return ResponseEntity.ok(rs);
	}
	

	
		
	
	
	/**
	 * Gets the citizen search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen search booking
	 */
	@PostMapping(value = "/citizen/_search")
	public ResponseEntity<?> getCitizenSearchBooking( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
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
			booking = bookingsService.getCitizenSearchBooking(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getCitizenSearchBooking " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	@PostMapping(value = "/employee/_search")
	public ResponseEntity<?> getEmployeeSearchBooking( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
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
			booking = bookingsService.getEmployeeSearchBooking(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getEmployeeSearchBooking " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Employee records count.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping(value = "/employee/records/_count")
	public ResponseEntity<?> employeeRecordsCount( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO, @RequestBody BookingsRequest bookingsRequest )
	{
		Map< String, Integer > bookingCountMap = new HashMap<>();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) 
			{
				throw new IllegalArgumentException("Invalid bookingsRequest");
			}
			String tenantId = searchCriteriaFieldsDTO.getTenantId();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) 
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			bookingCountMap = bookingsService.employeeRecordsCount(tenantId, uuid, bookingsRequest);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the employeeRecordsCount " + e);
		}
		return ResponseEntity.ok( bookingCountMap );
	}
	
	/**
	 * Citizen records count.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @param bookingsRequest the bookings request
	 * @return the response entity
	 */
	@PostMapping(value = "/citizen/records/_count")
	public ResponseEntity<?> citizenRecordsCount( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO, @RequestBody BookingsRequest bookingsRequest )
	{
		Map< String, Integer > bookingCountMap = new HashMap<>();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO) ) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) 
			{
				throw new IllegalArgumentException("Invalid bookingsRequest");
			}
			String tenantId = searchCriteriaFieldsDTO.getTenantId();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) 
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			bookingCountMap = bookingsService.citizenRecordsCount(tenantId, uuid, bookingsRequest);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the citizenRecordsCount " + e);
		}
		return ResponseEntity.ok( bookingCountMap );
	}
	
	/**
	 * Gets the workflow process instances.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @param criteria the criteria
	 * @return the workflow process instances
	 */
	@PostMapping( value = "egov-workflow/process/_search")
	public ResponseEntity<?> getWorkflowProcessInstances( @Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
            @Valid @ModelAttribute ProcessInstanceSearchCriteria criteria )
	{
		Object result = new Object();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(requestInfoWrapper)) 
			{
				throw new IllegalArgumentException("Invalid requestInfoWrapper");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(criteria)) 
			{
				throw new IllegalArgumentException("Invalid criteria");
			}
			result = bookingsService.getWorkflowProcessInstances(requestInfoWrapper, criteria);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getWorkflowProcessInstances " + e);
		}
		return ResponseEntity.ok( result );
	}
	
	/**
	 * Gets the assignee.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the assignee
	 */
	@PostMapping( value = "employee/assignee/_search")
	public ResponseEntity<?> getAssignee( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		List<UserDetails> userdetailsList = new ArrayList<>();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			userdetailsList = bookingsService.getAssignee(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getAssignee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok( userdetailsList );
	}
	
}

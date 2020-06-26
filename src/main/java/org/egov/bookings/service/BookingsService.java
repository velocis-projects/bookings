package org.egov.bookings.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.bookings.contract.Booking;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;

/**
 * The Interface BookingsService.
 */
public interface BookingsService {

	/**
	 * Save.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel save(BookingsRequest bookingsRequest);

	/**
	 * Gets the all building material.
	 *
	 * @return the all building material
	 */
	public List<BookingsModel> getAllBuildingMaterial();

	/**
	 * Gets the building material by id.
	 *
	 * @param id the id
	 * @return the building material by id
	 */
	public BookingsModel getBuildingMaterialById(Long id);
	
	/**
	 * Gets the citizen search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen search booking
	 */
	public Booking getCitizenSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO );
	
	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	public Booking getEmployeeSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO );
	
	/**
	 * Update.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel update(BookingsRequest bookingsRequest);
	
	/**
	 * Gets the all employee records.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @return the all employee records
	 */
	public Map< String, Integer > getAllEmployeeRecords( String tenantId, String uuid );
	
	/**
	 * Gets the all citizen records.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @return the all citizen records
	 */
	public Map< String, Integer > getAllCitizenRecords( String tenantId, String uuid );
	
}

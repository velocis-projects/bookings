package org.egov.bookings.service;

import java.util.List;
import java.util.Map;

import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.ProcessInstanceSearchCriteria;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.ProcessInstance;
import org.egov.bookings.web.models.ProcessInstanceRequest;

// TODO: Auto-generated Javadoc
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
	 * Employee records count.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	public Map< String, Integer > employeeRecordsCount( String tenantId, String uuid, BookingsRequest bookingsRequest );
	
	/**
	 * Citizen records count.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	public Map< String, Integer > citizenRecordsCount( String tenantId, String uuid, BookingsRequest bookingsRequest );
	
	/**
	 * Gets the workflow process instances.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @param criteria the criteria
	 * @return the workflow process instances
	 */
	public Object getWorkflowProcessInstances(RequestInfoWrapper requestInfoWrapper, ProcessInstanceSearchCriteria criteria);
}

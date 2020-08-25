package org.egov.bookings.service;

import java.util.List;
import java.util.Map;

import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.ProcessInstanceSearchCriteria;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;

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

	/**
	 * Checks if is booking exists.
	 *
	 * @param bkApplicationNumber the bk application number
	 * @return true, if is booking exists
	 */
	public boolean isBookingExists(String bkApplicationNumber);


	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	public List<BookingApprover> fetchAllApprover();

	
	/**
	 * Gets the assignee.
	 *
	 * @param requestinfo the requestinfo
	 * @param applicationNumber the application number
	 * @param action the action
	 * @return the assignee
	 */
	public List<UserDetails> getAssignee(RequestInfo requestinfo, String applicationNumber, String action);
}

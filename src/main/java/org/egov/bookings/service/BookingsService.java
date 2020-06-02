package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;

public interface BookingsService {

	public List<BookingsModel> save(BookingsRequest bookingsRequest);

	public List<BookingsModel> getAllBuildingMaterial();

	public BookingsModel getBuildingMaterialById(Long id);
	
	/**
	 * Gets the citizen search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen search booking
	 */
	public List<BookingsModel> getCitizenSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO );
	
	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	public List<BookingsModel> getEmployeeSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO );
}

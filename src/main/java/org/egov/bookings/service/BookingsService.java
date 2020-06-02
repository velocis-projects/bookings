package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;

public interface BookingsService {

	public List<BookingsModel> save(BookingsRequest bookingsRequest);

	public List<BookingsModel> getAllBuildingMaterial();

	public BookingsModel getBuildingMaterialById(Long id);
}

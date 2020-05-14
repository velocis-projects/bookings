package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.model.OpenSpaceBuildingMaterialBookingsModel;

public interface OpenSpaceBuildingMaterialBookingsService {

	public OpenSpaceBuildingMaterialBookingsModel save(OpenSpaceBuildingMaterialBookingsModel bookingsModel );

	public List<OpenSpaceBuildingMaterialBookingsModel> getAllBuildingMaterial();

	public OpenSpaceBuildingMaterialBookingsModel getBuildingMaterialById(Long id);
}

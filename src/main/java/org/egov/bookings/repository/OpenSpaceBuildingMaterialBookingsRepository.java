package org.egov.bookings.repository;

import org.egov.bookings.model.OpenSpaceBuildingMaterialBookingsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenSpaceBuildingMaterialBookingsRepository
		extends CrudRepository<OpenSpaceBuildingMaterialBookingsModel, Long> {

}

package org.egov.bookings.repository;

import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface ParkCommunityHallV1MasterRepository.
 */
@Repository
public interface ParkCommunityHallV1MasterRepository extends JpaRepository<ParkCommunityHallV1MasterModel, String> {

	ParkCommunityHallV1MasterModel findById(String bookingVenue);

}

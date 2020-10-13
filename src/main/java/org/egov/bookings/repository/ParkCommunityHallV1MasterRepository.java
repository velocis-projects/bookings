package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The Interface ParkCommunityHallV1MasterRepository.
 */
@Repository
public interface ParkCommunityHallV1MasterRepository extends JpaRepository<ParkCommunityHallV1MasterModel, String> {

	ParkCommunityHallV1MasterModel findById(String bookingVenue);

	List<ParkCommunityHallV1MasterModel> findByVenueTypeAndSector(String venueType, String sector);
	
	/**
	 * Find pacc fee records by limit.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM bk_park_community_hall_v1 LIMIT 100 OFFSET (?1)",
			nativeQuery = true )
			List<ParkCommunityHallV1MasterModel> findPaccFeeRecordsByLimit( int offSet );

}

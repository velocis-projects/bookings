package org.egov.bookings.repository;

import org.egov.bookings.model.BookingsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkAndCommunityRepository extends CrudRepository<BookingsModel, String> {

}

package org.egov.bookings.repository;

import org.egov.bookings.model.OsujmNewLocationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsujmNewLocationRepository extends CrudRepository<OsujmNewLocationModel, String> {

}

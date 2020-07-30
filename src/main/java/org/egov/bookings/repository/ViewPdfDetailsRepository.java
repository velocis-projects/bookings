package org.egov.bookings.repository;

import org.egov.bookings.model.ViewPdfDetailsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewPdfDetailsRepository extends CrudRepository<ViewPdfDetailsModel, Long> {

}

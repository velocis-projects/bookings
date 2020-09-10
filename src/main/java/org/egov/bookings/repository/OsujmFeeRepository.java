package org.egov.bookings.repository;

import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.repository.querybuilder.BookingsQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OsujmFeeRepository extends JpaRepository<OsujmFeeModel, String> {

	@Query(value = BookingsQueryBuilder.FIND_JURISDICTION_AMOUNT, nativeQuery = true)
	OsujmFeeModel findJurisdictionFee(@Param(BookingsConstants.AREA) Long area, @Param(BookingsConstants.SECTOR)String sector);


}

package org.egov.bookings.repository;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.querybuilder.WorkflowQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommonRepository extends JpaRepository<BookingsModel, Long> {

	@Query(value = WorkflowQueryBuilder.FIND_ASSIGNEE_UUID, nativeQuery = true)
	public String findAssigneeUuid(@Param(BookingsConstants.TENANT_ID) String tenantId,
			@Param(BookingsConstants.ACTION) String action,
			@Param(BookingsConstants.BUSINESS_SERVICE) String businessservice,
			@Param(BookingsConstants.ROLE_TENANT_ID) String role_tenantId);

}

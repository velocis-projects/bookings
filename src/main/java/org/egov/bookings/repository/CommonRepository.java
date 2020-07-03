package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.querybuilder.WorkflowQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommonRepository.
 */
public interface CommonRepository extends JpaRepository<BookingsModel, Long> {

	/**
	 * Find assignee uuid.
	 *
	 * @param tenantId the tenant id
	 * @param action the action
	 * @param businessservice the businessservice
	 * @param role_tenantId the role tenant id
	 * @return the string
	 */
	@Query(value = WorkflowQueryBuilder.FIND_ASSIGNEE_UUID, nativeQuery = true)
	public String findAssigneeUuid(@Param(BookingsConstants.TENANT_ID) String tenantId,
			@Param(BookingsConstants.ACTION) String action,
			@Param(BookingsConstants.BUSINESS_SERVICE) String businessservice,
			@Param(BookingsConstants.ROLE_TENANT_ID) String role_tenantId);
	
	
	/**
	 * Find application number.
	 *
	 * @param uuid the uuid
	 * @return the list
	 */
	@Query(value = WorkflowQueryBuilder.FIND_APPLICATION_NUMBER, nativeQuery = true)
	public List< String > findApplicationNumber(@Param(BookingsConstants.UUID) String uuid);
	
	/**
	 * Find sector list.
	 *
	 * @param uuid the uuid
	 * @return the list
	 */
	@Query(value = WorkflowQueryBuilder.FIND_SECTOR_LIST, nativeQuery = true)
	public List< String > findSectorList(@Param(BookingsConstants.UUID) String uuid);
	
	/**
	 * Find document list.
	 *
	 * @param applicationNumber the application number
	 * @return the list
	 */
	@Query(value = WorkflowQueryBuilder.FIND_DOCUMENT_LIST, nativeQuery = true)
	public List< ? > findDocumentList(@Param(BookingsConstants.APPLICATION_NUMBER) String applicationNumber);
	
	

}

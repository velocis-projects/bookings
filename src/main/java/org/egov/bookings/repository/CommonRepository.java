package org.egov.bookings.repository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

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
	 * @param tenantId        the tenant id
	 * @param action          the action
	 * @param businessservice the businessservice
	 * @param role_tenantId   the role tenant id
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
	public Set<String> findApplicationNumber(@Param(BookingsConstants.ROLES) String roles);

	/**
	 * Find sector list.
	 *
	 * @param uuid the uuid
	 * @return the list
	 */
	@Query(value = WorkflowQueryBuilder.FIND_SECTOR_LIST, nativeQuery = true)
	public List<String> findSectorList(@Param(BookingsConstants.UUID) String uuid);

	/**
	 * Find document list.
	 *
	 * @param applicationNumber the application number
	 * @return the list
	 */
	@Query(value = WorkflowQueryBuilder.FIND_DOCUMENT_LIST, nativeQuery = true)
	public List<?> findDocumentList(@Param(BookingsConstants.APPLICATION_NUMBER) String applicationNumber);

	/**
	 * Find business service.
	 *
	 * @param applicationNumber the application number
	 * @return the string
	 */
	@Query(value = WorkflowQueryBuilder.FIND_BUSINESS_SERVICE, nativeQuery = true)
	public String findBusinessService(@Param(BookingsConstants.APPLICATION_NUMBER) String applicationNumber);

	@Query(value = WorkflowQueryBuilder.CHECK_COMMERCIAL_GROUND_AVAILABILITY, nativeQuery = true)
	public List<BookingsModel> findAllByBkBookingVenueAndBetweenToDateAndFromDate(
			@Param(BookingsConstants.BOOKING_VENUE) String bookingVenue,
			@Param(BookingsConstants.BOOKING_TYPE) String bookingType, @Param(BookingsConstants.DATE) Date date);
	
	
	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	@Query(value = "select u.username,u.mobilenumber,u.name,u.uuid,u.id from eg_user u where u.type =:type",nativeQuery = true)
	List<?> fetchAllApprover( @Param(BookingsConstants.TYPE) String type);


}

package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmApproverModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface MasterService.
 */
public interface MasterService {
	
	/**
	 * Gets the park community inventory details.
	 *
	 * @param venue the venue
	 * @param sector the sector
	 * @return the park community inventory details
	 */
	public List< InventoryModel > getParkCommunityInventoryDetails(String venue, String sector);
	
	/**
	 * Creates the osbm approver.
	 *
	 * @param osbmApproverRequest the osbm approver request
	 * @return the osbm approver model
	 */
	public OsbmApproverModel createOsbmApprover(OsbmApproverRequest osbmApproverRequest);
	
	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	public List<BookingApprover> fetchAllApprover();

}

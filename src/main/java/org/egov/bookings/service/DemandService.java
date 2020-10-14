package org.egov.bookings.service;


import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.models.demand.GenerateBillCriteria;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;

public interface DemandService {

	public void createDemand(BookingsRequest bookingsRequest);

	public void updateDemand(BookingsRequest bookingsRequest);
	
	public BillResponse generateBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria);

}

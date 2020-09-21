package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.GenerateBillCriteria;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;

public interface DemandService {

	public List<Demand> createDemand(BookingsRequest bookingsRequest);

	public List<Demand> updateDemand(BookingsRequest bookingsRequest);
	
	public BillResponse generateBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria);

}

package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.web.models.BookingsRequest;

public interface DemandService {

	public List<Demand> createDemand(BookingsRequest bookingsRequest);

	public List<Demand> updateDemand(BookingsRequest bookingsRequest);

}

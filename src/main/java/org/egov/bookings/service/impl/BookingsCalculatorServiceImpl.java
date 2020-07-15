package org.egov.bookings.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookingsCalculatorServiceImpl implements BookingsCalculatorService  {

	@Autowired
	DemandRepository demandRepository;
	
	@Override
	public List<Demand> createDemand(DemandRequest demandRequest) {
		RequestInfo requestInfo = demandRequest.getRequestInfo();
		List<Demand> demands = demandRequest.getDemands();
		return demandRepository.saveDemand(requestInfo,demands);
	}

}

package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.DemandService;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DemandServiceImpl implements DemandService {

	@Autowired
	DemandRepository demandRepository;

	@Autowired
	private BookingsConfiguration config;

	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	@Autowired
	BookingsCalculatorService bookingsCalculatorService;

	@Autowired
	BookingsUtils bookingsUtils;

	@Autowired
	ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	BookingsCalculatorServiceImpl bookingsCalculator;

	@Override
	public List<Demand> createDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new ArrayList<>();
		
		if(bookingsRequest.getBookingsModel().getBusinessService().equals("OSBM")) {
			 demands = bookingsCalculator.createAndGetCalculationAndDemandForOsbm(bookingsRequest);
		}
		else if(bookingsRequest.getBookingsModel().getBusinessService().equals("BWT")) {
			 demands = bookingsCalculator.createAndGetCalculationAndDemandForBwt(bookingsRequest);
		}
		
		return demandRepository.saveDemand(bookingsRequest.getRequestInfo(), demands);
	}

	@Override
	public List<Demand> updateDemand(BookingsRequest bookingsRequest) {
		
		List<Demand> demands = bookingsCalculator.updateAndGetCalculationAndDemandForOsbm(bookingsRequest);
		

		return demandRepository.updateDemand(bookingsRequest.getRequestInfo(), demands);
	}
	
	public List<Demand> searchDemand(String tenantId, Set<String> consumerCodes, RequestInfo requestInfo,
			String businessService) {
		String uri = bookingsUtils.getDemandSearchURL();
		uri = uri.replace("{1}", tenantId);
		uri = uri.replace("{2}", businessService);
		uri = uri.replace("{3}", StringUtils.join(consumerCodes, ','));

		Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),
				RequestInfoWrapper.builder().requestInfo(requestInfo).build());

		DemandResponse response;
		try {
			response = mapper.convertValue(result, DemandResponse.class);
		} catch (IllegalArgumentException e) {
			throw new CustomException("PARSING ERROR", "Failed to parse response from Demand Search");
		}

		if (CollectionUtils.isEmpty(response.getDemands()))
			return null;

		else
			return response.getDemands();

	}
	

}

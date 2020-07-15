package org.egov.bookings.repository.impl;

import java.util.List;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class DemandRepository {

	
	@Autowired
	private BookingsConfiguration config;
	
	@Autowired
	ServiceRequestRepository serviceRequestRepository;
	
	 @Autowired
	    private ObjectMapper mapper;
	
	public List<Demand> saveDemand(RequestInfo requestInfo, List<Demand> demands) {
		StringBuilder url = new StringBuilder(config.getBillingHost());
        url.append(config.getDemandCreateEndpoint());
        DemandRequest request = new DemandRequest(requestInfo,demands);
        Object result = serviceRequestRepository.fetchResult(url,request);
        DemandResponse response = null;
        try{
            response = mapper.convertValue(result,DemandResponse.class);
        }
        catch(IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Failed to parse response of create demand");
        }
        return response.getDemands();
	}

}

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class DemandRepository.
 */
@Repository
public class DemandRepository {

	
	/** The config. */
	@Autowired
	private BookingsConfiguration config;
	
	/** The service request repository. */
	@Autowired
	ServiceRequestRepository serviceRequestRepository;
	
	 /** The mapper. */
 	@Autowired
	    private ObjectMapper mapper;
	
	/**
	 * Save demand.
	 *
	 * @param requestInfo the request info
	 * @param demands the demands
	 * @return the list
	 */
	public List<Demand> saveDemand(RequestInfo requestInfo, List<Demand> demands) {
		StringBuilder url = new StringBuilder(config.getBillingHost());
        url.append(config.getDemandCreateEndpoint());
        DemandRequest request = new DemandRequest(requestInfo,demands);
        try {
			String writeValueAsString = new ObjectMapper().writeValueAsString(request);
			System.out.println("------------------------------->"+writeValueAsString);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Object result = serviceRequestRepository.fetchResult(url,request);
        DemandResponse response = null;
        try{
            response = mapper.convertValue(result,DemandResponse.class);
            try {
				String writeValueAsString = new ObjectMapper().writeValueAsString(response);
				System.out.println("------------------------------->"+writeValueAsString);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        catch(IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Failed to parse response of create demand");
        }
        return response.getDemands();
	}
	
	/**
	 * Update demand.
	 *
	 * @param requestInfo the request info
	 * @param demands the demands
	 * @return the list
	 * @throws JsonProcessingException 
	 */
	public List<Demand> updateDemand(RequestInfo requestInfo, List<Demand> demands)  {
		StringBuilder url = new StringBuilder(config.getBillingHost());
		 url.append(config.getDemandUpdateEndPoint());
        DemandRequest request = new DemandRequest(requestInfo,demands);
        try {
			String writeValueAsString = new ObjectMapper().writeValueAsString(request);
			System.out.println("------------------------------->"+writeValueAsString);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Object result = serviceRequestRepository.fetchResult(url,request);
        DemandResponse response = null;
        try{
            response = mapper.convertValue(result,DemandResponse.class);
            String writeValueAsString;
			try {
				writeValueAsString = new ObjectMapper().writeValueAsString(response);
				System.out.println("------------------------------->"+writeValueAsString);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
        catch(IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Failed to parse response of create demand");
        }
        return response.getDemands();
	}

}

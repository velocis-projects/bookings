package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.DemandService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class DemandServiceImpl.
 */
@Service
public class DemandServiceImpl implements DemandService {

	/** The demand repository. */
	@Autowired
	DemandRepository demandRepository;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	/** The bookings calculator service. */
	@Autowired
	BookingsCalculatorService bookingsCalculatorService;

	/** The bookings utils. */
	@Autowired
	BookingsUtils bookingsUtils;

	/** The service request repository. */
	@Autowired
	ServiceRequestRepository serviceRequestRepository;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/** The bookings calculator. */
	@Autowired
	BookingsCalculatorServiceImpl bookingsCalculator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.DemandService#createDemand(org.egov.bookings.web.
	 * models.BookingsRequest)
	 */
	@Override
	public List<Demand> createDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new ArrayList<>();

		switch (bookingsRequest.getBookingsModel().getBusinessService()) {

		case BookingsConstants.OSBM:

			demands = getDemandsForOsbm(bookingsRequest);
			break;

		case BookingsConstants.BWT:

			demands = getDemandsForBwt(bookingsRequest);
			break;

		case BookingsConstants.GFCP:	
			demands = getDemandsForGfcp(bookingsRequest);
		}

		return demandRepository.saveDemand(bookingsRequest.getRequestInfo(), demands);

	}

	
	
	/**
	 * Creates the and get calculation and demand for osbm.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the list
	 */
	public List<Demand> getDemandsForOsbm(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getRequestInfo().getUserInfo().getTenantId();

			String taxHeadCode1 = BookingsConstants.OSBM_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsConstants.OSBM_TAXHEAD_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

			Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;
			List<String> combinedBillingSlabs = new LinkedList<>();

			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", "Error while creating demand request body");
		}
		return demands;

	}
	
	
	/**
	 * Creates the and get calculation and demand for bwt.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the list
	 */
	public List<Demand> getDemandsForBwt(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getRequestInfo().getUserInfo().getTenantId();

			String taxHeadCode1 = BookingsConstants.BWT_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsConstants.BWT_TAXHEAD_CODE_2;
			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

			Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;
			List<String> combinedBillingSlabs = new LinkedList<>();

			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", "Error while creating demand request body");
		}
		return demands;

	}
	
	
	private List<Demand> getDemandsForGfcp(BookingsRequest bookingsRequest) {


		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getRequestInfo().getUserInfo().getTenantId();

			String taxHeadCode1 = BookingsConstants.GFCP;

			String taxHeadCode2 = BookingsConstants.GFCP_TAX_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

			Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;
			List<String> combinedBillingSlabs = new LinkedList<>();

			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", "Error while creating demand request body");
		}
		return demands;

	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.DemandService#updateDemand(org.egov.bookings.web.
	 * models.BookingsRequest)
	 */
	@Override
	public List<Demand> updateDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new ArrayList<>();
		switch(bookingsRequest.getBookingsModel().getBusinessService()) {
			
		case BookingsConstants.OSBM :
			demands = bookingsCalculator.updateAndGetCalculationAndDemandForOsbm(bookingsRequest);
			break;
			
		case BookingsConstants.GFCP :
			demands = updateDemandsForGfcp(bookingsRequest);
			break;	
		}
		return demandRepository.updateDemand(bookingsRequest.getRequestInfo(), demands);
	
	}

	private List<Demand> updateDemandsForGfcp(BookingsRequest bookingsRequest) {
		List<Demand> demands = new LinkedList<>();

		String taxHeadCode1 = BookingsConstants.GFCP;

		String taxHeadCode2 = BookingsConstants.GFCP_TAX_CODE_2;

		List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

		

		
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();

	List<Demand> searchResult = searchDemand(requestInfo.getUserInfo().getTenantId(),
			Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo,
			bookingsRequest.getBookingsModel().getBusinessService());
	
	 Demand demand = searchResult.get(0);
     List<DemandDetail> demandDetails = demand.getDemandDetails();
     List<DemandDetail> updatedDemandDetails = getUpdatedDemandDetails(taxHeadEstimate1,demandDetails);
     demand.setDemandDetails(updatedDemandDetails);
     demands.add(demand);
	
	/*taxHeadEstimate1.forEach(taxHeadEstimate -> {
		demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
				.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
				.tenantId(tenantId).build());
	});*/
	
	//demands.add(demands);


	if (CollectionUtils.isEmpty(searchResult)) {
		throw new CustomException("INVALID UPDATE", "No demand exists for applicationNumber: "
				+ bookingsRequest.getBookingsModel().getBkApplicationNumber());
	}
	return demands;
}

	private List<DemandDetail> getUpdatedDemandDetails(List<TaxHeadEstimate> taxHeadEstimate1,
			List<DemandDetail> demandDetails) {

        List<DemandDetail> newDemandDetails = new ArrayList<>();
        Map<String, List<DemandDetail>> taxHeadToDemandDetail = new HashMap<>();

        demandDetails.forEach(demandDetail -> {
            if(!taxHeadToDemandDetail.containsKey(demandDetail.getTaxHeadMasterCode())){
                List<DemandDetail> demandDetailList = new LinkedList<>();
                demandDetailList.add(demandDetail);
                taxHeadToDemandDetail.put(demandDetail.getTaxHeadMasterCode(),demandDetailList);
            }
            else
              taxHeadToDemandDetail.get(demandDetail.getTaxHeadMasterCode()).add(demandDetail);
        });

        BigDecimal diffInTaxAmount;
        List<DemandDetail> demandDetailList;
        BigDecimal total;

        for(TaxHeadEstimate taxHeadEstimate : taxHeadEstimate1){
            if(!taxHeadToDemandDetail.containsKey(taxHeadEstimate.getTaxHeadCode()))
                newDemandDetails.add(
                        DemandDetail.builder()
                                .taxAmount(taxHeadEstimate.getEstimateAmount())
                                .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode())
                                .tenantId("ch")
                                .collectionAmount(BigDecimal.ZERO)
                                .build());
            else {
                 demandDetailList = taxHeadToDemandDetail.get(taxHeadEstimate.getTaxHeadCode());
                 total = demandDetailList.stream().map(DemandDetail::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                 diffInTaxAmount = taxHeadEstimate.getEstimateAmount().subtract(total);
                 if(diffInTaxAmount.compareTo(BigDecimal.ZERO)!=0) {
                     newDemandDetails.add(
                             DemandDetail.builder()
                                     .taxAmount(diffInTaxAmount)
                                     .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode())
                                     .tenantId("ch")
                                     .collectionAmount(BigDecimal.ZERO)
                                     .build());
                 }
            }
        }
        List<DemandDetail> combinedBillDetials = new LinkedList<>(demandDetails);
        combinedBillDetials.addAll(newDemandDetails);
      //  addRoundOffTaxHead(calculation.getTenantId(),combinedBillDetials);
        return combinedBillDetials;
    }

	/**
	 * Search demand.
	 *
	 * @param tenantId        the tenant id
	 * @param consumerCodes   the consumer codes
	 * @param requestInfo     the request info
	 * @param businessService the business service
	 * @return the list
	 */
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

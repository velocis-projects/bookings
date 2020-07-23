package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class BookingsCalculatorServiceImpl implements BookingsCalculatorService {

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
	
	@Override
	public List<Demand> createDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();

		String constructionType = bookingsRequest.getBookingsModel().getBkConstructionType();
		String durationInMonths = bookingsRequest.getBookingsModel().getBkDuration();
		String storage = bookingsRequest.getBookingsModel().getBkAreaRequired();
		String villageCity = bookingsRequest.getBookingsModel().getBkVillCity();
		String residentialCommercial = bookingsRequest.getBookingsModel().getBkType();

		OsbmFeeModel osbmFeeModel = osbmFeeRepository
				.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(villageCity,
						residentialCommercial, storage, durationInMonths, constructionType);

		BigDecimal collectionAmount = null;
		BigDecimal tAmount;
		BigDecimal taxAmount = null;
		if (bookingsRequest.getBookingsModel().getBusinessService().equals("OSBM")) {
			 collectionAmount = new BigDecimal(osbmFeeModel.getAmount());

			 tAmount = new BigDecimal(osbmFeeModel.getAmount() * 1.18);

			 taxAmount = collectionAmount.add(tAmount);
		}

		if (bookingsRequest.getBookingsModel().getBusinessService().equals("BWT")) {
			 collectionAmount = new BigDecimal(350);

			 tAmount = collectionAmount.multiply(new BigDecimal(1.18));

			 taxAmount = collectionAmount.add(tAmount);

		}

		demandDetails.add(DemandDetail.builder().taxAmount(taxAmount)
				.taxHeadMasterCode(bookingsRequest.getBookingsModel().getBusinessService())
				.collectionAmount(collectionAmount)
				.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).build());

		Long taxPeriodFrom = 1554057000000L;
		Long taxPeriodTo = 1869676199000L;
		List<String> combinedBillingSlabs = new LinkedList<>();

		Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
				.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber()).demandDetails(demandDetails)
				.payer(bookingsRequest.getRequestInfo().getUserInfo())
				.minimumAmountPayable(config.getMinimumPayableAmount())
				.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).taxPeriodFrom(taxPeriodFrom)
				.taxPeriodTo(taxPeriodTo).consumerType("bookings")
				.businessService(bookingsRequest.getBookingsModel().getBusinessService())
				.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs)).build();
		demands.add(singleDemand);
		DemandRequest demandRequest = new DemandRequest(bookingsRequest.getRequestInfo(), demands);

		RequestInfo requestInfo = demandRequest.getRequestInfo();
		 demands = demandRequest.getDemands();
		return demandRepository.saveDemand(requestInfo, demands);
	}

	@Override
	public List<Demand> updateDemand(BookingsRequest bookingsRequest) {
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		 List<Demand> demands = new LinkedList<>();
		 
		 List<DemandDetail> demandDetails = new LinkedList<>();
		 
			String constructionType = bookingsRequest.getBookingsModel().getBkConstructionType();
			String durationInMonths = bookingsRequest.getBookingsModel().getBkDuration();
			String storage = bookingsRequest.getBookingsModel().getBkAreaRequired();
			String villageCity = bookingsRequest.getBookingsModel().getBkVillCity();
			String residentialCommercial = bookingsRequest.getBookingsModel().getBkType();
		 
		 OsbmFeeModel osbmFeeModel = osbmFeeRepository
					.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(villageCity,
							residentialCommercial, storage, durationInMonths, constructionType);

		 
		 BigDecimal collectionAmount = null;
			BigDecimal tAmount;
			BigDecimal taxAmount = null;
			
			if (bookingsRequest.getBookingsModel().getBusinessService().equals("OSBM")) {
				 collectionAmount = new BigDecimal(osbmFeeModel.getAmount());

				 tAmount = new BigDecimal(osbmFeeModel.getAmount() * 1.18);

				 taxAmount = collectionAmount.add(tAmount);
			}
		
			demandDetails.add(DemandDetail.builder().taxAmount(taxAmount)
					.taxHeadMasterCode(bookingsRequest.getBookingsModel().getBusinessService())
					.collectionAmount(collectionAmount)
					.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).build());

		List<Demand> searchResult = searchDemand(requestInfo.getUserInfo().getTenantId(),
				Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo, bookingsRequest.getBookingsModel().getBusinessService());
		Demand demand = searchResult.get(0);
		Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
				.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber()).demandDetails(demandDetails)
				.payer(bookingsRequest.getRequestInfo().getUserInfo())
				.minimumAmountPayable(config.getMinimumPayableAmount())
				.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).taxPeriodFrom(demand.getTaxPeriodFrom())
				.taxPeriodTo(demand.getTaxPeriodTo()).consumerType("bookings")
				.businessService(demand.getBusinessService())
				.id(demand.getId())
				.additionalDetails(demand.getAdditionalDetails()).build();
		demands.add(singleDemand);
		
		 
		// List<Demand> updatedDemandDetails = getUpdatedDemandDetails(demandDetails,bookingsRequest);
		 //demands.addAll(updatedDemandDetails);
		if (CollectionUtils.isEmpty(searchResult))
			throw new CustomException("INVALID UPDATE",
					"No demand exists for applicationNumber: " + bookingsRequest.getBookingsModel().getBkApplicationNumber());

          return demandRepository.updateDemand(requestInfo, demands);
	}
	
    private List<Demand> getUpdatedDemandDetails(List<DemandDetail> demandDetails, BookingsRequest bookingsRequest) {
    	List<Demand> demands = new LinkedList<>();

		String constructionType = bookingsRequest.getBookingsModel().getBkConstructionType();
		String durationInMonths = bookingsRequest.getBookingsModel().getBkDuration();
		String storage = bookingsRequest.getBookingsModel().getBkAreaRequired();
		String villageCity = bookingsRequest.getBookingsModel().getBkVillCity();
		String residentialCommercial = bookingsRequest.getBookingsModel().getBkType();

		OsbmFeeModel osbmFeeModel = osbmFeeRepository
				.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(villageCity,
						residentialCommercial, storage, durationInMonths, constructionType);

		BigDecimal collectionAmount = null;
		BigDecimal tAmount;
		BigDecimal taxAmount = null;
		if (bookingsRequest.getBookingsModel().getBusinessService().equals("OSBM")) {
			 collectionAmount = new BigDecimal(osbmFeeModel.getAmount());

			 tAmount = new BigDecimal(osbmFeeModel.getAmount() * 1.18);

			 taxAmount = collectionAmount.add(tAmount);
		}

		if (bookingsRequest.getBookingsModel().getBusinessService().equals("BWT")) {
			 collectionAmount = new BigDecimal(350);

			 tAmount = collectionAmount.multiply(new BigDecimal(1.18));

			 taxAmount = collectionAmount.add(tAmount);

		}

		demandDetails.add(DemandDetail.builder().taxAmount(taxAmount)
				.taxHeadMasterCode(bookingsRequest.getBookingsModel().getBusinessService())
				.collectionAmount(collectionAmount)
				.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).build());

		Long taxPeriodFrom = 1554057000000L;
		Long taxPeriodTo = 1869676199000L;
		List<String> combinedBillingSlabs = new LinkedList<>();

		Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
				.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber()).demandDetails(demandDetails)
				.payer(bookingsRequest.getRequestInfo().getUserInfo())
				.minimumAmountPayable(config.getMinimumPayableAmount())
				.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).taxPeriodFrom(taxPeriodFrom)
				.taxPeriodTo(taxPeriodTo).consumerType("bookings")
				.businessService(bookingsRequest.getBookingsModel().getBusinessService())
				.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs)).build();
		demands.add(singleDemand);
		DemandRequest demandRequest = new DemandRequest(bookingsRequest.getRequestInfo(), demands);

		RequestInfo requestInfo = demandRequest.getRequestInfo();
		 demands = demandRequest.getDemands();
		return demands;
	}

	private List<Demand> searchDemand(String tenantId,Set<String> consumerCodes,RequestInfo requestInfo, String businessService){
        String uri = bookingsUtils.getDemandSearchURL();
        uri = uri.replace("{1}",tenantId);
        uri = uri.replace("{2}",businessService);
        uri = uri.replace("{3}",StringUtils.join(consumerCodes, ','));

        Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),RequestInfoWrapper.builder()
        		                              .requestInfo(requestInfo).build());

        DemandResponse response;
        try {
             response = mapper.convertValue(result,DemandResponse.class);
        }
        catch (IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Failed to parse response from Demand Search");
        }

        if(CollectionUtils.isEmpty(response.getDemands()))
            return null;

        else return response.getDemands();

    }



}

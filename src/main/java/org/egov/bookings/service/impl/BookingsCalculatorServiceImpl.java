package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.TaxHeadMasterFields;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.models.demand.TaxHeadMaster;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.demands.model.enums.Category;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

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

	public List<Demand> createAndGetCalculationAndDemandForOsbm(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getRequestInfo().getUserInfo().getTenantId();

			String taxHeadCode1 = BookingsConstants.OSBM_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsConstants.OSBM_TAXHEAD_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

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

	private List<TaxHeadEstimate> getTaxHeadEstimate(BookingsRequest bookingsRequest, String taxHeadCode1,
			String taxHeadCode2) {
		List<TaxHeadEstimate> taxHeadEstimate1 = new ArrayList<>();
		String bussinessService = bookingsRequest.getBookingsModel().getBusinessService();
		List<TaxHeadMasterFields> taxHeadMasterFieldList = getTaxHeadMasterData(bookingsRequest, bussinessService);

		switch (bussinessService) {

		case BookingsConstants.OSBM:
			BigDecimal osbmAmount = getOsbmAmount(bookingsRequest);
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), osbmAmount, taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							osbmAmount.multiply((taxHeadEstimate.getTaxAmount().divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}

			}
			break;
		case BookingsConstants.BWT:
			BigDecimal bwtAmount = taxHeadMasterFieldList.get(0).getTaxAmount();
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), bwtAmount, taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							bwtAmount.multiply((taxHeadEstimate.getTaxAmount().divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}

			}
			break;
		}
		return taxHeadEstimate1;
	}

	private List<TaxHeadMasterFields> getTaxHeadMasterData(BookingsRequest bookingsRequest, String bussinessService) {

		List<TaxHeadMasterFields> taxHeadMasterFieldList = new ArrayList<>();
		JSONArray mdmsArrayList = null;
		try {
			Object mdmsData = bookingsUtils.prepareMdMsRequestForTaxHeadMaster(bookingsRequest.getRequestInfo());
			String jsonString = mapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = mapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get("BillingService");
			mdmsArrayList = mdmsRes.get("TaxHeadMaster");

			for (int i = 0; i < mdmsArrayList.size(); i++) {
				jsonString = mapper.writeValueAsString(mdmsArrayList.get(i));
				TaxHeadMasterFields taxHeadFields = mapper.readValue(jsonString, TaxHeadMasterFields.class);
				if (taxHeadFields.getService().equals(bussinessService)) {
					taxHeadMasterFieldList.add(taxHeadFields);
				}
			}
		} catch (Exception e) {
			throw new CustomException("MDMS_MASTER_ERROR", "Error while fetching mdms TaxHeadMaster data");
		}
		return taxHeadMasterFieldList;
	}

	private BigDecimal getOsbmAmount(BookingsRequest bookingsRequest) {

		OsbmFeeModel osbmFeeModel = null;
		try {
			String constructionType = bookingsRequest.getBookingsModel().getBkConstructionType();
			String durationInMonths = bookingsRequest.getBookingsModel().getBkDuration();
			String storage = bookingsRequest.getBookingsModel().getBkAreaRequired();
			String villageCity = bookingsRequest.getBookingsModel().getBkVillCity();
			String residentialCommercial = bookingsRequest.getBookingsModel().getBkType();

			osbmFeeModel = osbmFeeRepository
					.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(
							villageCity, residentialCommercial, storage, durationInMonths, constructionType);
		} catch (Exception e) {
			throw new IllegalArgumentException("Exception while fetching osbm amount from database");
		}
		return new BigDecimal(osbmFeeModel.getAmount());
	}

	public List<Demand> updateAndGetCalculationAndDemandForOsbm(BookingsRequest bookingsRequest) {
		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
			String tenantId = bookingsRequest.getRequestInfo().getUserInfo().getTenantId();

			String taxHeadCode1 = BookingsConstants.OSBM_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsConstants.OSBM_TAXHEAD_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

			

			
			RequestInfo requestInfo = bookingsRequest.getRequestInfo();

		List<Demand> searchResult = searchDemand(requestInfo.getUserInfo().getTenantId(),
				Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo,
				bookingsRequest.getBookingsModel().getBusinessService());
		Demand demand = searchResult.get(0);
		taxHeadEstimate1.forEach(taxHeadEstimate -> {
			demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
					.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
					.tenantId(tenantId).build());
		});
		
		
		Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
				.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber()).demandDetails(demandDetails)
				.payer(bookingsRequest.getRequestInfo().getUserInfo())
				.minimumAmountPayable(config.getMinimumPayableAmount())
				.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId())
				.taxPeriodFrom(demand.getTaxPeriodFrom()).taxPeriodTo(demand.getTaxPeriodTo()).consumerType("bookings")
				.businessService(demand.getBusinessService()).id(demand.getId())
				.additionalDetails(demand.getAdditionalDetails()).build();
		
		demands.add(singleDemand);

	
		if (CollectionUtils.isEmpty(searchResult)) {
			throw new CustomException("INVALID UPDATE", "No demand exists for applicationNumber: "
					+ bookingsRequest.getBookingsModel().getBkApplicationNumber());
		}
		return demands;
	}

	public List<Demand> createAndGetCalculationAndDemandForBwt(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getRequestInfo().getUserInfo().getTenantId();

			String taxHeadCode1 = BookingsConstants.BWT_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsConstants.BWT_TAXHEAD_CODE_2;
			List<TaxHeadEstimate> taxHeadEstimate1 = getTaxHeadEstimate(bookingsRequest, taxHeadCode1, taxHeadCode2);

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

}

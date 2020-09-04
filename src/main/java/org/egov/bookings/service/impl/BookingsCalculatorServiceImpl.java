package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.TaxHeadMasterFields;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.models.demand.TaxHeadMaster;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.CommercialGroundService;
import org.egov.bookings.service.OsujmService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.utils.BookingsCalculatorConstants;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
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

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsCalculatorServiceImpl.
 */
@Service
@Transactional
public class BookingsCalculatorServiceImpl implements BookingsCalculatorService {

	/** The demand repository. */
	@Autowired
	private DemandRepository demandRepository;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	/** The bookings calculator service. */
	@Autowired
	private BookingsCalculatorService bookingsCalculatorService;

	/** The bookings utils. */
	@Autowired
	private BookingsUtils bookingsUtils;

	/** The service request repository. */
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private CommercialGroundService commercialGroundService;

	@Autowired
	private OsujmService osujmService;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	private ParkAndCommunityService parkAndCommunityService;
	
	
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

	/**
	 * Gets the tax head estimate.
	 *
	 * @param bookingsRequest the bookings request
	 * @param taxHeadCode1    the tax head code 1
	 * @param taxHeadCode2    the tax head code 2
	 * @return the tax head estimate
	 */
	public List<TaxHeadEstimate> getTaxHeadEstimate(BookingsRequest bookingsRequest, String taxHeadCode1,
			String taxHeadCode2) {
		List<TaxHeadEstimate> taxHeadEstimate1 = new ArrayList<>();
		String bussinessService = bookingsRequest.getBookingsModel().getBusinessService();
		List<TaxHeadMasterFields> taxHeadMasterFieldList = getTaxHeadMasterData(bookingsRequest, bussinessService);

		switch (bussinessService) {

		case BookingsConstants.BUSINESS_SERVICE_OSBM:
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
		case BookingsConstants.BUSINESS_SERVICE_BWT:
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
		case BookingsConstants.BUSINESS_SERVICE_GFCP:
			BigDecimal commercialAmount = getCommercialAmount(bookingsRequest);
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), commercialAmount,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							commercialAmount.multiply((taxHeadEstimate.getTaxAmount().divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}
				

			}
			break;
		case BookingsConstants.BUSINESS_SERVICE_OSUJM:
			BigDecimal mccJurisdictionAmount = getJurisdicationAmount(bookingsRequest);
			//BigDecimal mccJurisdictionAmount = new BigDecimal(4400);
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), mccJurisdictionAmount,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							mccJurisdictionAmount.multiply((taxHeadEstimate.getTaxAmount().divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}
			}
			break;
			
		case BookingsConstants.BUSINESS_SERVICE_PACC:
			ParkCommunityHallV1MasterModel parkCommunityHallV1FeeMaster = getParkAndCommunityAmount(bookingsRequest);
			BigDecimal days = enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
			BigDecimal amount = BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getCleaningCharges())+Long.valueOf(parkCommunityHallV1FeeMaster.getRent()));
			BigDecimal finalAmount = days.multiply(amount);
		//	BigDecimal finalAmount = new BigDecimal(4400);
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), finalAmount,
							taxHeadEstimate.getCategory()));
				}
				/*if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							finalAmount.multiply((BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getSurcharge())).divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}*/
				if (taxHeadEstimate.getCode().equals(BookingsCalculatorConstants.PACC_CGST)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							finalAmount.multiply((BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getCgstRate())).divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(BookingsCalculatorConstants.PACC_UGST)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							finalAmount.multiply((BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getUtgstRate())).divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}
			}
			break;
			
		}
		return taxHeadEstimate1;
	}

	private ParkCommunityHallV1MasterModel getParkAndCommunityAmount(BookingsRequest bookingsRequest) {

		ParkCommunityHallV1MasterModel parkCommunityHallV1FeeMaster = null;
		try {
			 parkCommunityHallV1FeeMaster = parkAndCommunityService
					.findParkAndCommunityFee(bookingsRequest.getBookingsModel().getBkBookingVenue());
			if(BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1FeeMaster)) {
				throw new IllegalArgumentException("There is not any amount for this park and community criteria in database");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return parkCommunityHallV1FeeMaster;
	}

	public BigDecimal getJurisdicationAmount(BookingsRequest bookingsRequest) {

		OsujmFeeModel osujmFeeModel = null;
		Long area = null;
		BigDecimal finalAmount = null;
		try {
			area = Long.valueOf(bookingsRequest.getBookingsModel().getBkAreaRequired());
			/*OsujmFeeModel commercialGroundFeeSearchCriteria = CommercialGroundFeeSearchCriteria
					.builder().bookingVenue(bookingVenue).category(category).build();*/
			osujmFeeModel = osujmService
					.findJurisdictionFee(bookingsRequest);
			if(BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel)) {
				throw new IllegalArgumentException("There is not any amount for this mcc jurisdiction criteria in database");
			}
			BigDecimal days = enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
			finalAmount = days.multiply(new BigDecimal(osujmFeeModel.getRatePerSqrFeetPerDay()*area));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return finalAmount;
	}

	/**
	 * Gets the tax head master data.
	 *
	 * @param bookingsRequest  the bookings request
	 * @param bussinessService the bussiness service
	 * @return the tax head master data
	 */
	public List<TaxHeadMasterFields> getTaxHeadMasterData(BookingsRequest bookingsRequest, String bussinessService) {

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

	/**
	 * Gets the osbm amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the osbm amount
	 */
	public BigDecimal getOsbmAmount(BookingsRequest bookingsRequest) {

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
		if(BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel)) {
			throw new CustomException("DATA_NOT_FOUND","There is not any amount for this osbm criteria in database");
		}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Exception while fetching osbm amount from database");
		}
		return new BigDecimal(osbmFeeModel.getAmount());
	}

	/**
	 * Gets the commercial amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the commercial amount
	 */
	public BigDecimal getCommercialAmount(BookingsRequest bookingsRequest) {
		CommercialGroundFeeModel commercialGroundFeeModel = null;
		BigDecimal finalAmount = null;
		try {
			String category = bookingsRequest.getBookingsModel().getBkCategory();
			String bookingVenue = bookingsRequest.getBookingsModel().getBkBookingVenue();
			CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria = CommercialGroundFeeSearchCriteria
					.builder().bookingVenue(bookingVenue).category(category).build();
			commercialGroundFeeModel = commercialGroundService
					.searchCommercialGroundFee(commercialGroundFeeSearchCriteria);
			if(BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel)) {
				throw new CustomException("DATA_NOT_FOUND","There is not any amount for this commercial ground criteria in database");
			}
			BigDecimal days = enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
			 finalAmount = days.multiply(BigDecimal.valueOf(commercialGroundFeeModel.getRatePerDay())); 
			
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return finalAmount;
	}

}

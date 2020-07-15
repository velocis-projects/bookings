package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.IdResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.AuditDetails;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;

@Service
public class EnrichmentService {

	@Autowired
	private BookingsUtils bookingsUtils;

	@Autowired
	private BookingsConfiguration config;

	@Autowired
	private IdGenRepository idGenRepository;

	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	@Autowired
	BookingsCalculatorService bookingsCalculatorService;

	public void enrichBookingsCreateRequest(BookingsRequest bookingsRequest) {
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		/*
		 * AuditDetails auditDetails = bookingsUtil
		 * .getAuditDetails(requestInfo.getUserInfo().getUuid(), true);
		 */
		setIdgenIds(bookingsRequest);
		// setStatusForCreate(tradeLicenseRequest);
		// boundaryService.getAreaType(tradeLicenseRequest,config.getHierarchyTypeCode());
	}

	private void setIdgenIds(BookingsRequest bookingsRequest) {
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		String tenantId = bookingsRequest.getBookingsModel().getTenantId();

		BookingsModel bookingsModel = bookingsRequest.getBookingsModel();

		List<String> applicationNumbers = getIdList(requestInfo, tenantId, config.getApplicationNumberIdgenName(),
				config.getApplicationNumberIdgenFormat());
		ListIterator<String> itr = applicationNumbers.listIterator();

		Map<String, String> errorMap = new HashMap<>();
		/*
		 * if (applicationNumbers.size() != bookingsRequest.getBookingsModel().size()) {
		 * errorMap.put("IDGEN ERROR ",
		 * "The number of LicenseNumber returned by idgen is not equal to number of TradeLicenses"
		 * ); }
		 */

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);

		bookingsModel.setBkApplicationNumber(itr.next());
	}

	private List<String> getIdList(RequestInfo requestInfo, String tenantId, String idKey, String idformat) {
		List<IdResponse> idResponses = idGenRepository.getId(requestInfo, tenantId, idKey, idformat).getIdResponses();

		if (CollectionUtils.isEmpty(idResponses))
			throw new CustomException("IDGEN ERROR", "No ids returned from idgen Service");

		return idResponses.stream().map(IdResponse::getId).collect(Collectors.toList());
	}

	public void enrichBookingsDetails(BookingsRequest bookingsRequest) {
		bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		java.sql.Date date = bookingsUtils.getCurrentSqlDate();
		bookingsRequest.getBookingsModel().setBkDateCreated(date);
	}

	public void generateDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();

		if (bookingsRequest.getBookingsModel().getBusinessService() == "OSBM") {

			String constructionType = bookingsRequest.getBookingsModel().getBkConstructionType();
			String durationInMonths = bookingsRequest.getBookingsModel().getBkDuration();
			String storage = bookingsRequest.getBookingsModel().getBkAreaRequired();
			String villageCity = bookingsRequest.getBookingsModel().getBkVillCity();
			String residentialCommercial = bookingsRequest.getBookingsModel().getBkType();

			 OsbmFeeModel osbmFeeModel = osbmFeeRepository.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(villageCity,residentialCommercial,storage,durationInMonths,constructionType);

			 
			 BigDecimal collectionAmount = new BigDecimal(osbmFeeModel.getAmount());
			 
			 BigDecimal tAmount = new BigDecimal(osbmFeeModel.getAmount()*1.18);
			 
			// BigDecimal taxAmount = new BigDecimal(tAmount+collectionAmount);
			 
			 
			 
			demandDetails.add(DemandDetail.builder().taxAmount(new BigDecimal(118.0))
					.taxHeadMasterCode(bookingsRequest.getBookingsModel().getBusinessService())
					.collectionAmount(new BigDecimal(100.0))
					.tenantId(bookingsRequest.getRequestInfo().getUserInfo().getTenantId()).build());

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
			DemandRequest demandRequest = new DemandRequest(bookingsRequest.getRequestInfo(), demands);
			bookingsCalculatorService.createDemand(demandRequest);
		}

	}

}

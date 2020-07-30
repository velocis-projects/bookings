package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.AuditDetails;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;

// TODO: Auto-generated Javadoc
/**
 * The Class EnrichmentService.
 */
@Service
public class EnrichmentService {

	/** The bookings utils. */
	@Autowired
	private BookingsUtils bookingsUtils;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The id gen repository. */
	@Autowired
	private IdGenRepository idGenRepository;

	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	/** The bookings calculator service. */
	@Autowired
	BookingsCalculatorService bookingsCalculatorService;
	
	/** The bookings service. */
	@Autowired
	BookingsService bookingsService;
	
	/** The demand service. */
	@Autowired
	DemandServiceImpl demandService;
	
	/** The bookings repository. */
	@Autowired
	private BookingsRepository bookingsRepository;

	/**
	 * Enrich bookings create request.
	 *
	 * @param bookingsRequest the bookings request
	 */
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

	/**
	 * Sets the idgen ids.
	 *
	 * @param bookingsRequest the new idgen ids
	 */
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

	/**
	 * Gets the id list.
	 *
	 * @param requestInfo the request info
	 * @param tenantId the tenant id
	 * @param idKey the id key
	 * @param idformat the idformat
	 * @return the id list
	 */
	private List<String> getIdList(RequestInfo requestInfo, String tenantId, String idKey, String idformat) {
		List<IdResponse> idResponses = idGenRepository.getId(requestInfo, tenantId, idKey, idformat).getIdResponses();

		if (CollectionUtils.isEmpty(idResponses))
			throw new CustomException("IDGEN ERROR", "No ids returned from idgen Service");

		return idResponses.stream().map(IdResponse::getId).collect(Collectors.toList());
	}

	/**
	 * Enrich bookings details.
	 *
	 * @param bookingsRequest the bookings request
	 */
	public void enrichBookingsDetails(BookingsRequest bookingsRequest) {
		bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		java.sql.Date date = bookingsUtils.getCurrentSqlDate();
		bookingsRequest.getBookingsModel().setBkDateCreated(date);
	}

	/**
	 * Generate demand.
	 *
	 * @param bookingsRequest the bookings request
	 */
	public void generateDemand(BookingsRequest bookingsRequest) {

		if (bookingsRequest.getBookingsModel().getBusinessService().equals("OSBM")) {
			if(!bookingsService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber())) {
				demandService.createDemand(bookingsRequest);
			} else
				demandService.updateDemand(bookingsRequest);
		}

		else if (bookingsRequest.getBookingsModel().getBusinessService().equals("BWT")) {
			if(!bookingsService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber())) {
				demandService.createDemand(bookingsRequest);
			}
		}

	}

	/**
	 * Enrich osbm details.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel enrichOsbmDetails(BookingsRequest bookingsRequest) {
		BookingsModel bookingsModel = null;
		try {
			bookingsModel = bookingsRepository
					.findByBkApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
			if (null != bookingsModel) {
				LocalDate bkFromDate = LocalDate.now();
				Date fromDate = java.sql.Date.valueOf(bkFromDate);
				LocalDate bkToDate = LocalDate.now().plusMonths(Integer.valueOf(bookingsModel.getBkDuration()));
				Date toDate = java.sql.Date.valueOf(bkToDate);
				bookingsModel.setBkApplicationStatus(bookingsRequest.getBookingsModel().getBkApplicationStatus());
				bookingsModel.setBkAction(bookingsRequest.getBookingsModel().getBkAction());
				bookingsModel.setBookingsRemarks(bookingsRequest.getBookingsModel().getBookingsRemarks());
				bookingsModel.setBkFromDate(fromDate);
				bookingsModel.setBkToDate(toDate);
			}
		} catch (Exception e) {
			throw new CustomException("OSBM UPDATE ERROR", "ERROR WHILE UPDATING OSBM DETAILS ");
		}
		return bookingsModel;
	}
	
	/**
	 * Enrich bwt details.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel enrichBwtDetails(BookingsRequest bookingsRequest) {
		BookingsModel bookingsModel = null;
		try {
			bookingsModel = bookingsRepository
					.findByBkApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
			bookingsModel.setBkApplicationStatus(bookingsRequest.getBookingsModel().getBkApplicationStatus());
			bookingsModel.setBkAction(bookingsRequest.getBookingsModel().getBkAction());
			bookingsModel.setBookingsRemarks(bookingsRequest.getBookingsModel().getBookingsRemarks());
			bookingsModel.setBkContactNo(bookingsRequest.getBookingsModel().getBkContactNo());
			bookingsModel.setBkDriverName(bookingsRequest.getBookingsModel().getBkDriverName());
			bookingsModel.setBkApproverName(bookingsRequest.getBookingsModel().getBkApproverName());
		} catch (Exception e) {
			throw new CustomException("WATER TANKER UPDATE ERROR", "ERROR WHILE UPDATING BWT DETAILS ");
		}
		return bookingsModel;
	}

}

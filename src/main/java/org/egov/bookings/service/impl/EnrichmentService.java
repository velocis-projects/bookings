package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Bill;
import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.contract.BookingsRequestKafka;
import org.egov.bookings.contract.IdResponse;
import org.egov.bookings.contract.NewLocationKafkaRequest;
import org.egov.bookings.contract.ParkCommunityFeeMasterResponse;
import org.egov.bookings.contract.TaxHeadMasterFields;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.models.demand.GenerateBillCriteria;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.OsujmNewLocationRepository;
import org.egov.bookings.repository.impl.BillingServiceRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.DemandService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.NewLocationRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
	
	/** The bookings calculator service. */
	@Autowired
	BookingsCalculatorService bookingsCalculatorService;
	
	/** The bookings service. */
	@Autowired
	BookingsService bookingsService;
	
	/** The demand service. */
	@Autowired
	DemandService demandService;
	
	/** The bookings repository. */
	@Autowired
	private BookingsRepository bookingsRepository;
	
	/** The osujm new location repository. */
	@Autowired
	private OsujmNewLocationRepository osujmNewLocationRepository;

	/** The billing service repository. */
	@Autowired
	private BillingServiceRepository billingServiceRepository;
	
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
	 * Enrich new location create request.
	 *
	 * @param newLocationRequest the new location request
	 */
	public void enrichNewLocationCreateRequest(NewLocationRequest newLocationRequest) {
		RequestInfo requestInfo = newLocationRequest.getRequestInfo();
		/*
		 * AuditDetails auditDetails = bookingsUtil
		 * .getAuditDetails(requestInfo.getUserInfo().getUuid(), true);
		 */
		
			setIdgenIdsForNewLocation(newLocationRequest);
		// setStatusForCreate(tradeLicenseRequest);
		// boundaryService.getAreaType(tradeLicenseRequest,config.getHierarchyTypeCode());
	}

	

	/**
	 * Sets the idgen ids for new location.
	 *
	 * @param newLocationRequest the new idgen ids for new location
	 */
	private void setIdgenIdsForNewLocation(NewLocationRequest newLocationRequest) {
		RequestInfo requestInfo = newLocationRequest.getRequestInfo();
		String tenantId = newLocationRequest.getNewLocationModel().getTenantId();

		OsujmNewLocationModel newLocationModel = newLocationRequest.getNewLocationModel();

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

		newLocationModel.setApplicationNumber(itr.next());
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
		try {
		bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		java.sql.Date date = bookingsUtils.getCurrentSqlDate();
		bookingsRequest.getBookingsModel().setBkDateCreated(date);
		}catch (Exception e) {
			throw new CustomException("INVALID_BOOKING_REQUEST", e.getMessage());
		}
	}

	/**
	 * Generate demand.
	 *
	 * @param bookingsRequest the bookings request
	 */
	public void generateDemand(BookingsRequest bookingsRequest) {

		if (!BookingsConstants.BUSINESS_SERVICE_BWT.equals(bookingsRequest.getBookingsModel().getBusinessService())) {
			if (!bookingsService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber())) {
				demandService.createDemand(bookingsRequest);
			} else
				demandService.updateDemand(bookingsRequest);
		} else {
			if (!bookingsService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber())) {
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

				bookingsModel.setBkApplicationStatus(bookingsRequest.getBookingsModel().getBkApplicationStatus());
				bookingsModel.setBkAction(bookingsRequest.getBookingsModel().getBkAction());
				bookingsModel.setBkRemarks(bookingsRequest.getBookingsModel().getBkRemarks());
				if (BookingsConstants.PAY.equals(bookingsRequest.getBookingsModel().getBkAction())) {
					LocalDate bkFromDate = LocalDate.now();
					Date fromDate = java.sql.Date.valueOf(bkFromDate);
					LocalDate bkToDate = LocalDate.now().plusMonths(Integer.valueOf(bookingsModel.getBkDuration()));
					Date toDate = java.sql.Date.valueOf(bkToDate);
					bookingsModel.setBkFromDate(fromDate);
					bookingsModel.setBkToDate(toDate);
					if(!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getBkPaymentStatus()))
					bookingsModel.setBkPaymentStatus(bookingsRequest.getBookingsModel().getBkPaymentStatus());
				}

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
			bookingsModel.setBkRemarks(bookingsRequest.getBookingsModel().getBkRemarks());
			bookingsModel.setBkContactNo(bookingsRequest.getBookingsModel().getBkContactNo());
			bookingsModel.setBkDriverName(bookingsRequest.getBookingsModel().getBkDriverName());
			bookingsModel.setBkApproverName(bookingsRequest.getBookingsModel().getBkApproverName());
			if(!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
				bookingsModel.setBkPaymentStatus(bookingsRequest.getBookingsModel().getBkPaymentStatus());
			}
		} catch (Exception e) {
			throw new CustomException("WATER TANKER UPDATE ERROR", "ERROR WHILE UPDATING BWT DETAILS ");
		}
		return bookingsModel;
	}



	/**
	 * Enrich new location details.
	 *
	 * @param newLocationRequest the new location request
	 */
	public void enrichNewLocationDetails(NewLocationRequest newLocationRequest) {
		newLocationRequest.getNewLocationModel().setUuid(newLocationRequest.getRequestInfo().getUserInfo().getUuid());
		java.sql.Date date = bookingsUtils.getCurrentSqlDate();
		newLocationRequest.getNewLocationModel().setDateCreated(date);
	}



	/**
	 * Enrich nlujm details.
	 *
	 * @param newLocationRequest the new location request
	 * @return the osujm new location model
	 */
	public OsujmNewLocationModel enrichNlujmDetails(NewLocationRequest newLocationRequest) {
		OsujmNewLocationModel osujmNewLocationModel = null;
		try {
			osujmNewLocationModel = osujmNewLocationRepository
					.findByApplicationNumber(newLocationRequest.getNewLocationModel().getApplicationNumber());
			osujmNewLocationModel.setApplicationStatus(newLocationRequest.getNewLocationModel().getApplicationStatus());
			osujmNewLocationModel.setAction(newLocationRequest.getNewLocationModel().getAction());
			osujmNewLocationModel.setRemarks(newLocationRequest.getNewLocationModel().getRemarks());
		} catch (Exception e) {
			throw new CustomException("NLUJM UPDATE ERROR", "ERROR WHILE UPDATING NLUJM DETAILS ");
		}
		return osujmNewLocationModel;
	}



	/**
	 * Extract days between two dates.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the big decimal
	 */
	public BigDecimal extractDaysBetweenTwoDates(BookingsRequest bookingsRequest) {
			try {
				LocalDate dateBefore = LocalDate.parse(bookingsRequest.getBookingsModel().getBkFromDate()+"");
				LocalDate dateAfter = LocalDate.parse(bookingsRequest.getBookingsModel().getBkToDate()+"");
				long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
				long totalDays = noOfDaysBetween+1;
				BigDecimal finalAmount = BigDecimal.valueOf(totalDays);
				return finalAmount;
			}catch (Exception e) {
				throw new CustomException("INVALID_REQUEST",e.getLocalizedMessage());
			}
	}



	/**
	 * Enrich osujm details.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel enrichOsujmDetails(BookingsRequest bookingsRequest) {
		BookingsModel bookingsModel = null;
		try {
			bookingsModel = bookingsRepository
					.findByBkApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
			bookingsModel.setBkApplicationStatus(bookingsRequest.getBookingsModel().getBkApplicationStatus());
			bookingsModel.setBkAction(bookingsRequest.getBookingsModel().getBkAction());
			bookingsModel.setBkRemarks(bookingsRequest.getBookingsModel().getBkRemarks());
			if(!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
				bookingsModel.setBkPaymentStatus(bookingsRequest.getBookingsModel().getBkPaymentStatus());
			}
			
		} catch (Exception e) {
			throw new CustomException("OSUJM UPDATE ERROR", "ERROR WHILE UPDATING OSUJM DETAILS ");
		}
		return bookingsModel;
	}



	/**
	 * Enrich booked dates.
	 *
	 * @param bookingsModel the bookings model
	 * @return the list
	 */
	public List<LocalDate> enrichBookedDates(Set<BookingsModel> bookingsModel) {
		List<LocalDate> listOfDates = new ArrayList<>();

			for (BookingsModel bookingsModel1 : bookingsModel) {
				LocalDate startDate = LocalDate.parse(bookingsModel1.getBkFromDate() + "");
				LocalDate endDate = LocalDate.parse(bookingsModel1.getBkToDate() + "");
				long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

				List<LocalDate>listOfDates2 = LongStream.range(0, numOfDays).mapToObj(startDate::plusDays)
						.collect(Collectors.toList());
				listOfDates.addAll(listOfDates2);
				listOfDates.add(endDate);
			}

		return listOfDates;
	}

	/**
	 * Extract all dates between two dates.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the list
	 */
	public List<LocalDate> extractAllDatesBetweenTwoDates(BookingsRequest bookingsRequest) {
		LocalDate startDate = LocalDate.parse(bookingsRequest.getBookingsModel().getBkFromDate() + "");
		LocalDate endDate = LocalDate.parse(bookingsRequest.getBookingsModel().getBkToDate() + "");
		long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

		List<LocalDate> listOfDates2 = LongStream.range(0, numOfDays).mapToObj(startDate::plusDays)
				.collect(Collectors.toList());
		listOfDates2.add(endDate);
		return listOfDates2;

	}



	/**
	 * Enrich pacc details.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel enrichPaccDetails(BookingsRequest bookingsRequest) {
		BookingsModel bookingsModel = null;
		try {
			bookingsModel = bookingsRepository
					.findByBkApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
			bookingsModel.setBkApplicationStatus(bookingsRequest.getBookingsModel().getBkApplicationStatus());
			bookingsModel.setBkAction(bookingsRequest.getBookingsModel().getBkAction());
			bookingsModel.setBkRemarks(bookingsRequest.getBookingsModel().getBkRemarks());
			bookingsModel.setTimeslots(bookingsRequest.getBookingsModel().getTimeslots());
			if(!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
				bookingsModel.setBkPaymentStatus(bookingsRequest.getBookingsModel().getBkPaymentStatus());
			}
		} catch (Exception e) {
			throw new CustomException("PACC UPDATE ERROR", "ERROR WHILE UPDATING PACC DETAILS ");
		}
		return bookingsModel;
	}



	/**
	 * Enrich park community amount.
	 *
	 * @param parkCommunityHallFee the park community hall fee
	 * @return the park community fee master response
	 */
	public ParkCommunityFeeMasterResponse enrichParkCommunityAmount(
			ParkCommunityHallV1MasterModel parkCommunityHallFee) {
		ParkCommunityFeeMasterResponse parkCommunityFeeMasterResponse = new ParkCommunityFeeMasterResponse();
		try {
			BigDecimal amount = BigDecimal.valueOf(Long.valueOf(parkCommunityHallFee.getRent())
					+ Long.valueOf(parkCommunityHallFee.getCleaningCharges()));
			BigDecimal surchargeAmount = amount.multiply(
					BigDecimal.valueOf(Long.valueOf(parkCommunityHallFee.getSurcharge())).divide(new BigDecimal(100)));
			BigDecimal finalAmount = amount.add(surchargeAmount);
			
			BigDecimal finalBill = BookingsUtils.removeRoundOff(finalAmount);
			
			BigDecimal cgstAmount = amount.multiply(
					BigDecimal.valueOf(Long.valueOf(parkCommunityHallFee.getCgstRate())).divide(new BigDecimal(100)));
			BigDecimal ugstAmount = amount.multiply(
					BigDecimal.valueOf(Long.valueOf(parkCommunityHallFee.getUtgstRate())).divide(new BigDecimal(100)));
			parkCommunityFeeMasterResponse.setTotalAmount(finalBill);
			parkCommunityFeeMasterResponse.setAmount(amount);
			parkCommunityFeeMasterResponse.setSurchargeAmount(surchargeAmount);
			parkCommunityFeeMasterResponse.setCgstAmount(cgstAmount);
			parkCommunityFeeMasterResponse.setUgstAmount(ugstAmount);
			return parkCommunityFeeMasterResponse;
		} catch (Exception e) {
			throw new CustomException("FEE_MASTER_ERROR", "ERROR WHILE ENRICHING PARK AND COMMUNITY FEE RESPONSE");
		}
	}



	/**
	 * Enrich bookings assignee.
	 *
	 * @param bookingsRequest the bookings request
	 */
	public void enrichBookingsAssignee(BookingsRequest bookingsRequest) {
		String businessService = bookingsRequest.getBookingsModel().getBusinessService();
		SearchCriteriaFieldsDTO searchCriteriaFieldsDTO = new SearchCriteriaFieldsDTO();
			searchCriteriaFieldsDTO.setApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
			searchCriteriaFieldsDTO.setAction(BookingsConstants.ACTION_INITIATE);
			if(!BookingsConstants.BUSINESS_SERVICE_BWT.equals(businessService) && !BookingsConstants.BUSINESS_SERVICE_GFCP.equals(businessService) && !BookingsConstants.BUSINESS_SERVICE_PACC.equals(businessService));
			searchCriteriaFieldsDTO.setSector(bookingsRequest.getBookingsModel().getBkSector());
			searchCriteriaFieldsDTO.setRequestInfo(bookingsRequest.getRequestInfo());
		List<UserDetails> userdetailsList = bookingsService.getAssignee(searchCriteriaFieldsDTO);
		 if(!BookingsFieldsValidator.isNullOrEmpty(userdetailsList))
		 bookingsRequest.getBookingsModel().setAssignee(userdetailsList.get(0).getUuid());
	}



	/**
	 * Enrich for kafka.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings request kafka
	 */
	public BookingsRequestKafka enrichForKafka(BookingsRequest bookingsRequest) {
		List<BookingsModel> bModel = new ArrayList<>();
		bModel.add(bookingsRequest.getBookingsModel());
		BookingsRequestKafka kafkaBookingRequest =  BookingsRequestKafka.builder().bookingsModel(bModel).requestInfo(bookingsRequest.getRequestInfo()).build();
		return kafkaBookingRequest;
	}



	/**
	 * Enrich kafka for new location.
	 *
	 * @param newLocationRequest the new location request
	 * @return the new location kafka request
	 */
	public NewLocationKafkaRequest enrichKafkaForNewLocation(NewLocationRequest newLocationRequest) {
		List<OsujmNewLocationModel> sujmNewLocationModelList = new ArrayList<>();
		sujmNewLocationModelList.add(newLocationRequest.getNewLocationModel());
		NewLocationKafkaRequest kafkaNewLocationRequest =  NewLocationKafkaRequest.builder().newLocationModel(sujmNewLocationModelList).requestInfo(newLocationRequest.getRequestInfo()).build();
		return kafkaNewLocationRequest;
	}



	/**
	 * Enrich park community create request.
	 *
	 * @param bookingsRequest the bookings request
	 */
	public void enrichParkCommunityCreateRequest(BookingsRequest bookingsRequest) {
		// TODO Auto-generated method stub

		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		String tenantId = bookingsRequest.getBookingsModel().getTenantId();

		BookingsModel bookingsModel = bookingsRequest.getBookingsModel();

		List<String> applicationNumbers = getIdList(requestInfo, tenantId, config.getApplicationNumberIdgenName(),
				config.getApplicationNumberIdgenFormat());
		ListIterator<String> itr = applicationNumbers.listIterator();

		Map<String, String> errorMap = new HashMap<>();
	
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
		bookingsModel.setBkApplicationNumber(itr.next());
		if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getTimeslots())) {
			bookingsRequest.getBookingsModel().getTimeslots().forEach(slots -> {
				slots.setId(UUID.randomUUID().toString());
				slots.setApplicationNumber(bookingsModel.getBkApplicationNumber());
			});
		}
	}



	/**
	 * Enrich pacc payment details.
	 *
	 * @param bookingsRequest the bookings request
	 */
	public void enrichPaccPaymentDetails(BookingsRequest bookingsRequest) {
		String businessService = bookingsRequest.getBookingsModel().getBusinessService();
		if (BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& BookingsConstants.BUSINESS_SERVICE_PACC.equals(businessService)) {
			config.setParkAndCommunityLock(true);
			if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
				bookingsRequest.getBookingsModel()
						.setBkPaymentStatus(bookingsRequest.getBookingsModel().getBkPaymentStatus());
			}
		}
	}

	/**
	 * Enrich tax head estimate for PACC.
	 *
	 * @param bookingsRequest the bookings request
	 * @param finalAmount the final amount
	 * @param taxHeadCode1 the tax head code 1
	 * @param taxHeadCode2 the tax head code 2
	 * @param taxHeadMasterFieldList the tax head master field list
	 * @param parkCommunityHallV1FeeMaster the park community hall V 1 fee master
	 * @return the list
	 */
	private List<TaxHeadEstimate> enrichTaxHeadEstimateForPACC(BookingsRequest bookingsRequest, BigDecimal finalAmount, String taxHeadCode1,
			String taxHeadCode2, List<TaxHeadMasterFields> taxHeadMasterFieldList,
			ParkCommunityHallV1MasterModel parkCommunityHallV1FeeMaster) {

		List<TaxHeadEstimate> taxHeadEstimate1 = new ArrayList<>();
		for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
			if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
				taxHeadEstimate1.add(
						new TaxHeadEstimate(taxHeadEstimate.getCode(), finalAmount, taxHeadEstimate.getCategory()));
				continue;
			}
			if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
				taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
						finalAmount
								.multiply((BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getSurcharge()))
										.divide(new BigDecimal(100)))),
						taxHeadEstimate.getCategory()));
				continue;
			}
			if (taxHeadEstimate.getCode().equals(BookingsConstants.PACC_TAXHEAD_CODE_LUXURY_TAX)) {
				taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
						BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getLuxuryTax())),
						taxHeadEstimate.getCategory()));
				continue;
			}
			if (taxHeadEstimate.getCode().equals(BookingsConstants.PACC_TAXHEAD_CODE_REFUNDABLE_SECURITY_AMOUNT)) {
				taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
						BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getRefundabelSecurity())),
						taxHeadEstimate.getCategory()));
				continue;
			}
			
			if(bookingsRequest.getRequestInfo().getUserInfo().getType().equals(BookingsConstants.EMPLOYEE)) {
			if (taxHeadEstimate.getCode().equals(BookingsConstants.PACC_TAXHEAD_CODE_FACILITATION_CHARGE)) {
				taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
						taxHeadEstimate.getFacilitationCharge(),
						taxHeadEstimate.getCategory()));
				}
			continue;
			}
			if (BookingsConstants.PAYMENT_SUCCESS_STATUS
					.equals(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
				if (taxHeadEstimate.getCode().equals(BookingsConstants.PACC_TAXHEAD_CODE_3)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getLocationChangeAmount())),
							taxHeadEstimate.getCategory()));
				}
				break;
			}
		}
		return taxHeadEstimate1;
	}


	/**
	 * Enrich pacc amount for booking change.
	 *
	 * @param bookingsRequest the bookings request
	 * @param finalAmount the final amount
	 * @param taxHeadCode1 the tax head code 1
	 * @param taxHeadCode2 the tax head code 2
	 * @param taxHeadMasterFieldList the tax head master field list
	 * @param parkCommunityHallV1FeeMaster the park community hall V 1 fee master
	 * @return the list
	 */
	public List<TaxHeadEstimate> enrichPaccAmountForBookingChange(BookingsRequest bookingsRequest,
			BigDecimal finalAmount, String taxHeadCode1, String taxHeadCode2,
			List<TaxHeadMasterFields> taxHeadMasterFieldList,
			ParkCommunityHallV1MasterModel parkCommunityHallV1FeeMaster) {

		List<TaxHeadEstimate> taxHeadEstimate1 = new ArrayList<>();

		if (BookingsConstants.PAYMENT_SUCCESS_STATUS.equals(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
			BillResponse billResponse = null;
			if (bookingsService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber())) {
			GenerateBillCriteria billCriteria = GenerateBillCriteria.builder()
					.tenantId(bookingsRequest.getBookingsModel().getTenantId())
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber()).build();
			 billResponse = demandService.generateBill(bookingsRequest.getRequestInfo(), billCriteria);
			}
			BigDecimal amount = finalAmount.subtract(
					billResponse.getBill().get(0).getBillDetails().get(0).getBillAccountDetails().get(0).getAmount());

			if (finalAmount.compareTo(billResponse.getBill().get(0).getBillDetails().get(0).getBillAccountDetails()
					.get(0).getAmount()) < 1 || finalAmount.compareTo(billResponse.getBill().get(0).getBillDetails().get(0).getBillAccountDetails()
							.get(0).getAmount()) == 0) {
				config.setDemandFlag(false);
				/*taxHeadEstimate1 = enrichTaxHeadEstimateForPACC(bookingsRequest, finalAmount, taxHeadCode1,
						taxHeadCode2, taxHeadMasterFieldList, parkCommunityHallV1FeeMaster);*/
			}

			else {
				taxHeadEstimate1 = enrichTaxHeadEstimateForPACC(bookingsRequest, finalAmount, taxHeadCode1, taxHeadCode2,
						taxHeadMasterFieldList, parkCommunityHallV1FeeMaster);
			/*	for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
					if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
						taxHeadEstimate1.add(
								new TaxHeadEstimate(taxHeadEstimate.getCode(), amount, taxHeadEstimate.getCategory()));
					}
					if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
						taxHeadEstimate1
								.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
										amount.multiply((BigDecimal
												.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getSurcharge()))
												.subtract(billResponse.getBill().get(0).getBillDetails().get(0)
														.getBillAccountDetails().get(1).getAmount()))
																.divide(new BigDecimal(100))),
										taxHeadEstimate.getCategory()));
					}
					if (taxHeadEstimate.getCode().equals(BookingsConstants.PACC_TAXHEAD_CODE_3)) {
						taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
								BigDecimal
										.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getLocationChangeAmount())),
								taxHeadEstimate.getCategory()));
					}
				}*/
			}
		} else {

			taxHeadEstimate1 = enrichTaxHeadEstimateForPACC(bookingsRequest, finalAmount, taxHeadCode1, taxHeadCode2,
					taxHeadMasterFieldList, parkCommunityHallV1FeeMaster);

		}
		return taxHeadEstimate1;
	}

}

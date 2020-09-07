package org.egov.bookings.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.BookingsRequestKafka;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.ParkAndCommunitySearchCriteria;
import org.egov.bookings.contract.ParkCommunityFeeMasterRequest;
import org.egov.bookings.contract.ParkCommunityFeeMasterResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.ParkAndCommunityRepository;
import org.egov.bookings.repository.ParkCommunityHallV1MasterRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class ParkAndCommunityServiceImpl.
 */
@Service
@Transactional
public class ParkAndCommunityServiceImpl implements ParkAndCommunityService {

	/** The bookings repository. */
	@Autowired
	private ParkAndCommunityRepository parkAndCommunityRepository;

	/** The save topic. */
	@Value("${kafka.topics.save.service}")
	private String saveTopic;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The workflow integrator. */
	@Autowired
	private WorkflowIntegrator workflowIntegrator;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	/** The park community hall V 1 master repository. */
	@Autowired
	private ParkCommunityHallV1MasterRepository parkCommunityHallV1MasterRepository;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(BookingsServiceImpl.class.getName());

	/** The booking service. */
	@Autowired
	private BookingsService bookingService;
	
	private Lock lock = new ReentrantLock();

	@Autowired
	private BookingsProducer bookingsProducer;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.ParkAndCommunityService#
	 * createParkAndCommunityBooking(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public BookingsModel createParkAndCommunityBooking(BookingsRequest bookingsRequest) {
		boolean flag = bookingService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber());
		
		if (!flag)
			enrichmentService.enrichParkCommunityCreateRequest(bookingsRequest);
		enrichmentService.generateDemand(bookingsRequest);

		if (config.getIsExternalWorkFlowEnabled()) {
			if (!flag)
				workflowIntegrator.callWorkFlow(bookingsRequest);
		}
		enrichmentService.enrichBookingsDetails(bookingsRequest);
		try {
		BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
		bookingsProducer.push(config.getSaveBookingTopic(), kafkaBookingRequest);
		}catch (Exception e) {
			throw new CustomException("PARK_COMMUNITY_CREATE_ERROR",e.getLocalizedMessage());
		}
		//parkAndCommunityRepository.save(bookingsRequest.getBookingsModel());
		if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel())) {
			bookingsProducer.push(config.getSaveBookingSMSTopic(), bookingsRequest);
		}
		return bookingsRequest.getBookingsModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.ParkAndCommunityService#
	 * updateParkAndCommunityBooking(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public BookingsModel updateParkAndCommunityBooking(BookingsRequest bookingsRequest) {

		/*if(BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction()))
			enrichmentService.enrichBookingsAssignee(bookingsRequest);*/

		String businessService = bookingsRequest.getBookingsModel().getBusinessService();

		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);

		// bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		BookingsModel bookingsModel = null;
		if (!BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& BookingsConstants.BUSINESS_SERVICE_PACC.equals(businessService)) {
			bookingsModel = enrichmentService.enrichPaccDetails(bookingsRequest);
			bookingsRequest.setBookingsModel(bookingsModel);
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
			//bookingsModel = parkAndCommunityRepository.save(bookingsModel);
		} else {
			if (BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
					&& BookingsConstants.BUSINESS_SERVICE_PACC.equals(businessService)) {
				config.setParkAndCommercialLock(true);
			}
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
			//bookingsModel = parkAndCommunityRepository.save(bookingsRequest.getBookingsModel());
		}
		if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel())) {
			bookingsProducer.push(config.getUpdateBookingSMSTopic(), bookingsRequest);
		}
		return bookingsRequest.getBookingsModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.ParkAndCommunityService#fetchParkCommunityMaster()
	 */
	@Override
	public List<ParkCommunityHallV1MasterModel> fetchParkCommunityMaster(ParkCommunityFeeMasterRequest parkCommunityFeeMasterRequest) {

		List<ParkCommunityHallV1MasterModel> parkCommunityHallV1Master = null;
		try {

			parkCommunityHallV1Master = parkCommunityHallV1MasterRepository.findByVenueTypeAndSector(parkCommunityFeeMasterRequest.getVenueType(),parkCommunityFeeMasterRequest.getSector());
			return parkCommunityHallV1Master;

		} catch (Exception e) {
			throw new CustomException("DATABASE_ERROR", e.getLocalizedMessage());

		}
	}

	@Override
	public Set<AvailabilityResponse> availabilitySearch(ParkAndCommunitySearchCriteria parkAndCommunitySearchCriteria) {

		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		LocalDate date = LocalDate.now();
		Date date1 = Date.valueOf(date);
		Set<AvailabilityResponse> bookedDates = new HashSet<>();
		Set<BookingsModel> bookingsModel = parkAndCommunityRepository.fetchBookedDatesOfParkAndCommunity(
				parkAndCommunitySearchCriteria.getBookingVenue(), parkAndCommunitySearchCriteria.getBookingType(),
				parkAndCommunitySearchCriteria.getSector(), date1, BookingsConstants.APPLY);
		if (null != bookingsModel) {
			for (BookingsModel bkModel : bookingsModel) {
				bookedDates.add(AvailabilityResponse.builder().fromDate(bkModel.getBkFromDate())
						.toDate(bkModel.getBkToDate()).timeslots(bkModel.getTimeslots()).build());
			}
		}
		return bookedDates;

	}

	@Override
	public Set<Date> fetchBookedDates(BookingsRequest bookingsRequest) {

		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		LocalDate date = LocalDate.now();
		Date date1 = Date.valueOf(date);
		SortedSet<Date> bookedDates = new TreeSet<>();
		
		try {
			List<LocalDate> toBeBooked = enrichmentService.extractAllDatesBetweenTwoDates(bookingsRequest);
			lock.lock();
			if (config.isParkAndCommercialLock()) {
				Set<BookingsModel> bookingsModelSet = parkAndCommunityRepository.fetchBookedDatesOfParkAndCommunity(
						bookingsRequest.getBookingsModel().getBkBookingVenue(), bookingsRequest.getBookingsModel().getBkBookingType(),
						bookingsRequest.getBookingsModel().getBkSector(), date1, BookingsConstants.APPLY);

				List<LocalDate> fetchBookedDates = enrichmentService.enrichBookedDates(bookingsModelSet);
				
				for (LocalDate toBeBooked1 : toBeBooked) {

					for (LocalDate fetchBookedDates1 : fetchBookedDates) {
						if (toBeBooked1.equals(fetchBookedDates1)) {
							bookedDates.add(Date.valueOf(toBeBooked1));
						}
					}
				}
			} else {
				lock.unlock();
				throw new CustomException("OTHER_PAYMENT_IN_PROCESS", "Please try after few seconds");
			}
			lock.unlock();

		} catch (Exception e) {
			lock.unlock();
			config.setParkAndCommercialLock(true);
			throw new CustomException("OTHER_PAYMENT_IN_PROCESS", "Please try after few seconds");
		}
		return bookedDates;

	}

	@Override
	public ParkCommunityHallV1MasterModel findParkAndCommunityFee(String bookingVenue) {
		ParkCommunityHallV1MasterModel parkCommunityHallFee = null;
		try {
			parkCommunityHallFee = parkCommunityHallV1MasterRepository.findById(bookingVenue);
			return parkCommunityHallFee;
		} catch (Exception e) {
			throw new CustomException("DATABASE_ERROR", e.getLocalizedMessage());
		}
	}

	@Override
	public ParkCommunityFeeMasterResponse fetchAmount(ParkCommunityFeeMasterRequest parkCommunityFeeMasterRequest) {
		ParkCommunityHallV1MasterModel parkCommunityHallFee = null;
		try {
			parkCommunityHallFee = parkCommunityHallV1MasterRepository.findById(parkCommunityFeeMasterRequest.getBookingVenue());
			if(BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallFee)) {
				throw new CustomException("DATABASE_ERROR", "Invalid Fee Master Request");
			}
			else {
				ParkCommunityFeeMasterResponse parkCommunityFeeMasterResponse= enrichmentService.enrichParkCommunityAmount(parkCommunityHallFee);
				return parkCommunityFeeMasterResponse;
			}
			
		} catch (Exception e) {
			throw new CustomException("DATABASE_ERROR", e.getLocalizedMessage());
		}
	}

}

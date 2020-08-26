package org.egov.bookings.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.repository.ParkAndCommunityRepository;
import org.egov.bookings.repository.ParkCommunityHallV1MasterRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.utils.BookingsConstants;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.ParkAndCommunityService#
	 * createParkAndCommunityBooking(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public BookingsModel createParkAndCommunityBooking(BookingsRequest bookingsRequest) {
		BookingsModel bookingsModel = null;
		boolean flag = bookingService.isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber());

		if (!flag)
			enrichmentService.enrichBookingsCreateRequest(bookingsRequest);
		enrichmentService.generateDemand(bookingsRequest);

		if (config.getIsExternalWorkFlowEnabled()) {
			if (!flag)
				workflowIntegrator.callWorkFlow(bookingsRequest);
		}
		// bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		enrichmentService.enrichBookingsDetails(bookingsRequest);
		bookingsModel = parkAndCommunityRepository.save(bookingsRequest.getBookingsModel());
		bookingsRequest.setBookingsModel(bookingsModel);
		return bookingsModel;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.ParkAndCommunityService#
	 * updateParkAndCommunityBooking(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public BookingsModel updateParkAndCommunityBooking(BookingsRequest bookingsRequest) {

		String businessService = bookingsRequest.getBookingsModel().getBusinessService();

		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);

		// bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		// bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		BookingsModel bookingsModel = null;
		if (!BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& BookingsConstants.BUSINESS_SERVICE_PACC.equals(businessService)) {
			bookingsModel = enrichmentService.enrichPaccDetails(bookingsRequest);
			bookingsModel = parkAndCommunityRepository.save(bookingsModel);
		} else {
			bookingsModel = parkAndCommunityRepository.save(bookingsRequest.getBookingsModel());
		}

		return bookingsModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.ParkAndCommunityService#fetchParkCommunityMaster()
	 */
	@Override
	public List<ParkCommunityHallV1MasterModel> fetchParkCommunityMaster() {

		List<ParkCommunityHallV1MasterModel> parkCommunityHallV1MasterList = null;
		try {

			parkCommunityHallV1MasterList = parkCommunityHallV1MasterRepository.findAll();
			return parkCommunityHallV1MasterList;

		} catch (Exception e) {
			throw new CustomException("DATABASE_ERROR", "ERROR WHILE FETCHING PARK AND COMMUNITY MASTER DATA");
		}
	}

}

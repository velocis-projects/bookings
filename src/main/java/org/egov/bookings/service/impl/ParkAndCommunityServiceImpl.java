package org.egov.bookings.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.Message;
import org.egov.bookings.contract.MessagesResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.repository.ParkAndCommunityRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class ParkAndCommunityServiceImpl implements ParkAndCommunityService{

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

	/** The osbm approver repository. */
	@Autowired
	OsbmApproverRepository osbmApproverRepository;

	/** The common repository. */
	@Autowired
	CommonRepository commonRepository;

	/** The bookings utils. */
	@Autowired
	private BookingsUtils bookingsUtils;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	/** The service request repository. */
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	/** The sms notification service. */
	@Autowired
	private SMSNotificationService smsNotificationService;
	
	/** The mail notification service. */
	@Autowired
	private MailNotificationService mailNotificationService;
	
	@Autowired
	private BookingsService bookingsService;
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(BookingsServiceImpl.class.getName());

	@Autowired
	private BookingsService bookingService;
	
	
	
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
		}
		else {
			bookingsModel = parkAndCommunityRepository.save(bookingsRequest.getBookingsModel());
		}

		return bookingsModel;
	}

}

package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.Difference;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.ProcessInstanceSearchCriteria;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.notification.EditNotificationService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.mdms.model.MdmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsServiceImpl.
 */
@Service
@Transactional
public class BookingsServiceImpl implements BookingsService {

	/** The bookings repository. */
	@Autowired
	private BookingsRepository bookingsRepository;

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
	BookingsUtils bookingsUtils;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	/** The rest template. */
	@Autowired
	private RestTemplate restTemplate;

	/** The service request repository. */
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	/** The sms notification service. */
	@Autowired
	private SMSNotificationService smsNotificationService;
	
	@Autowired
	private MailNotificationService mailNotificationService;
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(BookingsServiceImpl.class.getName());

	/**
	 * Save.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	@Override
	public BookingsModel save(BookingsRequest bookingsRequest) {
		BookingsModel bookingsModel = new BookingsModel();
		boolean flag = isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber());

		if (!flag)
			enrichmentService.enrichBookingsCreateRequest(bookingsRequest);
		enrichmentService.generateDemand(bookingsRequest);

		if (config.getIsExternalWorkFlowEnabled()) {
			if (!flag)
				workflowIntegrator.callWorkFlow(bookingsRequest);
		}
		// bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		enrichmentService.enrichBookingsDetails(bookingsRequest);
		bookingsModel = bookingsRepository.save(bookingsRequest.getBookingsModel());
		bookingsRequest.setBookingsModel(bookingsModel);
		if(!BookingsFieldsValidator.isNullOrEmpty(bookingsModel))
		{
			Map< String, MdmsJsonFields > mdmsJsonFieldsMap = mdmsJsonField(bookingsRequest);
			String notificationMsg = prepareSMSNotifMsgForCreate(bookingsModel, mdmsJsonFieldsMap);
			smsNotificationService.sendSMS(notificationMsg);
			mailNotificationService.sendMail(bookingsModel.getBkEmail(), notificationMsg, "Booking Status");
		}
		return bookingsRequest.getBookingsModel();

	}
	
	
	/**
	 * Mdms json field.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	private Map< String, MdmsJsonFields > mdmsJsonField(BookingsRequest bookingsRequest)
	{
		JSONArray mdmsArrayList = null;
		Map< String, MdmsJsonFields > mdmsJsonFieldsMap = new HashMap<>();
		try
		{
			if(!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest) && !BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getRequestInfo()))
			{
				Object mdmsData = bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
				String jsonString = objectMapper.writeValueAsString(mdmsData);
				MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
				Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
				Map<String, JSONArray> mdmsRes = mdmsResMap.get("Booking");
				mdmsArrayList = mdmsRes.get("BookingType");
				for (int i = 0; i < mdmsArrayList.size(); i++) 
				{
					jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
					MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
					mdmsJsonFieldsMap.put(mdmsJsonFields.getCode(), mdmsJsonFields);
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur during mdmsJsonField " + e);
		}
		return mdmsJsonFieldsMap;
	}
	
	/**
	 * Prepare SMS notification message.
	 *
	 * @param bookingsModel the bookings model
	 * @param mdmsJsonFieldsMap the mdms json fields map
	 * @return the string
	 */
	private String prepareSMSNotifMsgForCreate(BookingsModel bookingsModel, Map< String, MdmsJsonFields > mdmsJsonFieldsMap)
	{
		String notificationMsg = "";
		if(!BookingsFieldsValidator.isNullOrEmpty(bookingsModel) && !BookingsFieldsValidator.isNullOrEmpty(mdmsJsonFieldsMap))
		{
			notificationMsg = "Dear " + bookingsModel.getBkApplicantName() + ", You have booked " + mdmsJsonFieldsMap.get(bookingsModel.getBkBookingType()).getName()
					+ " successfully. Your application number is " + bookingsModel.getBkApplicationNumber() + ".";
		}
		return notificationMsg;
	}
	
	/**
	 * Prepare SMS notif msg for update.
	 *
	 * @param bookingsModel the bookings model
	 * @param mdmsJsonFieldsMap the mdms json fields map
	 * @return the string
	 */
	private String prepareSMSNotifMsgForUpdate(BookingsModel bookingsModel, Map< String, MdmsJsonFields > mdmsJsonFieldsMap)
	{
		String notificationMsg = "";
		if(!BookingsFieldsValidator.isNullOrEmpty(bookingsModel) && !BookingsFieldsValidator.isNullOrEmpty(mdmsJsonFieldsMap))
		{
			notificationMsg = "Dear " + bookingsModel.getBkApplicantName() + ", Your " + mdmsJsonFieldsMap.get(bookingsModel.getBkBookingType()).getName()
					+ " Application no. " + bookingsModel.getBkApplicationNumber() +  " has been updated with status " + bookingsModel.getBkApplicationStatus() + ".";
		}
		return notificationMsg;
	}
	
	/**
	 * Checks if is booking exists.
	 *
	 * @param bkApplicationnumber the bk applicationnumber
	 * @return true, if is booking exists
	 */
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.BookingsService#isBookingExists(java.lang.String)
	 */
	public boolean isBookingExists(String bkApplicationnumber) {

		BookingsModel bookingsModel = bookingsRepository.findByBkApplicationNumber(bkApplicationnumber);

		if (null == bookingsModel) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Gets the all building material.
	 *
	 * @return the all building material
	 */
	@Override
	public List<BookingsModel> getAllBuildingMaterial() {
		return (List<BookingsModel>) bookingsRepository.findAll();
	}

	/**
	 * Gets the building material by id.
	 *
	 * @param id the id
	 * @return the building material by id
	 */
	@Override
	public BookingsModel getBuildingMaterialById(Long id) {
		return bookingsRepository.findOne(id);
	}

	/**
	 * Gets the citizen search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen search booking
	 */
	@Override
	public Booking getCitizenSearchBooking(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		Booking booking = new Booking();
		List<BookingsModel> myBookingList = new ArrayList<>();
		List<?> documentList = new ArrayList<>();
		Map<String, String> documentMap = new HashMap<>();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}

			String tenantId = searchCriteriaFieldsDTO.getTenantId();
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
			String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
			Date fromDate = searchCriteriaFieldsDTO.getFromDate();
			Date toDate = searchCriteriaFieldsDTO.getToDate();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			String bookingType = searchCriteriaFieldsDTO.getBookingType();

			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				myBookingList = bookingsRepository.getCitizenSearchBooking(tenantId, applicationNumber,
						applicationStatus, mobileNumber, bookingType, uuid);
			} else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate)
					&& !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				myBookingList = bookingsRepository.getCitizenSearchBooking(tenantId, applicationNumber,
						applicationStatus, mobileNumber, bookingType, uuid, fromDate, toDate);
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocumentList(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}

			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					String[] strArray = documentStrArray[1].split("/");
					String fileStoreId = documentStrArray[0].substring(2, documentStrArray[0].length() - 1);
					String document = strArray[strArray.length - 1].substring(13,
							(strArray[strArray.length - 1].length() - 2));
					documentMap.put(fileStoreId, document);
				}
			}
			booking.setDocumentMap(documentMap);
			booking.setBookingsModelList(myBookingList);
			booking.setBookingsCount(myBookingList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getCitizenSearchBooking " + e);
		}
		return booking;
	}

	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	@Override
	public Booking getEmployeeSearchBooking(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		Booking booking = new Booking();
		List<BookingsModel> bookingsList = new ArrayList<>();
		List<?> documentList = new ArrayList<>();
		Map<String, String> documentMap = new HashMap<>();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			String tenantId = searchCriteriaFieldsDTO.getTenantId();
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
			String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
			Date fromDate = searchCriteriaFieldsDTO.getFromDate();
			Date toDate = searchCriteriaFieldsDTO.getToDate();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			String bookingType = searchCriteriaFieldsDTO.getBookingType();
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			List<String> sectorList = commonRepository.findSectorList(uuid);
			if (sectorList == null || sectorList.isEmpty()) {
				return booking;
			}
			if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				bookingsList = bookingsRepository.getEmployeeSearchBooking(tenantId, applicationNumber,
						applicationStatus, mobileNumber, bookingType, sectorList);
			} else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate)
					&& !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				bookingsList = bookingsRepository.getEmployeeSearchBooking(tenantId, applicationNumber,
						applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate);
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocumentList(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					String[] strArray = documentStrArray[1].split("/");
					String fileStoreId = documentStrArray[0].substring(2, documentStrArray[0].length() - 1);
					String document = strArray[strArray.length - 1].substring(13,
							(strArray[strArray.length - 1].length() - 2));
					documentMap.put(fileStoreId, document);
				}
			}
			booking.setDocumentMap(documentMap);
			booking.setBookingsModelList(bookingsList);
			booking.setBookingsCount(bookingsList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getEmployeeSearchBooking " + e);
		}
		return booking;

	}

	/**
	 * Update.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	@Override
	public BookingsModel update(BookingsRequest bookingsRequest) {

		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);

		// bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		// bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		BookingsModel bookingsModel = null;
		try {
			if (!bookingsRequest.getBookingsModel().getBkAction().equals(BookingsConstants.APPLY)
					&& bookingsRequest.getBookingsModel().getBusinessService().equals(BookingsConstants.OSBM)) {

				bookingsModel = enrichmentService.enrichOsbmDetails(bookingsRequest);
				bookingsModel = bookingsRepository.save(bookingsModel);

			}

			else if (!bookingsRequest.getBookingsModel().getBkAction().equals(BookingsConstants.APPLY)
					&& bookingsRequest.getBookingsModel().getBusinessService().equals(BookingsConstants.BWT)) {

				bookingsModel = enrichmentService.enrichBwtDetails(bookingsRequest);
				bookingsModel = bookingsRepository.save(bookingsModel);

			} else {
				bookingsModel = bookingsRepository.save(bookingsRequest.getBookingsModel());
				bookingsModel = bookingsRequest.getBookingsModel();

			}
			if(!BookingsFieldsValidator.isNullOrEmpty(bookingsModel))
			{
				Map< String, MdmsJsonFields > mdmsJsonFieldsMap = mdmsJsonField(bookingsRequest);
				String notificationMsg = prepareSMSNotifMsgForUpdate(bookingsModel, mdmsJsonFieldsMap);
				smsNotificationService.sendSMS(notificationMsg);
				mailNotificationService.sendMail(bookingsModel.getBkEmail(), notificationMsg, "Booking Status");
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur while updating booking " + e);
		}
		return bookingsModel;
	}

	/**
	 * Employee records count.
	 *
	 * @param tenantId        the tenant id
	 * @param uuid            the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	@Override
	public Map<String, Integer> employeeRecordsCount(String tenantId, String uuid, BookingsRequest bookingsRequest) {
		Map<String, Integer> bookingCountMap = new HashMap<>();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) {
				throw new IllegalArgumentException("Invalid bookingsRequest");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			List<String> sectorList = commonRepository.findSectorList(uuid);
			int allRecordsCount = bookingsRepository.countByTenantIdAndBkSectorIn(tenantId, sectorList);
			bookingCountMap.put("allRecordsCount", allRecordsCount);
			int bookingCount = 0;
			JSONArray mdmsArrayList = null;
			Object mdmsData = bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
			String jsonString = objectMapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get("Booking");
			mdmsArrayList = mdmsRes.get("BookingType");
			for (int i = 0; i < mdmsArrayList.size(); i++) {
				jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
				MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
				bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndBkSectorIn(tenantId,
						mdmsJsonFields.getCode(), sectorList);
				bookingCountMap.put(mdmsJsonFields.getCode(), bookingCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in the employeeRecordsCount " + e);
		}
		return bookingCountMap;
	}

	/**
	 * Citizen records count.
	 *
	 * @param tenantId        the tenant id
	 * @param uuid            the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	@Override
	public Map<String, Integer> citizenRecordsCount(String tenantId, String uuid, BookingsRequest bookingsRequest) {
		Map<String, Integer> bookingCountMap = new HashMap<>();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) {
				throw new IllegalArgumentException("Invalid bookingsRequest");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			int allRecordsCount = bookingsRepository.countByTenantIdAndUuid(tenantId, uuid);
			bookingCountMap.put("allRecordsCount", allRecordsCount);
			int bookingCount = 0;
			JSONArray mdmsArrayList = null;
			Object mdmsData = bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
			String jsonString = objectMapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get("Booking");
			mdmsArrayList = mdmsRes.get("BookingType");
			for (int i = 0; i < mdmsArrayList.size(); i++) {
				jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
				MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
				bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndUuid(tenantId,
						mdmsJsonFields.getCode(), uuid);
				bookingCountMap.put(mdmsJsonFields.getCode(), bookingCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in the citizenRecordsCount " + e);
		}
		return bookingCountMap;
	}

	/**
	 * Gets the workflow process instances.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @param criteria           the criteria
	 * @return the workflow process instances
	 */
	@Override
	public Object getWorkflowProcessInstances(RequestInfoWrapper requestInfoWrapper,
			ProcessInstanceSearchCriteria criteria) {
		Object result = new Object();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(requestInfoWrapper)) {
				throw new IllegalArgumentException("Invalid requestInfoWrapper");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(criteria)) {
				throw new IllegalArgumentException("Invalid criteria");
			}
			StringBuilder url = new StringBuilder(config.getWfHost());
			url.append(config.getWorkflowProcessInstancePath());
			url.append("?businessIds=");
			url.append(criteria.getBusinessIds().get(0));
			url.append("&history=");
			url.append(criteria.getHistory());
			url.append("&tenantId=");
			url.append(criteria.getTenantId());
			result = serviceRequestRepository.fetchResult(url, requestInfoWrapper);
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getWorkflowProcessInstances " + e);
		}
		return result;
	}
}

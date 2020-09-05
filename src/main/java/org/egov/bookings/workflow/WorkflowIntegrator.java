package org.egov.bookings.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.NewLocationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
@Slf4j
public class WorkflowIntegrator {

	private static final String TENANTIDKEY = "tenantId";

	private static final String BUSINESSSERVICEKEY = "businessService";

	private static final String ACTIONKEY = "action";

	private static final String COMMENTKEY = "comment";

	private static final String MODULENAMEKEY = "moduleName";

	private static final String BUSINESSIDKEY = "businessId";

	private static final String DOCUMENTSKEY = "documents";

	private static final String ASSIGNEEKEY = "assignee";

	private static final String UUIDKEY = "uuid";

	private static final String MODULENAMEVALUE = "BOOKING";

	private static final String WORKFLOWREQUESTARRAYKEY = "ProcessInstances";

	private static final String REQUESTINFOKEY = "RequestInfo";

	private static final String PROCESSINSTANCESJOSNKEY = "$.ProcessInstances";

	private static final String BUSINESSIDJOSNKEY = "$.businessId";

	private static final String STATUSJSONKEY = "$.state.applicationStatus";

	private RestTemplate rest;

	@Autowired
	private BookingsConfiguration config;

	@Autowired
	private CommonRepository commonRepository;

	@Autowired
	private OsbmApproverRepository osbmApproverRepository;

	@Autowired
	private BookingsUtils bookingsUtils;

	@Autowired
	public WorkflowIntegrator(RestTemplate rest, BookingsConfiguration config) {
		this.rest = rest;
		this.config = config;
	}

	/**
	 * Method to integrate with workflow
	 *
	 * takes the trade-license request as parameter constructs the work-flow request
	 *
	 * and sets the resultant status from wf-response back to trade-license object
	 *
	 * @param tradeLicenseRequest
	 */
	public void callWorkFlow(BookingsRequest bookingsRequest) {

		String wfTenantId = bookingsRequest.getBookingsModel().getTenantId();

		JSONArray array = new JSONArray();
		BookingsModel bkModel = bookingsRequest.getBookingsModel();

		/*
		 * uuid = commonRepository.findAssigneeUuid(wfTenantId, bkModel.getBkAction(),
		 * bkModel.getBusinessService(), wfTenantId);
		 */

		// Object mdmsData =
		// bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
		// OsbmApproverModel osbmApproverModel = null;
		// osbmApproverModel =
		// osbmApproverRepository.findBySector(bookingsRequest.getBookingsModel().getBkSector());
		JSONObject obj = new JSONObject();
		Map<String, String> uuidmap = new HashMap<>();
		uuidmap.put(UUIDKEY, bkModel.getAssignee());
		obj.put(BUSINESSIDKEY, bkModel.getBkApplicationNumber());
		obj.put(TENANTIDKEY, wfTenantId);
		obj.put(BUSINESSSERVICEKEY, bookingsRequest.getBookingsModel().getBusinessService());
		obj.put(MODULENAMEKEY, MODULENAMEVALUE);
		obj.put(ACTIONKEY, bkModel.getBkAction());
		if (!BookingsFieldsValidator.isNullOrEmpty(bkModel.getBkRemarks()))
			obj.put(COMMENTKEY, bkModel.getBkRemarks());
		if (!StringUtils.isEmpty(bkModel.getAssignee()))
			obj.put(ASSIGNEEKEY, uuidmap);
		obj.put(DOCUMENTSKEY, bkModel.getWfDocuments());
		array.add(obj);

		JSONObject workFlowRequest = new JSONObject();
		workFlowRequest.put(REQUESTINFOKEY, bookingsRequest.getRequestInfo());
		workFlowRequest.put(WORKFLOWREQUESTARRAYKEY, array);

		log.info("Workflow Request " + workFlowRequest);

		String response = null;
		try {
			response = rest.postForObject(config.getWfHost().concat(config.getWfTransitionPath()), workFlowRequest,
					String.class);
		} catch (HttpClientErrorException e) {

			/*
			 * extracting message from client error exception
			 */
			DocumentContext responseContext = JsonPath.parse(e.getResponseBodyAsString());
			List<Object> errros = null;
			try {
				errros = responseContext.read("$.Errors");
			} catch (PathNotFoundException pnfe) {
				log.error("EG_BOOKING_WF_ERROR_KEY_NOT_FOUND",
						" Unable to read the json path in error object : " + pnfe.getMessage());
				throw new CustomException("EG_BOOKING_WF_ERROR_KEY_NOT_FOUND",
						" Unable to read the json path in error object : " + pnfe.getMessage());
			}
			throw new CustomException("EG_WF_ERROR", errros.toString());
		} catch (Exception e) {
			throw new CustomException("EG_WF_ERROR",
					" Exception occured while integrating with workflow : " + e.getMessage());
		}

		/*
		 * on success result from work-flow read the data and set the status back to TL
		 * object
		 */
		DocumentContext responseContext = JsonPath.parse(response);
		List<Map<String, Object>> responseArray = responseContext.read(PROCESSINSTANCESJOSNKEY);
		Map<String, String> idStatusMap = new HashMap<>();
		responseArray.forEach(object -> {

			DocumentContext instanceContext = JsonPath.parse(object);
			idStatusMap.put(instanceContext.read(BUSINESSIDJOSNKEY), instanceContext.read(STATUSJSONKEY));
		});

		// setting the status back to booking object from wf response
		bookingsRequest.getBookingsModel()
				.setBkApplicationStatus(idStatusMap.get(bookingsRequest.getBookingsModel().getBkApplicationNumber()));

	}

	public void callWorkFlow(NewLocationRequest newLocationRequest) {

		String wfTenantId = newLocationRequest.getNewLocationModel().getTenantId();

		JSONArray array = new JSONArray();
		OsujmNewLocationModel newLocModel = newLocationRequest.getNewLocationModel();

		/*
		 * uuid = commonRepository.findAssigneeUuid(wfTenantId, bkModel.getBkAction(),
		 * bkModel.getBusinessService(), wfTenantId);
		 */

		// Object mdmsData =
		// bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
		// OsbmApproverModel osbmApproverModel = null;
		// osbmApproverModel =
		// osbmApproverRepository.findBySector(bookingsRequest.getBookingsModel().getBkSector());
		JSONObject obj = new JSONObject();
		Map<String, String> uuidmap = new HashMap<>();
		uuidmap.put(UUIDKEY, newLocModel.getAssignee());
		obj.put(BUSINESSIDKEY, newLocModel.getApplicationNumber());
		obj.put(TENANTIDKEY, wfTenantId);
		obj.put(BUSINESSSERVICEKEY, newLocModel.getBusinessService());
		obj.put(MODULENAMEKEY, MODULENAMEVALUE);
		obj.put(ACTIONKEY, newLocModel.getAction());
		if (!BookingsFieldsValidator.isNullOrEmpty(newLocModel.getRemarks()))
			obj.put(COMMENTKEY, newLocModel.getRemarks());
		if (!StringUtils.isEmpty(newLocModel.getAssignee()))
			obj.put(ASSIGNEEKEY, uuidmap);
		obj.put(DOCUMENTSKEY, newLocModel.getWfDocuments());
		array.add(obj);

		JSONObject workFlowRequest = new JSONObject();
		workFlowRequest.put(REQUESTINFOKEY, newLocationRequest.getRequestInfo());
		workFlowRequest.put(WORKFLOWREQUESTARRAYKEY, array);

		log.info("Workflow Request " + workFlowRequest);

		String response = null;
		try {
			response = rest.postForObject(config.getWfHost().concat(config.getWfTransitionPath()), workFlowRequest,
					String.class);
		} catch (HttpClientErrorException e) {

			/*
			 * extracting message from client error exception
			 */
			DocumentContext responseContext = JsonPath.parse(e.getResponseBodyAsString());
			List<Object> errros = null;
			try {
				errros = responseContext.read("$.Errors");
			} catch (PathNotFoundException pnfe) {
				log.error("EG_NEWLOCATIONMODEL_WF_ERROR_KEY_NOT_FOUND",
						" Unable to read the json path in error object : " + pnfe.getMessage());
				throw new CustomException("EG_TL_WF_ERROR_KEY_NOT_FOUND",
						" Unable to read the json path in error object : " + pnfe.getMessage());
			}
			throw new CustomException("EG_WF_ERROR", errros.toString());
		} catch (Exception e) {
			throw new CustomException("EG_WF_ERROR",
					" Exception occured while integrating with workflow : " + e.getMessage());
		}

		/*
		 * on success result from work-flow read the data and set the status back to newLocationModel
		 * object
		 */
		DocumentContext responseContext = JsonPath.parse(response);
		List<Map<String, Object>> responseArray = responseContext.read(PROCESSINSTANCESJOSNKEY);
		Map<String, String> idStatusMap = new HashMap<>();
		responseArray.forEach(object -> {

			DocumentContext instanceContext = JsonPath.parse(object);
			idStatusMap.put(instanceContext.read(BUSINESSIDJOSNKEY), instanceContext.read(STATUSJSONKEY));
		});

		// setting the status back to newLocalModel object from wf response
		newLocationRequest.getNewLocationModel()
				.setApplicationStatus(idStatusMap.get(newLocationRequest.getNewLocationModel().getApplicationNumber()));

	}
}
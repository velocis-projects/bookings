package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.ServiceReqSearchCriteria;
import org.egov.bookings.contract.ServiceResponse;
import org.egov.bookings.model.ActionInfo;
import org.egov.bookings.model.Service;
import org.egov.bookings.model.user.UserResponse;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Service
public class NotificationService {
		
	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeV2Host;

	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeV2SearchEndpoint;
	
	@Value("${egov.hrms.host}")
	private String egovHRMShost;

	@Value("${egov.hrms.search.endpoint}")
	private String egovHRMSSearchEndpoint;
	
	@Value("${egov.user.host}")
	private String egovUserHost;

	@Value("${egov.user.search.endpoint}")
	private String egovUserSearchEndpoint;
	
	@Value("${egov.default.sla.in.ms}")
	private Long egovDefaultServiceSla;
	
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	@Autowired
	private GrievanceService requestService;

	@Autowired
	private BookingsUtils bookingsUtils;
	
	public static Map<String, Map<String, String>> localizedMessageMap = new HashMap<>();

	/**
	 * Fetches the Service type and sla hours for the respective service type
	 * 
	 * @param serviceReq
	 * @param requestInfo
	 * @param locale
	 * @return
	 */
	public List<Object> getServiceType(Service serviceReq, RequestInfo requestInfo, String locale) {
		StringBuilder uri = new StringBuilder();
		List<Object> listOfValues = new ArrayList<>();
		MdmsCriteriaReq mdmsCriteriaReq = bookingsUtils.prepareSearchRequestForServiceType(uri, serviceReq.getTenantId(),
				serviceReq.getServiceCode(), requestInfo);
		String serviceType = null;
		List<String> serviceTypes = null;
		List<Integer> slaHours = null;
		String tenantId = serviceReq.getTenantId().split("[.]")[0]; // localization values are for now state-level.
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			serviceTypes = JsonPath.read(result, BookingsConstants.JSONPATH_SERVICE_CODES);
			slaHours = JsonPath.read(result, BookingsConstants.JSONPATH_SLA);
			if (CollectionUtils.isEmpty(serviceTypes) || CollectionUtils.isEmpty(slaHours))
				return null;
			if (null == localizedMessageMap.get(locale + "|" + tenantId)) // static map that saves code-message pair against locale | tenantId.
				getLocalisedMessages(requestInfo, tenantId, locale, BookingsConstants.LOCALIZATION_MODULE_NAME);
			serviceType = localizedMessageMap.get(locale + "|" + tenantId).get(BookingsConstants.LOCALIZATION_COMP_CATEGORY_PREFIX + serviceTypes.get(0)); //result set is always of size one.
			if(StringUtils.isEmpty(serviceType))
				serviceType = BookingsUtils.splitCamelCase(serviceTypes.get(0));
		} catch (Exception e) {
			return null;
		}
		Integer sla = slaHours.get(0) / 24; //converting hours to days.
		listOfValues.add(serviceType); listOfValues.add(sla);
		return listOfValues;
	}

	/**
	 * Fetches Employee Details
	 * 
	 * @param tenantId
	 * @param id
	 * @param requestInfo
	 * @return
	 */
	public Map<String, String> getEmployeeDetails(String tenantId, String id, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(egovHRMShost).append(egovHRMSSearchEndpoint).append("?ids=" + id)
				.append("&tenantId=" + tenantId);
		Object response = null;
		Map<String, String> employeeDetails = new HashMap<>();
		try {
			response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			if (null == response) {
				return employeeDetails;
			}
			employeeDetails.put("name", JsonPath.read(response, BookingsConstants.EMPLOYEE_NAME_JSONPATH));
			employeeDetails.put("phone", JsonPath.read(response, BookingsConstants.EMPLOYEE_PHNO_JSONPATH));
			employeeDetails.put("department", ((List<String>) JsonPath.read(response, BookingsConstants.EMPLOYEE_DEPTCODE_JSONPATH)).get(0));
			employeeDetails.put("designation", ((List<String>)JsonPath.read(response, BookingsConstants.EMPLOYEE_DESGCODE_JSONPATH)).get(0));
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
		return employeeDetails;
	}

	/**
	 * An employee might belong to different departments, 
	 * This method fetches all his departments and returns only that department to which the currently assigned complaint belongs to.
	 *  
	 * @param serviceReq
	 * @param codes
	 * @param requestInfo
	 * @return
	 */
	public String getDepartmentForNotification(Service serviceReq, List<String> codes, RequestInfo requestInfo) {
		String department = null;
		try {
			if (CollectionUtils.isEmpty(codes))
				 return department;
			else {
				Object response = requestService.fetchServiceDefs(requestInfo, serviceReq.getTenantId(), codes);
				if (null == response) {
					 return department;
				}
				try {
					List<String> departments = JsonPath.read(response, "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.[?(@.serviceCode=='" + serviceReq.getServiceCode() + "')].department");
					if(CollectionUtils.isEmpty(departments)) {
						 return department;
					}else {
						department = departments.get(0); //Every serviceCode is mapped to always only one dept.
					}
				} catch (Exception e) {
					 return department;
				}
			}
		}catch (Exception e) {
		    return department;
		}
		 return department;
	}

	/**
	 * 	Fetches designation for notification text
	 * 
	 * @param serviceReq
	 * @param code
	 * @param requestInfo
	 * @return
	 */
	public String getDesignation(Service serviceReq, String code, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = bookingsUtils.prepareMdMsRequestForDesignation(uri, serviceReq.getTenantId(), code,
				requestInfo);
		List<String> designations = null;
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			log.info("Desg search: "+result);
			designations = JsonPath.read(result, BookingsConstants.JSONPATH_DESIGNATIONS);
			if (null == designations || designations.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		return designations.get(0);
	}

	/**
	 * Populates the localized msg cache
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param locale
	 * @param module
	 */
	public void getLocalisedMessages(RequestInfo requestInfo, String tenantId, String locale, String module) {
		Map<String, String> mapOfCodesAndMessages = new HashMap<>();
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = bookingsUtils.prepareRequestForLocalization(uri, requestInfo, locale,
				tenantId, module);
		List<String> codes = null;
		List<String> messages = null;
		Object result = null;
		try {
			result = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			codes = JsonPath.read(result, BookingsConstants.LOCALIZATION_CODES_JSONPATH);
			messages = JsonPath.read(result, BookingsConstants.LOCALIZATION_MSGS_JSONPATH);
		} catch (Exception e) {
			log.error("Exception while fetching from localization: " + e);
		}
		if (null != result) {
			for (int i = 0; i < codes.size(); i++) {
				mapOfCodesAndMessages.put(codes.get(i), messages.get(i));
			}
			localizedMessageMap.put(locale + "|" + tenantId, mapOfCodesAndMessages);
		}
	}
	
	/**
	 * Fetches phone number for notification based on the recepient of the notif.
	 * 
	 * @param requestInfo
	 * @param userId
	 * @param tenantId
	 * @param assignee
	 * @param role
	 * @return
	 */
	public String getMobileAndIdForNotificationService(RequestInfo requestInfo, String userId, String tenantId, String assignee, String role) {
		String phoneNumber = null;
		String uuid = "uuid";
		Object response = null;
		ObjectMapper mapper = bookingsUtils.getObjectMapper();
		StringBuilder uri = new StringBuilder();
		Object request = new HashMap<>();
		if(role.equals(BookingsConstants.ROLE_CITIZEN)) {
			request = bookingsUtils.prepareRequestForUserSearch(uri, requestInfo, userId, tenantId);
			try {
				response = serviceRequestRepository.fetchResult(uri, request);
				if(null != response) {
					UserResponse res = mapper.convertValue(response, UserResponse.class);
					phoneNumber = res.getUser().get(0).getMobileNumber();
					uuid = res.getUser().get(0).getUuid();
				}
			}catch(Exception e) {
				log.error("Couldn't fetch user for id: "+userId+" error: " + e);
			}
			return phoneNumber + "|" + uuid;
		}else if(role.equals(BookingsConstants.ROLE_EMPLOYEE)) {
			Map<String, String> employeeDetails = getEmployeeDetails(tenantId, assignee, requestInfo);
			if(!StringUtils.isEmpty(employeeDetails.get("phone"))) {
				phoneNumber = employeeDetails.get("phone");
			}
		}
		return phoneNumber + "|" + uuid;
	}
	
	/**
	 * Returns current assignee for a complaint
	 * 
	 * @param serviceReq
	 * @param requestInfo
	 * @return
	 */
	public String getCurrentAssigneeForTheServiceRequest(Service serviceReq, RequestInfo requestInfo) {
		ServiceReqSearchCriteria serviceReqSearchCriteria = ServiceReqSearchCriteria.builder().tenantId(serviceReq.getTenantId())
				.serviceRequestId(Arrays.asList(serviceReq.getServiceRequestId())).build();
		ServiceResponse response = (ServiceResponse) requestService.getServiceRequestDetails(requestInfo, serviceReqSearchCriteria);
		try {
			List<ActionInfo> actions = response.getActionHistory().get(0).getActions().stream()
					.filter(obj -> !StringUtils.isEmpty(obj.getAssignee())).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(actions))
				return null;
			return actions.get(0).getAssignee();
		}catch(Exception e) {
			return null;
		}

	}
	
	public Long getSlaHours() {
		log.info("Returning default sla: "+egovDefaultServiceSla);
		return egovDefaultServiceSla;
	}

}
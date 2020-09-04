package org.egov.bookings.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsConstants.
 */
@Component
public class BookingsConstants {

	/**
	 * Instantiates a new bookings constants.
	 */
	private BookingsConstants() {
	}

	/** The Constant SERV_REQ_ID_NAME. */
	public static final String SERV_REQ_ID_NAME = "pgr.servicerequestid";

	/** The Constant SERV_REQ_ID_FORMAT. */
	public static final String SERV_REQ_ID_FORMAT = "[cy:dd]/[cy:MM]/[cy:yyyy]/[SEQ_EG_PGR_SERVICEREQUESTID]";

	/** The Constant TEMPLATE_COMPLAINT_EMAIL. */
	// Notification
	public static final String TEMPLATE_COMPLAINT_EMAIL = "./src/main/resources/email-templates/velocityEmailNotifSample.vm";

	/** The Constant SEARCHER_PGR_MOD_NAME. */
	public static final String SEARCHER_PGR_MOD_NAME = "rainmaker-pgr-V2";

	/** The Constant SEARCHER_SRSEARCH_DEF_NAME. */
	public static final String SEARCHER_SRSEARCH_DEF_NAME = "serviceSearchWithDetails";

	/** The Constant SEARCHER_PLAINSEARCH_DEF_NAME. */
	public static final String SEARCHER_PLAINSEARCH_DEF_NAME = "plainSearch";

	/** The Constant SEARCHER_COUNT_DEF_NAME. */
	public static final String SEARCHER_COUNT_DEF_NAME = "count";

	/** The Constant PG_JSONPATH_COUNT. */
	public static final String PG_JSONPATH_COUNT = "$.count[0].count";

	/** The Constant SEARCHER_SRID_ASSIGNEDTO_DEF_NAME. */
	public static final String SEARCHER_SRID_ASSIGNEDTO_DEF_NAME = "getServiceRequestsOnAssignedTo";

	/** The Constant SRID_ASSIGNEDTO_JSONPATH. */
	public static final String SRID_ASSIGNEDTO_JSONPATH = "$.servicesRequestIds.*.businesskey";

	/** The Constant MDMS_PGR_MOD_NAME. */
	public static final String MDMS_PGR_MOD_NAME = "RAINMAKER-PGR";

	/** The Constant MDMS_SERVICETYPE_MASTER_NAME. */
	public static final String MDMS_SERVICETYPE_MASTER_NAME = "ServiceDefs";

	/** The Constant MDMS_COMMON_MASTERS_MODULE_NAME. */
	public static final String MDMS_COMMON_MASTERS_MODULE_NAME = "common-masters";

	/** The Constant MDMS_DEPT_MASTERS_MASTER_NAME. */
	public static final String MDMS_DEPT_MASTERS_MASTER_NAME = "Department";

	/** The Constant MDMS_DESIGNATION_MASTERS_MASTER_NAME. */
	public static final String MDMS_DESIGNATION_MASTERS_MASTER_NAME = "Designation";

	/** The Constant LOCALIZATION_MODULE_NAME. */
	public static final String LOCALIZATION_MODULE_NAME = "rainmaker-pgr";

	/** The Constant LOCALIZATION_CODE_SUBMIT_CITIZEN. */
	public static final String LOCALIZATION_CODE_SUBMIT_CITIZEN = "pgr.sms.notification.submit.citizen";

	/** The Constant LOCALIZATION_CODE_REOPEN_CITIZEN. */
	public static final String LOCALIZATION_CODE_REOPEN_CITIZEN = "pgr.sms.notification.reopen.citizen";

	/** The Constant LOCALIZATION_CODE_REOPEN_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_REOPEN_EMPLOYEE = "pgr.sms.notification.reopen.employee";

	/** The Constant LOCALIZATION_CODE_ASSIGN_CITIZEN. */
	public static final String LOCALIZATION_CODE_ASSIGN_CITIZEN = "pgr.sms.notification.assign.citizen";


	/** The Constant LOCALIZATION_CODE_ASSIGN_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_ASSIGN_EMPLOYEE = "pgr.sms.notification.assign.employee";

	/** The Constant LOCALIZATION_CODE_REASSIGN_CITIZEN. */
	public static final String LOCALIZATION_CODE_REASSIGN_CITIZEN = "pgr.sms.notification.reassign.citizen";

	/** The Constant LOCALIZATION_CODE_REASSIGN_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_REASSIGN_EMPLOYEE = "pgr.sms.notification.reassign.employee";

	/** The Constant LOCALIZATION_CODE_REJECT_CITIZEN. */
	public static final String LOCALIZATION_CODE_REJECT_CITIZEN = "pgr.sms.notification.reject.citizen";

	/** The Constant LOCALIZATION_CODE_RESOLVE_CITIZEN. */
	public static final String LOCALIZATION_CODE_RESOLVE_CITIZEN = "pgr.sms.notification.resolve.citizen";

	/** The Constant LOCALIZATION_CODE_CLOSE_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_CLOSE_EMPLOYEE = "pgr.sms.notification.close.employee";

	/** The Constant LOCALIZATION_CODE_COMMENT. */
	public static final String LOCALIZATION_CODE_COMMENT = "pgr.sms.notification.comment";

	/** The Constant LOCALIZATION_CODE_DEFAULT. */
	public static final String LOCALIZATION_CODE_DEFAULT = "pgr.sms.notification.default";

	/** The Constant LOCALIZATION_CODE_COMMENT_DEFAULT. */
	public static final String LOCALIZATION_CODE_COMMENT_DEFAULT = "pgr.sms.notification.comment.default";

	/** The Constant LOCALIZATION_COMP_CATEGORY_PREFIX. */
	public static final String LOCALIZATION_COMP_CATEGORY_PREFIX = "pgr.complaint.category.";

	/** The Constant SERVICE_CODES. */
	public static final String SERVICE_CODES = "serviceCode";

	/** The Constant JSONPATH_SERVICEDEFS. */
	public static final String JSONPATH_SERVICEDEFS = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs";

	/** The Constant JSONPATH_SERVICE_CODES. */
	public static final String JSONPATH_SERVICE_CODES = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.*.serviceCode";

	/** The Constant JSONPATH_SLA. */
	public static final String JSONPATH_SLA = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.*.slaHours";

	/** The Constant JSONPATH_DEPARTMENTS. */
	public static final String JSONPATH_DEPARTMENTS = "$.MdmsRes.common-masters.Department";

	/** The Constant JSONPATH_DESIGNATIONS. */
	public static final String JSONPATH_DESIGNATIONS = "$.MdmsRes.common-masters.Designation";

	/** The Constant SERVICE_NAME. */
	public static final String SERVICE_NAME = "serviceName";

	/** The Constant DEFAULT_COMPLAINT_TYPE. */
	public static final String DEFAULT_COMPLAINT_TYPE = "resolution";

	/** The Constant EMPLOYEE_DEPTCODES_JSONPATH. */
	public static final String EMPLOYEE_DEPTCODES_JSONPATH = "$.Employees[0].assignments.*.department";

	/** The Constant EMPLOYEE_DEPTCODE_JSONPATH. */
	public static final String EMPLOYEE_DEPTCODE_JSONPATH = "$.Employees.[0].assignments.[?(@.isCurrentAssignment == true)].department";

	/** The Constant EMPLOYEE_DESGCODE_JSONPATH. */
	public static final String EMPLOYEE_DESGCODE_JSONPATH = "$.Employees.[0].assignments.[?(@.isCurrentAssignment == true)].designation";

	/** The Constant EMPLOYEE_NAME_JSONPATH. */
	public static final String EMPLOYEE_NAME_JSONPATH = "$.Employees[0].user.name";

	/** The Constant EMPLOYEE_PHNO_JSONPATH. */
	public static final String EMPLOYEE_PHNO_JSONPATH = "$.Employees[0].user.mobileNumber";

	/** The Constant EMPLOYEE_TENANTID_JSONPATH. */
	public static final String EMPLOYEE_TENANTID_JSONPATH = "$.Employees[0].tenantId";

	/** The Constant EMPLOYEE_BASE_JSONPATH. */
	public static final String EMPLOYEE_BASE_JSONPATH = "$.Employees";

	/** The Constant DEPARTMENTNAME_EMPLOYEE_JSONPATH. */
	public static final String DEPARTMENTNAME_EMPLOYEE_JSONPATH = "$.Department[0].name";

	/** The Constant LOCALIZATION_CODES_JSONPATH. */
	public static final String LOCALIZATION_CODES_JSONPATH = "$.messages.*.code";

	/** The Constant LOCALIZATION_MSGS_JSONPATH. */
	public static final String LOCALIZATION_MSGS_JSONPATH = "$.messages.*.message";

	/** The Constant LOCATION__BOUNDARY_NAMES_JSONPATH. */
	public static final String LOCATION__BOUNDARY_NAMES_JSONPATH = "$.TenantBoundary.*.boundary.*.name";

	/** The Constant LOCATION__BOUNDARY_CODES_JSONPATH. */
	public static final String LOCATION__BOUNDARY_CODES_JSONPATH = "$.TenantBoundary.*.boundary.*.code";

	/** The Constant LOCATION__BOUNDARY_HIERARCHYTYPE_ADMIN. */
	public static final String LOCATION__BOUNDARY_HIERARCHYTYPE_ADMIN = "ADMIN";

	/** The Constant LOCATION__BOUNDARY_BOUNDARYTYPE_LOCALITY. */
	public static final String LOCATION__BOUNDARY_BOUNDARYTYPE_LOCALITY = "Locality";

	/** The Constant SEARCHER_RESPONSE_TEXT. */
	public static final String SEARCHER_RESPONSE_TEXT = "Searcher response : ";

	/** The Constant SMS_NOTIFICATION_STATUS_KEY. */
	public static final String SMS_NOTIFICATION_STATUS_KEY = "<status>";

	/** The Constant SMS_NOTIFICATION_COMPLAINT_TYPE_KEY. */
	public static final String SMS_NOTIFICATION_COMPLAINT_TYPE_KEY = "<complaint_type>";

	/** The Constant SMS_NOTIFICATION_DATE_KEY. */
	public static final String SMS_NOTIFICATION_DATE_KEY = "<date>";

	/** The Constant SMS_NOTIFICATION_ID_KEY. */
	public static final String SMS_NOTIFICATION_ID_KEY = "<id>";

	/** The Constant SMS_NOTIFICATION_EMP_NAME_KEY. */
	public static final String SMS_NOTIFICATION_EMP_NAME_KEY = "<emp_name>";

	/** The Constant SMS_NOTIFICATION_USER_NAME_KEY. */
	public static final String SMS_NOTIFICATION_USER_NAME_KEY = "<emp_name>";

	/** The Constant SMS_NOTIFICATION_EMP_DEPT_KEY. */
	public static final String SMS_NOTIFICATION_EMP_DEPT_KEY = "<emp_department>";

	/** The Constant SMS_NOTIFICATION_EMP_DESIGNATION_KEY. */
	public static final String SMS_NOTIFICATION_EMP_DESIGNATION_KEY = "<emp_designation>";

	/** The Constant SMS_NOTIFICATION_COMMENT_KEY. */
	public static final String SMS_NOTIFICATION_COMMENT_KEY = "<comment>";

	/** The Constant SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY. */
	public static final String SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY = "<reason>";

	/** The Constant SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY. */
	public static final String SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY = "<additional_comments>";

	/** The Constant SMS_NOTIFICATION_APP_LINK_KEY. */
	public static final String SMS_NOTIFICATION_APP_LINK_KEY = "<app_link>";

	/** The Constant SMS_NOTIFICATION_APP_DOWNLOAD_LINK_KEY. */
	public static final String SMS_NOTIFICATION_APP_DOWNLOAD_LINK_KEY = "<download_link>";

	/** The Constant SMS_NOTIFICATION_AO_DESIGNATION. */
	public static final String SMS_NOTIFICATION_AO_DESIGNATION = "<ao_designation>";

	/** The Constant SMS_NOTIFICATION_ULB_NAME. */
	public static final String SMS_NOTIFICATION_ULB_NAME = "<ulb>";

	/** The Constant SMS_NOTIFICATION_SLA_NAME. */
	public static final String SMS_NOTIFICATION_SLA_NAME = "<sla>";

	/** The Constant SMS_NOTIFICATION_RATING_KEY. */
	public static final String SMS_NOTIFICATION_RATING_KEY = "<rating>";

	/** The Constant USREVENTS_EVENT_TYPE. */
	public static final String USREVENTS_EVENT_TYPE = "SYSTEMGENERATED";

	/** The Constant USREVENTS_EVENT_NAME. */
	public static final String USREVENTS_EVENT_NAME = "Public Grievance Redressal";

	/** The Constant USREVENTS_EVENT_POSTEDBY. */
	public static final String USREVENTS_EVENT_POSTEDBY = "SYSTEM-PGR";

	/* search on roles constant */

	/** The Constant ROLE_EMPLOYEE. */
	public static final String ROLE_EMPLOYEE = "EMPLOYEE";

	/** The Constant ROLE_CITIZEN. */
	public static final String ROLE_CITIZEN = "CITIZEN";

	/** The Constant ROLE_GRO. */
	public static final String ROLE_GRO = "GRO";

	/** The Constant ROLE_DGRO. */
	public static final String ROLE_DGRO = "DGRO";

	/** The Constant ROLE_CSR. */
	public static final String ROLE_CSR = "CSR";

	/** The Constant ROLE_NAME_CITIZEN. */
	public static final String ROLE_NAME_CITIZEN = "Citizen";

	/** The Constant ROLE_NAME_EMPLOYEE. */
	public static final String ROLE_NAME_EMPLOYEE = "Employee";

	/** The Constant ROLE_NAME_GRO. */
	public static final String ROLE_NAME_GRO = "Grievance Routing Officer";

	/** The Constant ROLE_NAME_DGRO. */
	public static final String ROLE_NAME_DGRO = "Department Grievance Routing Officer";

	/** The Constant ROLE_NAME_CSR. */
	public static final String ROLE_NAME_CSR = "Customer Support Representative";

	/** The status notif key map. */
	private static Map<String, String> statusNotifKeyMap = prepareStatusNotifKeyMap();

	/** The action notif key map. */
	private static Map<String, String> actionNotifKeyMap = prepareActionNotifKeyMap();

	/** The status role localization key map. */
	private static Map<String, String> statusRoleLocalizationKeyMap = prepareStatusRoleLocalizationKeyMap();

	/** The action role localization key map. */
	private static Map<String, String> actionRoleLocalizationKeyMap = prepareActionRoleLocalizationKeyMap();

	/**
	 * Prepare status notif key map.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareStatusNotifKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.STATUS_OPENED, "submitted");
		map.put(WorkFlowConfigs.STATUS_ASSIGNED, "assigned");
		map.put(WorkFlowConfigs.STATUS_REJECTED, "rejected");
		map.put(WorkFlowConfigs.STATUS_RESOLVED, "resolved");

		return map;
	}

	/**
	 * Prepare action notif key map.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareActionNotifKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.ACTION_REOPEN, "reopened");
		map.put(WorkFlowConfigs.ACTION_REASSIGN, "reassigned");

		return map;
	}

	/**
	 * Mapping between which messages are to be sent to which actor and on what
	 * status.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareStatusRoleLocalizationKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.STATUS_OPENED + "|" + BookingsConstants.ROLE_CITIZEN, LOCALIZATION_CODE_SUBMIT_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_ASSIGNED + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_ASSIGN_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_ASSIGNED + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_ASSIGN_EMPLOYEE);
		map.put(WorkFlowConfigs.STATUS_REJECTED + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_REJECT_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_RESOLVED + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_RESOLVE_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_CLOSED + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_CLOSE_EMPLOYEE);

		return map;
	}

	/**
	 * Mapping between which messages are to be sent to which actor and on what
	 * action.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareActionRoleLocalizationKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.ACTION_REOPEN + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_REOPEN_EMPLOYEE);
		map.put(WorkFlowConfigs.ACTION_REOPEN + "|" + BookingsConstants.ROLE_CITIZEN, LOCALIZATION_CODE_REOPEN_CITIZEN);
		map.put(WorkFlowConfigs.ACTION_REASSIGN + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_REASSIGN_CITIZEN);
		map.put(WorkFlowConfigs.ACTION_REASSIGN + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_REASSIGN_EMPLOYEE);

		return map;
	}

	/**
	 * Gets the status notif key map.
	 *
	 * @return the status notif key map
	 */
	public static Map<String, String> getStatusNotifKeyMap() {
		return statusNotifKeyMap;
	}

	/**
	 * Gets the action notif key map.
	 *
	 * @return the action notif key map
	 */
	public static Map<String, String> getActionNotifKeyMap() {
		return actionNotifKeyMap;
	}

	/**
	 * Gets the status role localization key map.
	 *
	 * @return the status role localization key map
	 */
	public static Map<String, String> getStatusRoleLocalizationKeyMap() {
		return statusRoleLocalizationKeyMap;
	}

	/**
	 * Gets the action role localization key map.
	 *
	 * @return the action role localization key map
	 */
	public static Map<String, String> getActionRoleLocalizationKeyMap() {
		return actionRoleLocalizationKeyMap;
	}

	/** The Constant TENANT_ID. */
	public static final String TENANT_ID = "tenantId";

	/** The Constant ACTION. */
	public static final String ACTION = "action";
	
	public static final String ACTION_INITIATE = "INITIATE";

	/** The Constant BUSINESS_SERVICE. */
	public static final String BUSINESS_SERVICE = "businessservice";

	/** The Constant ROLE_TENANT_ID. */
	public static final String ROLE_TENANT_ID = "role_tenantId";

	/** The Constant UUID. */
	public static final String UUID = "uuid";

	/** The Constant APPLICATION_NUMBER. */
	public static final String APPLICATION_NUMBER = "applicationNumber";

	/** The Constant BOOKING_VENUE. */
	public static final String BOOKING_VENUE = "bookingVenue";

	/** The Constant BOOKING_TYPE. */
	public static final String BOOKING_TYPE = "bookingType";

	/** The Constant DATE. */
	public static final String DATE = "date";

	/** The Constant APPLY. */
	public static final String APPLY = "APPLY";
	
	/** The Constant APPROVE. */
	public static final String APPROVE = "APPROVE";

	/** The Constant PAY. */
	public static final String PAY = "PAY";

	/** The Constant BUSINESS_SERVICE_OSBM. */
	public static final String BUSINESS_SERVICE_OSBM = "OSBM";

	/** The Constant BUSINESS_SERVICE_BWT. */
	public static final String BUSINESS_SERVICE_BWT = "BWT";

	/** The Constant BUSINESS_SERVICE_GFCP. */
	public static final String BUSINESS_SERVICE_GFCP = "GFCP";

	/** The Constant BUSINESS_SERVICE_OSUJM. */
	public static final String BUSINESS_SERVICE_OSUJM = "OSUJM";

	/** The Constant BUSINESS_SERVICE_NLUJM. */
	public static final String BUSINESS_SERVICE_NLUJM = "NLUJM";
	
	public static final String BUSINESS_SERVICE_PACC = "PACC";

	/** The Constant EMPLOYEE. */
	public static final String TYPE = "type";

	/** The Constant ROLES. */
	public static final String ROLES = "roles";

	/** The Constant CITIZEN. */
	public static final String CITIZEN = "CITIZEN";

	/** The Constant EMPLOYEE. */
	public static final String EMPLOYEE = "EMPLOYEE";

	/** The Constant OSBM_APPROVER. */
	public static final String OSBM_APPROVER = "OSBM_APPROVER";

	/** The Constant MCC_HELPDESK_USER. */
	public static final String MCC_HELPDESK_USER = "MCC_HELPDESK_USER";

	/** The Constant COMMERCIAL_GROUND_VIEWER. */
	public static final String COMMERCIAL_GROUND_VIEWER = "COMMERCIAL_GROUND_VIEWER";

	/** The Constant GROUND_FOR_COMMERCIAL_PURPOSE. */
	public static final String GROUND_FOR_COMMERCIAL_PURPOSE = "GROUND_FOR_COMMERCIAL_PURPOSE";

	/** The Constant AREA. */
	public static final String AREA = "area";

	/** The Constant SECTOR. */
	public static final String SECTOR = "sector";

	/** The Constant BK_SECTOR. */
	public static final String BK_SECTOR = "bkSector";

	/** The Constant MCC_APPROVER. */
	public static final String MCC_APPROVER = "MCC_APPROVER";

	/** The Constant ACTION_SPECIAL_APPLY. */
	public static final String ACTION_DELIVER = "DELIVER";

	/** The Constant ACTION_FAILURE_APPLY. */
	public static final String ACTION_FAILURE_APPLY = "FAILUREAPPLY";

	/** The Constant OSD_APPROVER. */
	public static final String OSD_APPROVER = "OSD_APPROVER";

	/** The Constant ADMIN_APPROVER. */
	public static final String ADMIN_APPROVER = "ADMIN_APPROVER";
	
	/** The Constant NLOSUJM. */
	public static final String NLOSUJM = "New Location Open Spaces Under Jurisdiction of MCC";

	/** The Constant STATE. */
	public static final String STATE = "state";
	
	/** The Constant APPROVER. */
	public static final String APPROVER = "approver";
	
	/** The Constant USER_ID. */
	public static final String USER_ID = "userId";
	
	public static final String PARKS_AND_COMMUNITY_VIEWER = "PARKS_AND_COMMUNITY_VIEWER";
	
	
	
	// ACTION_STATUS combinations for notification
	//OSBM,OSUJM
    public static final String ACTION_STATUS_INITIATED = "INITIATE_INITIATED";
    public static final String ACTION_STATUS_PENDINGAPPROVAL = "APPLY_PENDINGAPPROVAL";
    public static final String ACTION_STATUS_PENDINGPAYMENT = "APPROVE_PENDINGPAYMENT";
    public static final String ACTION_STATUS_REJECTED  = "REJECT_REJECTED";
    public static final String ACTION_STATUS_APPROVED = "PAY_APPROVED";
    //BWT
    public static final String ACTION_STATUS_SPECIALAPPLY_DELIVERED = "SPECIALAPPLY_PENDINGASSIGNMENTDRIVER";
    public static final String ACTION_STATUS_PAIDAPPLY_PENDINGASSIGNMENTDRIVER = "PAIDAPPLY_PENDINGASSIGNMENTDRIVER";
    public static final String ACTION_STATUS_FAILUREAPPLY_PENDINGASSIGNMENTDRIVER = "FAILUREAPPLY_PENDINGASSIGNMENTDRIVER";
    public static final String ACTION_STATUS_PENDINGUPDATE = "ASSIGNDRIVER_PENDINGUPDATE";
    public static final String ACTION_STATUS_DELIVERED = "DELIVER_DELIVERED";
    public static final String ACTION_STATUS_NOTDELIVERED = "NOTDELIVER_NOTDELIVERED";
    //GFCP
    public static final String ACTION_STATUS_APPLIED  = "APPLY_APPLIED";
    //NLUJM
    public static final String ACTION_STATUS_PENDINGAPPROVALOSD = "APPROVE_PENDINGAPPROVALOSD";
    public static final String ACTION_STATUS_PENDINGPUBLISH = "APPROVEOSD_PENDINGPUBLISH";
    public static final String ACTION_STATUS_PUBLISH = "PUBLISH_PUBLISH";
	//
	
	public static final String NOTIFICATION_INITIATED = "bk.en.counter.initiate";
	public static final String NOTIFICATION_PENDINGAPPROVAL = "bk.en.counter.apply";
	public static final String NOTIFICATION_PENDINGPAYMENT = "bk.en.counter.approved";
	public static final String NOTIFICATION_REJECTED = "bk.en.counter.rejected";
	public static final String NOTIFICATION_APPROVED = "bk.en.counter.pay";
	public static final String NOTIFICATION_PENDINGASSIGNMENTDRIVER = "bk.en.counter.paidapply";
	public static final String NOTIFICATION_PENDINGUPDATE = "bk.en.counter.assigndriver";
	public static final String NOTIFICATION_DELIVERED = "BK_WF_BWT_DELIVERED";
	public static final String NOTIFICATION_NOTDELIVERED = "BK_WF_BWT_NOTDELIVERED";
	public static final String NOTIFICATION_APPLIED = "bk.en.counter.apply";
    public static final String NOTIFICATION_OBJECT_MODIFIED = "tl.en.edit.object.modified";
    public static final String DEFAULT_OBJECT_MODIFIED_MSG = "Dear <1>,Your Trade License with application number <APPLICATION_NUMBER> was modified.";
    public static  final String EMAIL_SUBJECT = "Chandigarh mSeva Application Status";
    public static final String BILL_AMOUNT_JSONPATH = "$.billResponse.Bill[0].totalAmount";
    public static final String TRADE_LICENSE_MODULE_CODE = "BK";
    public static final String NOTIFICATION_LOCALE = "en_IN";
    public static final String MODULE = "rainmaker-services";
    public static final String INITIATED = "INITIATED";
    public static final String PARKS = "Parks";
    public static final String COMMUNITY_CENTER = "Community Center";
    public static final String DEO = "DEO";
    public static final String CLERK = "CLERK";
    public static final String SENIOR_ASSISTANT = "SENIOR_ASSISTANT";
    public static final String AUDIT_DEPARTMENT = "AUDIT_DEPARTMENT";
    public static final String CHIEF_ACCOUNT_OFFICER = "CHIEF_ACCOUNT_OFFICER";
    public static final String PAYMENT_PROCESSING_AUTHORITY = "PAYMENT_PROCESSING_AUTHORITY";
    public static final String E_SAMPARK_CENTER = "E-SAMPARK-CENTER";
    public static final String MCC_USER = "MCC_USER";
    public static final String NLUJM_BOOKING_TYPE = "New Location Open Spaces Under Jurisdiction of MCC";
    public static final String NOTIFICATION_UPDATE = "bk.en.counter.update";
    public static final String BK_WF_NLUJM = "BK_WF_NLUJM_";
    public static final String BK_WF_OSBM = "BK_WF_OSBM_";
    public static final String BK_WF_BWT = "BK_WF_BWT_";
    public static final String BK = "BK_";
    public static final String BK_CGB = "BK_CGB_";
    public static final String BK_WF_OSUJM = "bk.en.counter.update";
    public static final String BK_WF_PACC = "bk.en.counter.update";
    
}
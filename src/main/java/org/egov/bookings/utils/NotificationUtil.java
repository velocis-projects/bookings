package org.egov.bookings.utils;

import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_APPLIED;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_APPROVED;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_DELIVERED;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_FAILUREAPPLY_PENDINGASSIGNMENTDRIVER;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_INITIATED;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_NOTDELIVERED;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PAIDAPPLY_PENDINGASSIGNMENTDRIVER;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PENDINGAPPROVAL;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PENDINGAPPROVALOSD;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PENDINGPAYMENT;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PENDINGPUBLISH;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PENDINGUPDATE;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_PUBLISH;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_REJECTED;
import static org.egov.bookings.utils.BookingsConstants.ACTION_STATUS_SPECIALAPPLY_DELIVERED;
import static org.egov.bookings.utils.BookingsConstants.BILL_AMOUNT_JSONPATH;
import static org.egov.bookings.utils.BookingsConstants.DEFAULT_OBJECT_MODIFIED_MSG;
import static org.egov.bookings.utils.BookingsConstants.NOTIFICATION_LOCALE;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.EmailRequest;
import org.egov.bookings.contract.EventRequest;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.SMSRequest;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.impl.BookingsServiceImpl;
import org.egov.bookings.service.impl.OsujmNewLocationServiceImpl;
import org.egov.common.contract.request.RequestInfo;
//import org.egov.tl.web.models.*;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class NotificationUtil.
 */
@Component

/** The Constant log. */
@Slf4j
public class NotificationUtil {

	/** The config. */
	private BookingsConfiguration config;

	/** The service request repository. */
	private ServiceRequestRepository serviceRequestRepository;

	/** The producer. */
	private BookingsProducer producer;
	
	@Autowired
	private BookingsServiceImpl bookingsServiceImpl;
	
	@Autowired
	private OsujmNewLocationServiceImpl osujmNewLocationServiceImpl;

	/**
	 * Instantiates a new notification util.
	 *
	 * @param config the config
	 * @param serviceRequestRepository the service request repository
	 * @param producer the producer
	 */
	@Autowired
	public NotificationUtil(BookingsConfiguration config, ServiceRequestRepository serviceRequestRepository,
			BookingsProducer producer) {
		this.config = config;
		this.serviceRequestRepository = serviceRequestRepository;
		this.producer = producer;
	}

	/** The receipt number key. */
	final String receiptNumberKey = "receiptNumber";

	/** The amount paid key. */
	final String amountPaidKey = "amountPaid";
	
	/** The consumer code key. */
	final String consumerCodeKey = "consumerCodeKey";

	public String getCustomizedMsg(RequestInfo requestInfo, BookingsModel bookingsModel, String localizationMessage) {
		String message = null, messageTemplate;
		String ACTION_STATUS = bookingsModel.getBkAction() + "_" + bookingsModel.getBkApplicationStatus();
		String applicationStatus = bookingsServiceImpl.prepareApplicationStatus(requestInfo, bookingsModel);
		switch (ACTION_STATUS) {
		//OSBM,OSUJM,NLUJM
		case ACTION_STATUS_INITIATED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_INITIATED, localizationMessage);
			message = getInitiatedMsg(bookingsModel, messageTemplate);
			break;
			
		case ACTION_STATUS_PENDINGAPPROVAL:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_PENDINGAPPROVAL, localizationMessage);
			message = getAppliedMsg(bookingsModel, messageTemplate);
			break;
			
		case ACTION_STATUS_PENDINGPAYMENT:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_PENDINGPAYMENT, localizationMessage);
			message = getPendingPaymentMsg(bookingsModel, messageTemplate);
			break;
			
		case ACTION_STATUS_REJECTED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_REJECTED, localizationMessage);
			message = getRejectedMsg(bookingsModel, messageTemplate);
			break;
			
		case ACTION_STATUS_APPROVED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_APPROVED, localizationMessage);
//			message = getApprovedMsg(bookingsModel,localizationMessage, messageTemplate);
			message = getPaymentMsg(bookingsModel, messageTemplate);
			break;
			
		//BWT
		case ACTION_STATUS_SPECIALAPPLY_DELIVERED:
		case ACTION_STATUS_PENDINGUPDATE:
		case ACTION_STATUS_DELIVERED:
		case ACTION_STATUS_NOTDELIVERED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_PENDINGUPDATE, localizationMessage);
			message = getUpdatedMsg(bookingsModel, messageTemplate, applicationStatus);
			break;
			
		case ACTION_STATUS_PAIDAPPLY_PENDINGASSIGNMENTDRIVER:	
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_PENDINGASSIGNMENTDRIVER, localizationMessage);
//			message = getApprovedMsg(bookingsModel,localizationMessage, messageTemplate);
			message = getPendingAssignmentDriverMsg(bookingsModel, messageTemplate);
			break;
		
		case ACTION_STATUS_FAILUREAPPLY_PENDINGASSIGNMENTDRIVER:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_PENDINGASSIGNMENTDRIVER, localizationMessage);
			message = getPendingAssignmentDriverMsg(bookingsModel, messageTemplate);
			break;
			
		//GFCP
		case ACTION_STATUS_APPLIED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_APPLIED, localizationMessage);
			message = getAppliedMsg(bookingsModel, messageTemplate);
			break;
		}
		return message;
	}
	
	
	public String getCustomizedMsg(RequestInfo requestInfo, OsujmNewLocationModel osujmNewLocationModel, String localizationMessage) {
		String message = null, messageTemplate;
		String ACTION_STATUS = osujmNewLocationModel.getAction() + "_" + osujmNewLocationModel.getApplicationStatus();
		String applicationStatus = osujmNewLocationServiceImpl.prepareApplicationStatus(requestInfo, osujmNewLocationModel);
		BigDecimal amountToBePaid = new BigDecimal(0);
		switch (ACTION_STATUS) {
		//NLUJM
		case ACTION_STATUS_INITIATED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_INITIATED, localizationMessage);
			message = getNLUJMInitiatedMsg(osujmNewLocationModel, messageTemplate);
			break;
			
		case ACTION_STATUS_PENDINGAPPROVAL:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_PENDINGAPPROVAL, localizationMessage);
			message = getNLUJMAppliedMsg(osujmNewLocationModel, messageTemplate);
			break;
			
		case ACTION_STATUS_REJECTED:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_REJECTED, localizationMessage);
			message = getNLUJMRejectedMsg(osujmNewLocationModel, messageTemplate);
			break;
			
		case ACTION_STATUS_PENDINGAPPROVALOSD:
		case ACTION_STATUS_PENDINGPUBLISH:
		case ACTION_STATUS_PUBLISH:
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_UPDATE, localizationMessage);
			message = getNLUJMUpdatedMsg(osujmNewLocationModel, messageTemplate, applicationStatus);
			break;
		}
		return message;
	}
	/**
	 * Creates customized message based on tradelicense.
	 *
	 * @param requestInfo the request info
	 * @param bookingsModel the bookings model
	 * @param localizationMessage            The messages from localization
	 * @return customized message based on tradelicense
	 */
	public String getCustomizedCTLMessage(RequestInfo requestInfo, BookingsModel bookingsModel, String localizationMessage) {
		String message = null, messageTemplate;
		String ACTION_STATUS = bookingsModel.getBkAction() + "_" + bookingsModel.getBkApplicationStatus();
		switch (ACTION_STATUS) {
		
//			case ACTION_FORWARD_CLERK:
//				messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_SUBMITTED, localizationMessage);
//				message = getSubittedMsg(license, messageTemplate, localizationMessage);
//				break;
//				
//			case ACTION_SENDFORCLARIFICATION_CLERK:
//				messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_SENDBACK_CITIZEN, localizationMessage);
//				message = getSendBackToCitizen(license, messageTemplate, localizationMessage);
//				break;
//				
//			case ACTION_STATUS_REJECTED:
//				messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_REJECTED, localizationMessage);
//				message = getRejectedMsgForCitizen(license, messageTemplate, localizationMessage);
//				break;
//				
//			case ACTION_STATUS_APPROVED:
//				BigDecimal amountToBePaid = getAmountToBePaid(requestInfo, license);
//				messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_APRROVED_AND_PAYMENT_PENDING, localizationMessage);
//				message = getApprovedAndPaymentPendingMsg(license,messageTemplate, localizationMessage, amountToBePaid);
//				break;
//				
//			case ACTION_STATUS_PAY:
//				BigDecimal amountToBePaid1 = getAmountToBePaid(requestInfo, license);
//				messageTemplate = getMessageTemplate(TLConstants.NOTIFICATION_APPROVED, localizationMessage);
//				message = getApprovedMsg(license, localizationMessage, messageTemplate);
//				break;
		}
		return message;
	}

	/**
	 * Extracts message for the specific code.
	 *
	 * @param notificationCode            The code for which message is required
	 * @param localizationMessage            The localization messages
	 * @return message for the specific code
	 */
	public String getMessageTemplate(String notificationCode, String localizationMessage) {
		String path = "$..messages[?(@.code==\"{}\")].message";
		path = path.replace("{}", notificationCode);
		String message = null;
		try {
			Object messageObj = JsonPath.parse(localizationMessage).read(path);
			message = ((ArrayList<String>) messageObj).get(0);
		} catch (Exception e) {
			log.warn("Fetching from localization failed", e);
		}
		return message;
	}

	/**
	 * Returns the uri for the localization call.
	 *
	 * @param tenantId            TenantId of the propertyRequest
	 * @param requestInfo the request info
	 * @return The uri for localization search call
	 */
	public StringBuilder getUri(String tenantId, RequestInfo requestInfo) {

		if (config.getIsLocalizationStateLevel())
			tenantId = tenantId.split("\\.")[0];

		String locale = NOTIFICATION_LOCALE;
		if (!StringUtils.isEmpty(requestInfo.getMsgId()) && requestInfo.getMsgId().split("|").length >= 2)
			locale = requestInfo.getMsgId().split("\\|")[1];

		StringBuilder uri = new StringBuilder();
		uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath())
				.append(config.getLocalizationSearchEndpoint()).append("?").append("locale=").append(locale)
				.append("&tenantId=").append(tenantId).append("&module=").append(BookingsConstants.MODULE);

		return uri;
	}

	/**
	 * Fetches messages from localization service.
	 *
	 * @param tenantId            tenantId of the tradeLicense
	 * @param requestInfo            The requestInfo of the request
	 * @return Localization messages for the module
	 */
	public String getLocalizationMessages(String tenantId, RequestInfo requestInfo) {
		LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(getUri(tenantId, requestInfo),
				requestInfo);
		String jsonString = new JSONObject(responseMap).toString();
		return jsonString;
	}

	/**
	 * Creates customized message for initiate.
	 *
	 * @param bookingsModel the bookings model
	 * @param message            Message from localization for initiate
	 * @return customized message for initiate
	 */
	private String getInitiatedMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<1>",bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkBookingType());
		message = message.replace("<3>", bookingsModel.getBkApplicationNumber());
		return message;
	}
	
	private String getNLUJMInitiatedMsg(OsujmNewLocationModel osujmNewLocationModel, String message) {
		message = message.replace("<1>",osujmNewLocationModel.getApplicantName());
		message = message.replace("<2>", BookingsConstants.NLUJM_BOOKING_TYPE);
		message = message.replace("<3>", osujmNewLocationModel.getApplicationNumber());
		return message;
	}
	/**
	 * Gets the pending payment msg.
	 *
	 * @param bookingsModel the bookings model
	 * @param message the message
	 * @return the pending payment msg
	 */
	private String getPendingPaymentMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<1>",bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkBookingType());
		message = message.replace("<3>", bookingsModel.getBkApplicationNumber());
		return message;
	}
	
	/**
	 * Creates customized message for apply.
	 *
	 * @param bookingsModel the bookings model
	 * @param message            Message from localization for apply
	 * @return customized message for apply
	 */
	private String getAppliedMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<1>",bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkBookingType());
		message = message.replace("<3>", bookingsModel.getBkApplicationNumber());
		return message;
	}
	
	private String getNLUJMAppliedMsg(OsujmNewLocationModel osujmNewLocationModel, String message) {
		message = message.replace("<1>",osujmNewLocationModel.getApplicantName());
		message = message.replace("<2>", BookingsConstants.NLUJM_BOOKING_TYPE);
		message = message.replace("<3>", osujmNewLocationModel.getApplicationNumber());
		return message;
	}
	/**
	 * Creates customized message for submitted.
	 *
	 * @param bookingsModel the bookings model
	 * @param localizationMessage the localization message
	 * @param message            Message from localization for submitted
	 * @return customized message for submitted
	 */
	
	
//	private String getSubittedMsg(TradeLicense license, String message, String localizationMessage) {
//		message = message.replace("<2>", getMessageTemplate(license.getBusinessService(), localizationMessage));
//		message = message.replace("<3>", license.getApplicationNumber());
//
//		return message;
//	}
	
	
//	private String getApprovalPendingMsg(TradeLicense license, String message) {
//		// message = message.replace("<1>",);
//		message = message.replace("<2>", license.getTradeName());
//
//		return message;
//	}

	/**
	 * Creates customized message for approved
	 * 
	 * @param license
	 *            tenantId of the tradeLicense
	 * @param message
	 *            Message from localization for approved
	 * @return customized message for approved
	 */
	private String getApprovedMsg(BookingsModel bookingsModel, String localizationMessage, String message) {
//		message = message.replace("<2>", license.getTradeName());
//		Date date=new Date(license.getValidTo());
		Date date=new Date("172800000");
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String validTODate = df2.format(date);
		message = message.replace("<2>", getMessageTemplate(bookingsModel.getBusinessService(), localizationMessage));
		message = message.replace("<3>", bookingsModel.getBkApplicationNumber());
		message = message.replace("<4>", validTODate);
		return message;
	}
	

	/**
 * Creates customized message for rejected.
 *
 * @param bookingsModel the bookings model
 * @param message            Message from localization for rejected
 * @return customized message for rejected
 */
	private String getRejectedMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<1>", bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkBookingType());
		return message;
	}
	
	private String getNLUJMRejectedMsg(OsujmNewLocationModel osujmNewLocationModel, String message) {
		message = message.replace("<1>",osujmNewLocationModel.getApplicantName());
		message = message.replace("<2>", BookingsConstants.NLUJM_BOOKING_TYPE);
		return message;
	}
	
	/**
	 * Gets the payment msg.
	 *
	 * @param bookingsModel the bookings model
	 * @param message the message
	 * @return the payment msg
	 */
	private String getPaymentMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<1>", bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkApplicationNumber());
		message = message.replace("<3>", bookingsModel.getBkBookingType());
		return message;
	}
	
	/**
	 * Gets the pending assignment driver msg.
	 *
	 * @param bookingsModel the bookings model
	 * @param message the message
	 * @return the pending assignment driver msg
	 */
	private String getPendingAssignmentDriverMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<1>", bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkBookingType());
		message = message.replace("<3>", bookingsModel.getBkStatus());
		message = message.replace("<4>", bookingsModel.getBkApplicationNumber());
		return message;
	}
	
	/**
	 * Gets the updated msg.
	 *
	 * @param bookingsModel the bookings model
	 * @param message the message
	 * @return the updated msg
	 */
	private String getUpdatedMsg(BookingsModel bookingsModel, String message, String applicationStatus) {
		message = message.replace("<1>", bookingsModel.getBkApplicantName());
		message = message.replace("<2>", bookingsModel.getBkApplicationNumber());
		message = message.replace("<3>", bookingsModel.getBkBookingType());
		message = message.replace("<4>", bookingsModel.getBkStatus());
		message = message.replace("<5>", applicationStatus);
		return message;
	}
	
	private String getNLUJMUpdatedMsg(OsujmNewLocationModel osujmNewLocationModel, String message, String applicationStatus) {
		message = message.replace("<1>", osujmNewLocationModel.getApplicantName());
		message = message.replace("<2>", osujmNewLocationModel.getApplicationNumber());
		message = message.replace("<3>", BookingsConstants.NLUJM_BOOKING_TYPE);
		message = message.replace("<4>", applicationStatus);
		return message;
	}
//	private String getRejectedMsgForCitizen(TradeLicense license, String message, String localizationMessage) {
//		// message = message.replace("<1>",);
//		//message = message.replace("<2>", license.getTradeName());
//		message = message.replace("<2>", getMessageTemplate(license.getBusinessService(), localizationMessage));
//		return message;
//	}
	
//	private String getSendBackToCitizen(TradeLicense license, String message,  String localizationMessage) {
//		//message = message.replace("<3>", license.getTradeName());
//		message = message.replace("<2>", getMessageTemplate(license.getBusinessService(), localizationMessage));
//		message = message.replace("<3>", license.getApplicationNumber());
//
//		return message;
//	}

	/**
	 * Send the SMSRequest on the SMSNotification kafka topic
	 * 
	 * @param smsRequestList
	 *            The list of SMSRequest to be sent
	 */
	public void sendSMS(List<SMSRequest> smsRequestList, boolean isSMSEnabled) {
		if (isSMSEnabled) {
			if (CollectionUtils.isEmpty(smsRequestList))
				log.info("Messages from localization couldn't be fetched!");
			for (SMSRequest smsRequest : smsRequestList) {
				producer.push(config.getSmsNotifTopic(), smsRequest);
				log.info("MobileNumber: " + smsRequest.getMobileNumber() + " Messages: " + smsRequest.getMessage());
			}
		}
	}
	
	
	
	/**
	 * Send the EMAILRequest on the EMAILNotification kafka topic.
	 *
	 * @param emailRequestList            The list of EMAILRequest to be sent
	 * @param isEMAILEnabled the is EMAIL enabled
	 */
	public void sendEMAIL(List<EmailRequest> emailRequestList, boolean isEMAILEnabled) {
		if (isEMAILEnabled) {
			if (CollectionUtils.isEmpty(emailRequestList))
				log.info("Messages from localization couldn't be fetched!");
			for (EmailRequest emailRequest : emailRequestList) {
				producer.pushEmail(config.getEmailNotifTopic(), emailRequest);
				log.info("EmailAddress: " + emailRequest.getEmail() + " Messages: " + emailRequest.getBody());
			}
		}
	}
	
	/**
	 * Send EMAIL.
	 *
	 * @param emailRequestList the email request list
	 * @param isEMAILEnabled the is EMAIL enabled
	 * @param emailSignature the email signature
	 */
	public void sendEMAIL(List<EmailRequest> emailRequestList, boolean isEMAILEnabled, String emailSignature) {
		if (isEMAILEnabled) {
			this.sendEMAIL(emailRequestList.stream().map(
				request -> {
					String body = new StringBuilder(request.getBody()).append(emailSignature).toString();
					body = body.replace("\\n", "<br/>");
					return EmailRequest.builder()
						.isHTML(true)
						.attachments(request.getAttachments())
						.body(body)
						.email(request.getEmail())
						.subject(request.getSubject())
						.build();
				}
			).collect(Collectors.toList()), true);
		}
	}

	/**
	 * Creates sms request for the each owners.
	 *
	 * @param message            The message for the specific tradeLicense
	 * @param mobileNumberToOwnerName            Map of mobileNumber to OwnerName
	 * @return List of SMSRequest
	 */
	public List<SMSRequest> createSMSRequest(String message, Map<String, String> mobileNumberToOwnerName) {
		List<SMSRequest> smsRequest = new LinkedList<>();
		for (Map.Entry<String, String> entryset : mobileNumberToOwnerName.entrySet()) {
			String customizedMsg = message.replace("<1>", entryset.getValue());
			smsRequest.add(new SMSRequest(entryset.getKey(), customizedMsg));
		}
		return smsRequest;
	}
	
	
	/**
	 * Creates email request for the each owners.
	 *
	 * @param message            The message for the specific tradeLicense
	 * @param emailIdToOwner            Map of emailId to OwnerName
	 * @return List of EMAILRequest
	 */
	public List<EmailRequest> createEMAILRequest(String message, Map<String, String> emailIdToOwner) {
		List<EmailRequest> emailRequest = new LinkedList<>();
		for (Map.Entry<String, String> entryset : emailIdToOwner.entrySet()) {
			String customizedMsg = message.replace("<1>", entryset.getValue());
			emailRequest.add(EmailRequest.builder()
					.email(entryset.getKey())
					.subject(BookingsConstants.EMAIL_SUBJECT)
					.body(customizedMsg)
					.isHTML(false)
					.build());
		}
		return emailRequest;
	}
	

	/**
	 * Gets the customized msg.
	 *
	 * @param diff the diff
	 * @param bookingsModel the bookings model
	 * @param localizationMessage the localization message
	 * @return the customized msg
	 */
	/*public String getCustomizedMsg(Difference diff, BookingsModel bookingsModel, String localizationMessage) {
		String message = null, messageTemplate;
		// StringBuilder finalMessage = new StringBuilder();

		
		 * if(!CollectionUtils.isEmpty(diff.getFieldsChanged())){ messageTemplate =
		 * getMessageTemplate(TLConstants.NOTIFICATION_FIELD_CHANGED,localizationMessage
		 * ); message = getEditMsg(license,diff.getFieldsChanged(),messageTemplate);
		 * finalMessage.append(message); }
		 * 
		 * if(!CollectionUtils.isEmpty(diff.getClassesAdded())){ messageTemplate =
		 * getMessageTemplate(TLConstants.NOTIFICATION_OBJECT_ADDED,localizationMessage)
		 * ; message = getEditMsg(license,diff.getClassesAdded(),messageTemplate);
		 * finalMessage.append(message); }
		 * 
		 * if(!CollectionUtils.isEmpty(diff.getClassesRemoved())){ messageTemplate =
		 * getMessageTemplate(TLConstants.NOTIFICATION_OBJECT_REMOVED,
		 * localizationMessage); message =
		 * getEditMsg(license,diff.getClassesRemoved(),messageTemplate);
		 * finalMessage.append(message); }
		 

//		if (!CollectionUtils.isEmpty(diff.getFieldsChanged()) || !CollectionUtils.isEmpty(diff.getClassesAdded())
//				|| !CollectionUtils.isEmpty(diff.getClassesRemoved())) {
			messageTemplate = getMessageTemplate(BookingsConstants.NOTIFICATION_OBJECT_MODIFIED, localizationMessage);
			if (messageTemplate == null)
				messageTemplate = DEFAULT_OBJECT_MODIFIED_MSG;
			message = getEditMsg(bookingsModel, messageTemplate);
//		}
		return message;
	}
*/
	/**
	 * Gets the edits the msg.
	 *
	 * @param bookingsModel the bookings model
	 * @param message the message
	 * @return the edits the msg
	 */
	private String getEditMsg(BookingsModel bookingsModel, String message) {
		message = message.replace("<APPLICATION_NUMBER>", bookingsModel.getBkApplicationNumber());
		return message;
	}

	/**
	 * Pushes the event request to Kafka Queue.
	 *
	 * @param request the request
	 */
	public void sendEventNotification(EventRequest request) {
		producer.push(config.getSaveUserEventsTopic(), request);
	}

}

package org.egov.bookings.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.CommonMasterFields;
import org.egov.bookings.contract.MasterRequest;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.repository.ParkCommunityHallV1MasterRepository;
import org.egov.bookings.repository.ParkCommunityInventoryRepsitory;
import org.egov.bookings.service.MasterService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class MasterServiceImpl.
 */
@Service
@Transactional
public class MasterServiceImpl implements MasterService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger( MasterServiceImpl.class.getName() );
	
	/** The park community inventory repsitory. */
	@Autowired
	private ParkCommunityInventoryRepsitory parkCommunityInventoryRepsitory;
	
	/** The osbm approver repository. */
	@Autowired
	private OsbmApproverRepository osbmApproverRepository; 

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;
	/** The common repository. */
	
	@Autowired
	private CommonRepository commonRepository;
	
	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;
	
	/** The osujm fee repository. */
	@Autowired
	private OsujmFeeRepository osujmFeeRepository;
	
	/** The bookings producer. */
	@Autowired
	private BookingsProducer bookingsProducer;
	
	/** The config. */
	@Autowired
	private BookingsConfiguration config;
	
	/** The bookings fields validator. */
	@Autowired
	private BookingsFieldsValidator bookingsFieldsValidator;
	
	/** The commercial ground fee repository. */
	@Autowired
	private CommercialGroundRepository commercialGroundFeeRepository;
	
	/** The park community hall V 1 master repository. */
	@Autowired
	private ParkCommunityHallV1MasterRepository parkCommunityHallV1MasterRepository;
	
	/**
	 * Gets the park community inventory details.
	 *
	 * @param venue the venue
	 * @param sector the sector
	 * @return the park community inventory details
	 */
	@Override
	public List<InventoryModel> getParkCommunityInventoryDetails(String venue, String sector) {
		List< InventoryModel > inventoryModelList = new ArrayList<>();
		try
		{
			inventoryModelList = parkCommunityInventoryRepsitory.getParkCommunityInventoryDetails(venue, sector);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getParkCommunityInventoryDetails " + e);
			e.printStackTrace();
		}
		return inventoryModelList;
	}
	
	/**
	 * Creates the approver.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createApprover(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid osbmApproverRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		try {
			masterRequest.getApproverList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateApproverBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getApproverList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getApproverList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveApproverTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("APPROVER_SAVE_ERROR", "ERROR WHILE SAVING APPROVER DETAILS");
		}
		return masterRequest.getApproverList();
	}

	
	/**
	 * Update approver.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateApprover(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid Approver id");
		}
		try {
			bookingsFieldsValidator.validateApproverBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getApproverList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getUpdateApproverTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("APPROVER_UPDATE_ERROR", "ERROR WHILE UPDATE APPROVER DETAILS");
		}
		return masterRequest.getApproverList();
	}
	
	
	/**
	 * Creates the OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createOSBMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		try {
			masterRequest.getOsbmFeeList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateOSBMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsbmFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getOsbmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveOsbmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSBM_FEE_SAVE_ERROR", "ERROR WHILE SAVING OSBM FEE DETAILS");
		}
		return masterRequest.getOsbmFeeList();
	}

	
	/**
	 * Update OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateOSBMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee id");
		}
		try {
			bookingsFieldsValidator.validateOSBMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsbmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getUpdateOsbmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSBM_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE OSBM FEE DETAILS");
		}
		return masterRequest.getOsbmFeeList();
	}
	
	/**
	 * Creates the OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createOSUJMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		try {
			masterRequest.getOsujmFeeList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateOSUJMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsujmFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getOsujmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveOsujmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSUJM_FEE_SAVE_ERROR", "ERROR WHILE SAVING OSUJM FEE DETAILS");
		}
		return masterRequest.getOsujmFeeList();
	}

	
	/**
	 * Update OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateOSUJMFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee id");
		}
		try {
			bookingsFieldsValidator.validateOSUJMFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getOsujmFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getUpdateOsujmFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("OSUJM_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE OSUJM FEE DETAILS");
		}
		return masterRequest.getOsujmFeeList();
	}
	
	/**
	 * Creates the GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createGFCPFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee List");
		}
		try {
			masterRequest.getGfcpFeeList().get(0).setId(UUID.randomUUID().toString());
			bookingsFieldsValidator.validateGFCPFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getGfcpFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getGfcpFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSaveGfcpFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("GFCP_FEE_SAVE_ERROR", "ERROR WHILE SAVING GFCP FEE DETAILS");
		}
		return masterRequest.getGfcpFeeList();
	}

	/**
	 * Update GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updateGFCPFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee id");
		}
		try {
			bookingsFieldsValidator.validateGFCPFeeBody(masterRequest);
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getGfcpFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getUpdateGfcpFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("GFCP_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE GFCP FEE DETAILS");
		}
		return masterRequest.getGfcpFeeList();
	}
	
	/**
	 * Creates the PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> createPACCFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee List");
		}
		try {
			masterRequest.getPaccFeeList().get(0).setId(UUID.randomUUID().toString());
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getPaccFeeList().get(0).setCreatedDate(formatter.format(new Date()));
			masterRequest.getPaccFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getSavePaccFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("PACC_FEE_SAVE_ERROR", "ERROR WHILE SAVING PACC FEE DETAILS");
		}
		return masterRequest.getPaccFeeList();
	}

	/**
	 * Update PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	@Override
	public List<CommonMasterFields> updatePACCFee(MasterRequest masterRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee id");
		}
		try {
			DateFormat formatter = getSimpleDateFormat();
			masterRequest.getPaccFeeList().get(0).setLastModifiedDate(formatter.format(new Date()));
			bookingsProducer.push(config.getUpdatePaccFeeTopic(), masterRequest);
		}catch (Exception e) {
			throw new CustomException("PACC_FEE_UPDATE_ERROR", "ERROR WHILE UPDATE PACC FEE DETAILS");
		}
		return masterRequest.getPaccFeeList();
	}
	
	/**
	 * Gets the simple date format.
	 *
	 * @return the simple date format
	 */
	private DateFormat getSimpleDateFormat() {
		DateFormat formatter = new SimpleDateFormat(BookingsConstants.DATE_FORMAT);
		formatter.setTimeZone(TimeZone.getTimeZone(BookingsConstants.TIME_ZONE));
		return formatter;
	}
	
	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.BookingsService#fetchAllApprover()
	 */
	@Override
	public List<BookingApprover> fetchAllApprover() {
		List<BookingApprover> bookingApprover1 = new ArrayList<>();
		List<?> userList = new ArrayList<>();
		try {
			String type = "EMPLOYEE";
			userList = commonRepository.fetchAllApprover(type);
			if (null == userList || CollectionUtils.isEmpty(userList)) {
				throw new CustomException("APPROVER_ERROR", "NO APPROVER EXISTS IN EG_USER TABLE");
			} else {
				if (!BookingsFieldsValidator.isNullOrEmpty(userList)) {
					for (Object userObject : userList) {
						BookingApprover bookingApprover = new BookingApprover();
						String jsonString = objectMapper.writeValueAsString(userObject);
						String approverDetails = jsonString.substring(1, (jsonString.length() - 1));
						String[] documentStrArray = approverDetails.split(",");
						for (int i = 0; i < documentStrArray.length; i++) {
							switch (i) {
							case 0:
								bookingApprover.setUserName(
										documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 1:
								bookingApprover.setMobileNumber(
										documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 2:
								bookingApprover
										.setName(documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 3:
								bookingApprover
										.setUuid(documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 4:
								bookingApprover.setId(Long.parseLong(documentStrArray[i]));
								break;
							default:
								break;
							}
						}
						bookingApprover1.add(bookingApprover);
					}
				}
			}
		}
		catch (Exception e) {
			throw new CustomException("APPROVER_ERROR", e.getLocalizedMessage());
		}
		return bookingApprover1;
	}
	
	/**
	 * Fetch all approver details.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<OsbmApproverModel> fetchAllApproverDetails() {
		List<OsbmApproverModel> approverList = new ArrayList<>();
		try {
			approverList = osbmApproverRepository.findAll();
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllApproverDetails " + e);
			e.printStackTrace();
		}
		return approverList;
	}

	/**
	 * Fetch all OSBM fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<OsbmFeeModel> fetchAllOSBMFee() {
		List<OsbmFeeModel> osbmFeeList = new ArrayList<>();
		try {
			osbmFeeList = osbmFeeRepository.findAll(); 
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllOSBMFee " + e);
			e.printStackTrace();
		}
		return osbmFeeList;
	}

	/**
	 * Fetch all OSUJM fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<OsujmFeeModel> fetchAllOSUJMFee() {
		List<OsujmFeeModel> osujmFeeList = new ArrayList<>();
		try {
			osujmFeeList = osujmFeeRepository.findAll(); 
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllOSUJMFee " + e);
			e.printStackTrace();
		}
		return osujmFeeList;
	}

	/**
	 * Fetch all GFCP fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<CommercialGroundFeeModel> fetchAllGFCPFee() {
		List<CommercialGroundFeeModel> gfcpFeeList = new ArrayList<>();
		try {
			gfcpFeeList = commercialGroundFeeRepository.findAll(); 
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllGFCPFee " + e);
			e.printStackTrace();
		}
		return gfcpFeeList;
	}
	
	/**
	 * Fetch all PACC fee.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Override
	public List<ParkCommunityHallV1MasterModel> fetchAllPACCFee() {
		List<ParkCommunityHallV1MasterModel> paccFeeList = new ArrayList<>();
		try {
			paccFeeList = parkCommunityHallV1MasterRepository.findAll(); 
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllPACCFee " + e);
			e.printStackTrace();
		}
		return paccFeeList;
	}
	
	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	@Override
	public List<String> getRoles() {
		List<String> roleList = new ArrayList<>();
		try {
			roleList = commonRepository.findRoles();
			
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the getRoles " + e);
			e.printStackTrace();
		}
		return roleList;
	}

	/**
	 * Gets the users.
	 *
	 * @param approver the approver
	 * @return the users
	 */
	@Override
	public List<UserDetails> getUsers(String approver) {
		if (BookingsFieldsValidator.isNullOrEmpty(approver)) 
		{
			throw new IllegalArgumentException("Invalid approver");
		}
		List<UserDetails> userDetailsList = new ArrayList<>();
		List<?> userList = new ArrayList<>();
		List<Integer> userId = new ArrayList<>();
		try {
			userId = commonRepository.findUserId(approver);
			if (!BookingsFieldsValidator.isNullOrEmpty(userId)) 
			{
				userList = commonRepository.findUserList(userId);
			}
			for (Object object : userList) {
				UserDetails userDetails = new UserDetails();
				String jsonString = objectMapper.writeValueAsString(object);
				String[] jsonArray = jsonString.split(",");
				userDetails.setUuid(jsonArray[0].substring(2, jsonArray[0].length() - 1));
				userDetails.setUserName(jsonArray[1].substring(1, jsonArray[1].length() - 2));
				userDetailsList.add(userDetails);
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the getUsers " + e);
			e.printStackTrace();
		}
		return userDetailsList;
	}

}

package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.repository.ParkCommunityInventoryRepsitory;
import org.egov.bookings.service.MasterService;
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
	 * Creates the osbm approver.
	 *
	 * @param osbmApproverRequest the osbm approver request
	 * @return the osbm approver model
	 */
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsbmApproverService#createOsbmApprover(org.egov.bookings.contract.OsbmApproverRequest)
	 */
	@Override
	public OsbmApproverModel createOsbmApprover(OsbmApproverRequest osbmApproverRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(osbmApproverRequest)) 
		{
			throw new IllegalArgumentException("Invalid osbmApproverRequest");
		}
		OsbmApproverModel osbmApproverModel = new OsbmApproverModel();
		try {
			osbmApproverModel.setSector(osbmApproverRequest.getSector());
			osbmApproverModel.setUuid(osbmApproverRequest.getUuid());
			osbmApproverRequest.setOsbmApproverModel(osbmApproverModel);
			bookingsProducer.push(config.getSaveApproverTopic(), osbmApproverRequest);
//			osbmApproverModel = osbmApproverRepository.save(osbmApproverModel);
		}catch (Exception e) {
			throw new CustomException("APPROVER_SAVE_ERROR", "ERROR WHILE SAVING OSBM SECTOR");
		}
		return osbmApproverRequest.getOsbmApproverModel();
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
	 * @return the list
	 */
	@Override
	public List<OsbmApproverModel> fetchAllApproverDetails() {
		List<OsbmApproverModel> osbmApproverList = new ArrayList<>();
		try {
			osbmApproverList = osbmApproverRepository.findAll();
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllApproverDetails " + e);
			e.printStackTrace();
		}
		return osbmApproverList;
	}

	/**
	 * Fetch all OSB mfee.
	 *
	 * @return the list
	 */
	@Override
	public List<OsbmFeeModel> fetchAllOSBMfee() {
		List<OsbmFeeModel> osbmFeeList = new ArrayList<>();
		try {
			osbmFeeList = osbmFeeRepository.findAll(); 
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllOSBMfee " + e);
			e.printStackTrace();
		}
		return osbmFeeList;
	}

	/**
	 * Fetch all OSUJ mfee.
	 *
	 * @return the list
	 */
	@Override
	public List<OsujmFeeModel> fetchAllOSUJMfee() {
		List<OsujmFeeModel> osujmFeeList = new ArrayList<>();
		try {
			osujmFeeList = osujmFeeRepository.findAll(); 
		}
		catch (Exception e) {
			LOGGER.error("Exception occur in the fetchAllOSUJMfee " + e);
			e.printStackTrace();
		}
		return osujmFeeList;
	}

}

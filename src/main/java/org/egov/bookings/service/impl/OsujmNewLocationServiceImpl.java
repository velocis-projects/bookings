package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.DocumentFields;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.Message;
import org.egov.bookings.contract.MessagesResponse;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsujmNewLocationRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.OsujmNewLocationService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.NewLocationRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.common.contract.request.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class OsujmNewLocationServiceImpl implements OsujmNewLocationService{

	@Autowired
	private BookingsService bookingsService;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	private BookingsConfiguration config;
	
	@Autowired
	private WorkflowIntegrator workflowIntegrator;

	@Autowired
	OsujmNewLocationRepository newLocationRepository;
	
	/** The common repository. */
	@Autowired
	CommonRepository commonRepository;
	
	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Logger LOGGER = LogManager.getLogger(OsujmNewLocationServiceImpl.class.getName());
	
	@Override
	public OsujmNewLocationModel addNewLocation(NewLocationRequest newLocationRequest) {
		OsujmNewLocationModel osujmNewLocationModel = new OsujmNewLocationModel();
		try
		{
			boolean flag = bookingsService.isBookingExists(newLocationRequest.getNewLocationModel().getApplicationNumber());

			if (!flag)
				enrichmentService.enrichNewLocationCreateRequest(newLocationRequest);

			if (config.getIsExternalWorkFlowEnabled()) {
				if (!flag)
					workflowIntegrator.callWorkFlow(newLocationRequest);
			}
			// bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
			enrichmentService.enrichNewLocationDetails(newLocationRequest);
			osujmNewLocationModel = newLocationRepository.save(newLocationRequest.getNewLocationModel());
			newLocationRequest.setNewLocationModel(osujmNewLocationModel);
			
		}
		catch (Exception e) {
			LOGGER.error("Exception occur during create booking " + e);
		}
		return newLocationRequest.getNewLocationModel();

	}

	@Override
	public OsujmNewLocationModel updateNewLocation(NewLocationRequest newLocationRequest) {

		String businessService = newLocationRequest.getNewLocationModel().getBusinessService();
		
		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(newLocationRequest);

		// bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		// bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		OsujmNewLocationModel newLocaltionModel = null;
		try {
			if (!BookingsConstants.APPLY.equals(newLocationRequest.getNewLocationModel().getAction())
					&& BookingsConstants.BUSINESS_SERVICE_NLUJM.equals(businessService)) {

				//newLocaltionModel = enrichmentService.enrichNlujmDetails(newLocationRequest);
				newLocaltionModel = newLocationRepository.save(newLocationRequest.getNewLocationModel());
			}

			 else {
				 newLocationRepository.save(newLocationRequest.getNewLocationModel());
				 newLocaltionModel = newLocationRequest.getNewLocationModel();
			}
		
		} catch (Exception e) {
			LOGGER.error("Exception occur while updating booking " + e);
		}
		return newLocaltionModel;
	}

	/**
	 * Gets the employee newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee newlocation search
	 */
	@Override
	public Booking getEmployeeNewlocationSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		Booking booking = new Booking();
		List<OsujmNewLocationModel> osujmNewLocationModelList = new ArrayList<>();
		Set<OsujmNewLocationModel> osujmNewLocationModelSet = new HashSet<>();
		List<?> documentList = new ArrayList<>();
		List<DocumentFields> newLocationDocumentList = new ArrayList<>();
		Set<String> applicationNumberSet = new HashSet<>();
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
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo()) 
					|| BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo().getUserInfo())) {
				throw new IllegalArgumentException("Invalid request info details");
			}
			List< Role > roles = searchCriteriaFieldsDTO.getRequestInfo().getUserInfo().getRoles();
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			
			if (BookingsFieldsValidator.isNullOrEmpty(roles)) {
				throw new IllegalArgumentException("Invalid roles");
			}
			for (Role role : roles) {
				if(!BookingsConstants.CITIZEN.equals(role.getCode()) && !BookingsConstants.EMPLOYEE.equals(role.getCode())) {
					applicationNumberSet.addAll(commonRepository.findApplicationNumber(role.getCode()));
				}
			}
			boolean flag = false;
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber) && !BookingsFieldsValidator.isNullOrEmpty(applicationNumberSet)) {
				if(applicationNumberSet.contains(applicationNumber))
				{
					flag = true;
					applicationNumberSet.clear();
					applicationNumberSet.add(applicationNumber);
				}
				if(!flag)
				{
					return booking;
				}
			}
			for (Role role : roles) {
				if(!BookingsConstants.CITIZEN.equals(role.getCode()) && !BookingsConstants.EMPLOYEE.equals(role.getCode()) ) {
					
					if(BookingsConstants.MCC_APPROVER.equals(role.getCode()) || BookingsConstants.OSD_APPROVER.equals(role.getCode()) || BookingsConstants.ADMIN_APPROVER.equals(role.getCode()))
					{
						List<String> sectorList = commonRepository.findSectorList(uuid);
						if (sectorList == null || sectorList.isEmpty()) {
							return booking;
						}
						if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							osujmNewLocationModelSet.addAll( newLocationRepository.getEmployeeNewlocationSearch(tenantId, applicationNumber,
									applicationStatus, mobileNumber, sectorList, applicationNumberSet));
						}
						else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							osujmNewLocationModelSet.addAll( newLocationRepository.getEmployeeNewlocationSearch(tenantId, applicationNumber,
									applicationStatus, mobileNumber, sectorList, applicationNumberSet, fromDate, toDate ));
						}
					}
				}
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocuments(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					DocumentFields documentFields = new DocumentFields();
					documentFields.setFileStoreId(documentStrArray[0].substring(2,documentStrArray[0].length()-1));
					String[] strArray = documentStrArray[1].split("/");
					documentFields.setFileName(strArray[strArray.length - 1].substring(13,(strArray[strArray.length - 1].length() - 2)));
					if(!"null".equals(documentStrArray[2].substring(0,documentStrArray[2].length()-1)))
					{
						documentFields.setDocumentType(documentStrArray[2].substring(1,documentStrArray[2].length()-2));
					}
					newLocationDocumentList.add(documentFields);
				}
			}
			osujmNewLocationModelList.addAll(osujmNewLocationModelSet);
			booking.setDocumentList(newLocationDocumentList);
			booking.setOsujmNewLocationModelList(osujmNewLocationModelList);
			booking.setBookingsCount(osujmNewLocationModelSet.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getEmployeeNewlocationSearch " + e);
		}
		return booking;
	}

	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen newlocation search
	 */
	@Override
	public Booking getCitizenNewlocationSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		Booking booking = new Booking();
		List<OsujmNewLocationModel> osujmNewLocationModelList = new ArrayList<>();
		List<?> documentList = new ArrayList<>();
		List<DocumentFields> newLocationDocumentList = new ArrayList<>();
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

			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				osujmNewLocationModelList = newLocationRepository.getCitizenNewlocationSearch(tenantId, applicationNumber,
						applicationStatus, mobileNumber, uuid);
			} else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate)
					&& !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				osujmNewLocationModelList = newLocationRepository.getCitizenNewlocationSearch(tenantId, applicationNumber,
						applicationStatus, mobileNumber, uuid, fromDate, toDate);
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocuments(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}

			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					DocumentFields documentFields = new DocumentFields();
					documentFields.setFileStoreId(documentStrArray[0].substring(2,documentStrArray[0].length()-1));
					String[] strArray = documentStrArray[1].split("/");
					documentFields.setFileName(strArray[strArray.length - 1].substring(13,(strArray[strArray.length - 1].length() - 2)));
					if(!"null".equals(documentStrArray[2].substring(0,documentStrArray[2].length()-1)))
					{
						documentFields.setDocumentType(documentStrArray[2].substring(1,documentStrArray[2].length()-2));
					}
					newLocationDocumentList.add(documentFields);
				}
			}
			booking.setDocumentList(newLocationDocumentList);
			booking.setOsujmNewLocationModelList(osujmNewLocationModelList);
			booking.setBookingsCount(osujmNewLocationModelList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getCitizenNewlocationSearch " + e);
		}
		return booking;
	}

	/**
	 * Gets the all citizen newlocation.
	 *
	 * @return the all citizen newlocation
	 */
	@Override
	public Booking getAllCitizenNewlocation() {
		Booking booking = new Booking();
		List< OsujmNewLocationModel > osujmNewLocationModelsList = new ArrayList<>();
		List< MdmsJsonFields > newLocationList = null;
		Map< String, List< MdmsJsonFields > > newLocationMap = new HashMap<>();
		int count;
		try {
			osujmNewLocationModelsList = newLocationRepository.getAllCitizenNewlocation();
			if (!BookingsFieldsValidator.isNullOrEmpty(osujmNewLocationModelsList)) {
				for (OsujmNewLocationModel osujmNewLocationModel : osujmNewLocationModelsList) {
					MdmsJsonFields mdmsJsonFields = new MdmsJsonFields();
					if( newLocationMap.containsKey(osujmNewLocationModel.getSector())) {
						count = 1;
						newLocationList = newLocationMap.get(osujmNewLocationModel.getSector());
						for (MdmsJsonFields mdmsJsonFields2 : newLocationList) {
							if(count < mdmsJsonFields2.getId()) {
								count = mdmsJsonFields2.getId();
							}
						}
						mdmsJsonFields.setActive(true);
						mdmsJsonFields.setCode(osujmNewLocationModel.getLocalityAddress());
						mdmsJsonFields.setName(osujmNewLocationModel.getLocalityAddress());
						mdmsJsonFields.setId(count+1);
						mdmsJsonFields.setTenantId(osujmNewLocationModel.getTenantId());
						newLocationList.add(mdmsJsonFields);
					}
					else {
						newLocationList = new ArrayList<>();
						count = 1;
						mdmsJsonFields.setActive(true);
						mdmsJsonFields.setCode(osujmNewLocationModel.getLocalityAddress());
						mdmsJsonFields.setName(osujmNewLocationModel.getLocalityAddress());
						mdmsJsonFields.setId(count);
						mdmsJsonFields.setTenantId(osujmNewLocationModel.getTenantId());
						newLocationList.add(mdmsJsonFields);
					}
					newLocationMap.put(osujmNewLocationModel.getSector(), newLocationList);
				}
			}
			booking.setOsujmNewlocationMap(newLocationMap);
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getCitizenNewlocationSearch " + e);
		}
		return booking;
	}

	/**
	 * Gets the new location document.
	 *
	 * @param venue the venue
	 * @param sector the sector
	 * @return the new location document
	 */
	@Override
	public List<DocumentFields> getNewLocationDocument(String venue, String sector) {
		if (BookingsFieldsValidator.isNullOrEmpty(venue)) 
		{
			throw new IllegalArgumentException("Invalid venue");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(sector)) 
		{
			throw new IllegalArgumentException("Invalid sector");
		}
		List<DocumentFields> documentsList = new ArrayList<>();
		String applicationNumber = "";
		List<?> documentList = new ArrayList<>();
		try
		{
			applicationNumber = newLocationRepository.findApplicationNumber(venue, sector);
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocuments(applicationNumber);
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					DocumentFields documentFields = new DocumentFields();
					documentFields.setFileStoreId(documentStrArray[0].substring(2,documentStrArray[0].length()-1));
					documentFields.setFileName(documentStrArray[1].substring(1,documentStrArray[1].length()-1));
					if(!"null".equals(documentStrArray[2].substring(0,documentStrArray[2].length()-1)))
					{
						documentFields.setDocumentType(documentStrArray[2].substring(1,documentStrArray[2].length()-2));
					}
					documentsList.add(documentFields);
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getNewLocationDocument " + e);
		}
		return documentsList;
	}

}

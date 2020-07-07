package org.egov.bookings.service.impl;

import java.time.LocalDate;
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
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.mdms.model.MdmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
	ObjectMapper objectMapper;
	
	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;
	
	/** The Constant LOGGER. */
	   private static final Logger LOGGER = LogManager.getLogger( BookingsServiceImpl.class.getName() );
	   
	/**
	 * Save.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	@Override
	public BookingsModel save(BookingsRequest bookingsRequest) {
		
		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);
		//bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		 enrichmentService.enrichBookingsDetails(bookingsRequest);
		 bookingsRepository.save(bookingsRequest.getBookingsModel());
		 return bookingsRequest.getBookingsModel();
		
		
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
	public Booking getCitizenSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO ) 
	{
		Booking booking = new Booking();
		List<BookingsModel> myBookingList = new ArrayList<>();
		List< ? > documentList = new ArrayList<>();
		Map< String, String > documentMap = new HashMap<>();
		try
		{
			if( BookingsFieldsValidator.isNullOrEmpty( searchCriteriaFieldsDTO ) )
			{
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
			
			if( BookingsFieldsValidator.isNullOrEmpty( tenantId ) )
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if( BookingsFieldsValidator.isNullOrEmpty( uuid ) )
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			if( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidOrderByBkApplicationNumberDesc( tenantId, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, applicationNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, applicationStatus, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, applicationStatus, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationStatus, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( tenantId, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				documentList = commonRepository.findDocumentList( applicationNumber );
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidOrderByBkApplicationNumberDesc( tenantId, applicationNumber, uuid );
				if( !BookingsFieldsValidator.isNullOrEmpty( myBookingList ) )
				{
					booking.setBusinessService(commonRepository.findBusinessService( applicationNumber ) );
				}
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuidOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, uuid, fromDate, toDate );
			}
//			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
//			{
//				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndUuid( tenantId, applicationStatus, uuid );
//			}
//			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
//			{
//				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuid( tenantId, applicationStatus, mobileNumber, uuid );
//			}
//			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
//			{
//				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, uuid, fromDate, toDate );
//			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidOrderByBkApplicationNumberDesc( tenantId, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, uuid, fromDate, toDate );
			}
			if( !BookingsFieldsValidator.isNullOrEmpty( documentList ) )
			{
				for( Object documentObject : documentList )
				{
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					String[] strArray = documentStrArray[1].split("/"); 
					String fileStoreId = documentStrArray[0].substring(2, documentStrArray[0].length()-1 );
					String document = strArray[strArray.length-1].substring(13, ( strArray[strArray.length-1].length()-2 ) );
					
//					string remarks = documentStrArray[2].substring(beginIndex, endIndex);
					documentMap.put(fileStoreId, document);
				}
			}
			booking.setDocumentMap(documentMap);
			booking.setBookingsModelList( myBookingList );
			booking.setBookingsCount( myBookingList.size() );
		}
		catch(Exception e)
		{
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
	public Booking getEmployeeSearchBooking(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) 
	{
		Booking booking = new Booking();
		List<BookingsModel> bookingsList = new ArrayList<>();
		List< ? > documentList = new ArrayList<>();
		Map< String, String > documentMap = new HashMap<>();
		try
		{
			if( BookingsFieldsValidator.isNullOrEmpty( searchCriteriaFieldsDTO ) )
			{
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
			if( BookingsFieldsValidator.isNullOrEmpty( tenantId ) )
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if( BookingsFieldsValidator.isNullOrEmpty( uuid ) )
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			List< String > sectorList = commonRepository.findSectorList( uuid );
			if( sectorList == null || sectorList.isEmpty() )
			{
				return booking;
			}
			if( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationStatus, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationStatus, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				documentList = commonRepository.findDocumentList( applicationNumber );
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, sectorList );
				if( !BookingsFieldsValidator.isNullOrEmpty( bookingsList ) )
				{
					booking.setBusinessService(commonRepository.findBusinessService( applicationNumber ) );
				}
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) ) )//
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, applicationStatus, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, applicationNumber, sectorList, fromDate, toDate );
			}
//			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
//			{
//				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkSectorIn( tenantId, applicationStatus, sectorList );
//			}
//			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
//			{
//				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorIn( tenantId, applicationStatus, mobileNumber, sectorList );
//			}
//			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
//			{
//				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, sectorList, fromDate, toDate );
//			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkSectorInOrderByBkApplicationNumberDesc( tenantId, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( tenantId, sectorList, fromDate, toDate );
			}
			if( !BookingsFieldsValidator.isNullOrEmpty( documentList ) )
			{
				for( Object documentObject : documentList )
				{
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					String[] strArray = documentStrArray[1].split("/"); 
					String fileStoreId = documentStrArray[0].substring(2, documentStrArray[0].length()-1 );
					String document = strArray[strArray.length-1].substring(13, ( strArray[strArray.length-1].length()-2 ) );
					documentMap.put(fileStoreId, document);
				}
			}
			booking.setDocumentMap(documentMap);
			booking.setBookingsModelList( bookingsList );
			booking.setBookingsCount( bookingsList.size() );
		}
		catch(Exception e)
		{
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
		//bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		//bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		BookingsModel bookingsModel = bookingsRepository.findByBkApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
		bookingsModel.setBkApplicationStatus(bookingsRequest.getBookingsModel().getBkApplicationStatus());
		bookingsModel.setBkAction(bookingsRequest.getBookingsModel().getBkAction());
		bookingsModel.setBookingsRemarks(bookingsRequest.getBookingsModel().getBookingsRemarks());
		bookingsRepository.save(bookingsModel);
		return bookingsModel;
	}

	/**
	 * Employee records count.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	@Override
	public Map< String, Integer > employeeRecordsCount(String tenantId, String uuid, BookingsRequest bookingsRequest) 
	{
		Map< String, Integer > bookingCountMap = new HashMap<>();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) 
			{
				throw new IllegalArgumentException("Invalid bookingsRequest");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) 
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			List< String > sectorList = commonRepository.findSectorList( uuid );
			int allRecordsCount = bookingsRepository.countByTenantIdAndBkSectorIn( tenantId, sectorList );
			bookingCountMap.put( "allRecordsCount", allRecordsCount );
			int bookingCount = 0;
			JSONArray mdmsArrayList = null;
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
				bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndBkSectorIn(tenantId, mdmsJsonFields.getCode(), sectorList);
				bookingCountMap.put(mdmsJsonFields.getCode(), bookingCount);
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the employeeRecordsCount " + e);
		}
		return bookingCountMap;
	}

	/**
	 * Citizen records count.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	@Override
	public Map< String, Integer > citizenRecordsCount(String tenantId, String uuid, BookingsRequest bookingsRequest)  
	{
		Map< String, Integer > bookingCountMap = new HashMap<>();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) 
			{
				throw new IllegalArgumentException("Invalid bookingsRequest");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) 
			{
				throw new IllegalArgumentException("Invalid tentantId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) 
			{
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
			for (int i = 0; i < mdmsArrayList.size(); i++) 
			{
				jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
				MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
				bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndUuid(tenantId, mdmsJsonFields.getCode(), uuid);
				bookingCountMap.put(mdmsJsonFields.getCode(), bookingCount);
			}
		} 
		catch (Exception e) 
		{
			LOGGER.error("Exception occur in the citizenRecordsCount " + e);
		}
		return bookingCountMap;
	}
}

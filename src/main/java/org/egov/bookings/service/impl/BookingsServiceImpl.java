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
				myBookingList =  bookingsRepository.findByTenantIdAndUuid( tenantId, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkBookingType( tenantId, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingType( tenantId, applicationNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuidAndBkBookingType( tenantId, applicationNumber, applicationStatus, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingType( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingType( tenantId, applicationNumber, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( tenantId, applicationNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndUuidAndBkBookingType( tenantId, applicationStatus, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingType( tenantId, applicationStatus, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkBookingType( tenantId, mobileNumber, uuid, bookingType );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetween( tenantId, mobileNumber, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkBookingTypeAndBkDateCreatedBetween( tenantId, uuid, bookingType, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuid( tenantId, applicationNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuid( tenantId, applicationNumber, applicationStatus, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuid( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuid( tenantId, applicationNumber, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndUuid( tenantId, applicationStatus, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuid( tenantId, applicationStatus, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuid( tenantId, mobileNumber, uuid );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, mobileNumber, uuid, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkDateCreatedBetween( tenantId, uuid, fromDate, toDate );
			}
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
				bookingsList =  bookingsRepository.findByTenantIdAndBkSectorIn( tenantId, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorIn( tenantId, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, mobileNumber, bookingType, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, mobileNumber, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( bookingType ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, bookingType, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkSectorIn( tenantId, applicationNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkSectorIn( tenantId, applicationNumber, applicationStatus, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorIn( tenantId, applicationNumber, applicationStatus, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) ) )//
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorIn( tenantId, applicationNumber, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkSectorIn( tenantId, applicationStatus, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorIn( tenantId, applicationStatus, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkSectorIn( tenantId, mobileNumber, sectorList );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetween( tenantId, mobileNumber, sectorList, fromDate, toDate );
			}
			else if( !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( bookingType ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
			{
				bookingsList =  bookingsRepository.findByTenantIdAndBkSectorInAndBkDateCreatedBetween( tenantId, sectorList, fromDate, toDate );
			}
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
		 bookingsRepository.save(bookingsRequest.getBookingsModel());
		 return bookingsRequest.getBookingsModel();
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

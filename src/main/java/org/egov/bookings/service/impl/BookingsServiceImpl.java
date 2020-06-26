package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.enums.BookingTypeEnum;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
		bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
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
			throw new IllegalArgumentException("Invalid user uuId");
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
		booking.setBookingsModelList( myBookingList );
		booking.setBookingsCount( myBookingList.size() );
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
			throw new IllegalArgumentException("Invalid user uuId");
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
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, mobileNumber, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) 
				&& !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, mobileNumber, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( fromDate ) && BookingsFieldsValidator.isNullOrEmpty( toDate ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, mobileNumber, bookingType, sectorList );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) && !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( !BookingsFieldsValidator.isNullOrEmpty( fromDate ) && !BookingsFieldsValidator.isNullOrEmpty( toDate ) && ( BookingsFieldsValidator.isNullOrEmpty( applicationNumber ) && BookingsFieldsValidator.isNullOrEmpty( applicationStatus ) && BookingsFieldsValidator.isNullOrEmpty( mobileNumber ) ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, bookingType, sectorList, fromDate, toDate );
		}
		booking.setBookingsModelList( bookingsList );
		booking.setBookingsCount( bookingsList.size() );
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
	 * Gets the all employee records.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @return the all employee records
	 */
	@Override
	public Map< String, Integer > getAllEmployeeRecords(String tenantId, String uuid) 
	{
		Map< String, Integer > bookingCountMap = new HashMap<>();
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( uuid == null || uuid == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		List< String > sectorList = commonRepository.findSectorList( uuid );
		int allRecordsCount = bookingsRepository.countByTenantIdAndBkSectorIn( tenantId, sectorList );
		bookingCountMap.put( "allRecordsCount", allRecordsCount );
		int bookingCount = 0;
		for ( BookingTypeEnum bookingTypeEnum : BookingTypeEnum.values() ) 
		{
			bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndBkSectorIn( tenantId, bookingTypeEnum.getName(), sectorList );
			bookingCountMap.put( bookingTypeEnum.getName(), bookingCount );
		}
		return bookingCountMap;
	}

	/**
	 * Gets the all citizen records.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @return the all citizen records
	 */
	@Override
	public Map< String, Integer > getAllCitizenRecords(String tenantId, String uuid) 
	{
		Map< String, Integer > bookingCountMap = new HashMap<>();
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( uuid == null || uuid == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		int allRecordsCount = bookingsRepository.countByTenantIdAndUuid( tenantId, uuid );
		bookingCountMap.put( "allRecordsCount", allRecordsCount );
		int bookingCount = 0;
		for ( BookingTypeEnum bookingTypeEnum : BookingTypeEnum.values() ) 
		{
			bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndUuid( tenantId, bookingTypeEnum.getName(), uuid );
			bookingCountMap.put( bookingTypeEnum.getName(), bookingCount );
		}
		return bookingCountMap;
	}
}

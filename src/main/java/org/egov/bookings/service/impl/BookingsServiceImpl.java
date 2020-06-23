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
		if( searchCriteriaFieldsDTO == null )
		{
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		
		String tenantId = searchCriteriaFieldsDTO.getTenantId();
		String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
		String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
		String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
		Date fromDate = searchCriteriaFieldsDTO.getFromDate();
		Date toDate = searchCriteriaFieldsDTO.getToDate();
		String uuId = searchCriteriaFieldsDTO.getUuId();
		
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( uuId == null || uuId == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		int bookingsCount = bookingsRepository.countByTenantIdAndUuid( tenantId, uuId );
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndUuid( tenantId, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuid( tenantId, applicationNumber, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuid( tenantId, applicationNumber, applicationStatus, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuid( tenantId, applicationNumber, applicationStatus, mobileNumber, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, uuId, fromDate, toDate );
		}
		
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuid( tenantId, applicationNumber, mobileNumber, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, uuId, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationNumber, uuId, fromDate, toDate );
		}
		
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndUuid( tenantId, applicationStatus, uuId );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuid( tenantId, applicationStatus, mobileNumber, uuId );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, uuId, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuid( tenantId, mobileNumber, uuId );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuidAndBkDateCreatedBetween( tenantId, mobileNumber, uuId, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndUuidAndBkDateCreatedBetween( tenantId, uuId, fromDate, toDate );
		}
		booking.setBookingsModelList( myBookingList );
		booking.setBookingsCount( bookingsCount );
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
		if( searchCriteriaFieldsDTO == null )
		{
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		
		String tenantId = searchCriteriaFieldsDTO.getTenantId();
		String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
		String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
		String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
		Date fromDate = searchCriteriaFieldsDTO.getFromDate();
		Date toDate = searchCriteriaFieldsDTO.getToDate();
		String uuid = searchCriteriaFieldsDTO.getUuId();
		String bookingType = searchCriteriaFieldsDTO.getBookingType();
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( uuid == null || uuid == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		if( bookingType == null || bookingType == "" )
		{
			throw new IllegalArgumentException("Invalid booking type");
		}
		
		List< String > sectorList = commonRepository.findSectorList( uuid );
		if( sectorList == null || sectorList.isEmpty() )
		{
			return booking;
		}
		int bookingsCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndBkSectorIn( tenantId, bookingType, sectorList );
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorIn( tenantId, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, mobileNumber, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, bookingType, sectorList );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, mobileNumber, bookingType, sectorList );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, mobileNumber, bookingType, sectorList );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, bookingType, sectorList, fromDate, toDate );
		}
		booking.setBookingsModelList(bookingsList);
		booking.setBookingsCount(bookingsCount);
		return booking;
	
	}
	
	
	/**
	 * Gets the employee search booking.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingType the booking type
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee search booking
	 */
	@Override
	public Booking getEmployeeSearchBooking( String tenantId, String uuid, String bookingType, String applicationNumber, String applicationStatus,
			String mobileNumber, Date fromDate, Date toDate ) 
	{
		Booking booking = new Booking();
		List<BookingsModel> bookingsList = new ArrayList<>();
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( uuid == null || uuid == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		if( bookingType == null || bookingType == "" )
		{
			throw new IllegalArgumentException("Invalid booking type");
		}
		
		List< String > sectorList = commonRepository.findSectorList( uuid );
		if( sectorList == null || sectorList.isEmpty() )
		{
			return booking;
		}
		int bookingsCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndBkSectorIn( tenantId, bookingType, sectorList );
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorIn( tenantId, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationNumber, mobileNumber, bookingType, sectorList );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, bookingType, sectorList );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, applicationStatus, mobileNumber, bookingType, sectorList );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorIn( tenantId, mobileNumber, bookingType, sectorList );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, mobileNumber, bookingType, sectorList, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetween( tenantId, bookingType, sectorList, fromDate, toDate );
		}
		booking.setBookingsModelList(bookingsList);
		booking.setBookingsCount(bookingsCount);
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

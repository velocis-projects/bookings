package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookingsServiceImpl implements BookingsService {

	@Autowired
	private BookingsRepository bookingsRepository;

	@Autowired
	private BookingsProducer bookingsProducer;

	@Value("${kafka.topics.save.service}")
	private String saveTopic;
	
	@Autowired
	private BookingsConfiguration config;
	
	@Autowired
	private WorkflowIntegrator workflowIntegrator;

	@Autowired
	OsbmApproverRepository osbmApproverRepository;
	
	@Autowired
	CommonRepository commonRepository;
	
	@Override
	public BookingsModel save(BookingsRequest bookingsRequest) {
		
		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);
		//bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		 bookingsRepository.save(bookingsRequest.getBookingsModel());
		 return bookingsRequest.getBookingsModel();
		
		
	}

	@Override
	public List<BookingsModel> getAllBuildingMaterial() {
		return (List<BookingsModel>) bookingsRepository.findAll();
	}

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
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( uuid == null || uuid == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		
		List< String > applicationNumberList = commonRepository.findApplicationNumber( uuid );
		if( applicationNumberList == null || applicationNumberList.isEmpty() )
		{
			return booking;
		}
		int bookingsCount = bookingsRepository.countByTenantIdAndBkApplicationNumberIn( tenantId, applicationNumberList );
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberIn( tenantId, applicationNumberList );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationNumberIn( tenantId, applicationNumber, applicationNumberList );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkApplicationNumberIn( tenantId, applicationNumber, applicationStatus, applicationNumberList );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberIn( tenantId, applicationNumber, applicationStatus, mobileNumber, applicationNumberList );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, applicationNumberList, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkApplicationNumberIn( tenantId, applicationNumber, mobileNumber, applicationNumberList );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, applicationNumberList, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationNumberInAndBkDateCreatedBetween( tenantId, applicationNumber, applicationNumberList, fromDate, toDate );
		}
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkApplicationNumberIn( tenantId, applicationStatus, applicationNumberList );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberIn( tenantId, applicationStatus, mobileNumber, applicationNumberList );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, applicationNumberList, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkApplicationNumberIn( tenantId, mobileNumber, applicationNumberList );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( tenantId, mobileNumber, applicationNumberList, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			bookingsList =  bookingsRepository.findByTenantIdAndBkApplicationNumberInAndBkDateCreatedBetween( tenantId, applicationNumberList, fromDate, toDate );
		}
		booking.setBookingsModelList(bookingsList);
		booking.setBookingsCount(bookingsCount);
		return booking;
	
	}
		
	@Override
	public BookingsModel update(BookingsRequest bookingsRequest) {
		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);
		//bookingsProducer.push(saveTopic, bookingsRequest.getBookingsModel());
		//bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		 bookingsRepository.save(bookingsRequest.getBookingsModel());
		 return bookingsRequest.getBookingsModel();
	}

}

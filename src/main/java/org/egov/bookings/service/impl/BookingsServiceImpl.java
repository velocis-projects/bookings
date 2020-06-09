package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.BookingsRepository;
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
	public List<BookingsModel> getCitizenSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO ) 
	{
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
		
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndUuId( tenantId, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuId( tenantId, applicationNumber, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuId( tenantId, applicationNumber, applicationStatus, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuId( tenantId, applicationNumber, applicationStatus, mobileNumber, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, uuId, fromDate, toDate );
		}
		
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuId( tenantId, applicationNumber, mobileNumber, uuId );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, uuId, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndUuIdAndBkDateCreatedBetween( tenantId, applicationNumber, uuId, fromDate, toDate );
		}
		
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndUuId( tenantId, applicationStatus, uuId );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuId( tenantId, applicationStatus, mobileNumber, uuId );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, uuId, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuId( tenantId, mobileNumber, uuId );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndUuIdAndBkDateCreatedBetween( tenantId, mobileNumber, uuId, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndUuIdAndBkDateCreatedBetween( tenantId, uuId, fromDate, toDate );
		}
		return myBookingList;
	}

	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	@Override
	public List<BookingsModel> getEmployeeSearchBooking(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) 
	{

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
		String roleCode = searchCriteriaFieldsDTO.getRoleCode();
		int userId = searchCriteriaFieldsDTO.getUserId();
		String uuId = searchCriteriaFieldsDTO.getUuId();
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if( userId <= 0 )
		{
			throw new IllegalArgumentException("Invalid userId");
		}
		if( roleCode == null || roleCode == "" )
		{
			throw new IllegalArgumentException("Invalid user role code");
		}
		if( uuId == null || uuId == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			myBookingList =  bookingsRepository.findByTenantId( tenantId );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumber( tenantId, applicationNumber );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatus( tenantId, applicationNumber, applicationStatus );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumber( tenantId, applicationNumber, applicationStatus, mobileNumber );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, fromDate, toDate );
		}
		
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumber( tenantId, applicationNumber, mobileNumber );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkDateCreatedBetween( tenantId, applicationNumber, fromDate, toDate );
		}
		
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatus( tenantId, applicationStatus );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumber( tenantId, applicationStatus, mobileNumber );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumber( tenantId, mobileNumber );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkDateCreatedBetween( tenantId, mobileNumber, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkDateCreatedBetween( tenantId, fromDate, toDate );
		}
		return myBookingList;
	
	}

}

package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.BookingsRepository;
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
		String sector = searchCriteriaFieldsDTO.getSector();
		if( tenantId == null || tenantId == "" )
		{
			throw new IllegalArgumentException("Invalid tentantId");
		}
//		if( userId <= 0 )
//		{
//			throw new IllegalArgumentException("Invalid userId");
//		}
//		if( roleCode == null || roleCode == "" )
//		{
//			throw new IllegalArgumentException("Invalid user role code");
//		}
		if( uuId == null || uuId == "" )
		{
			throw new IllegalArgumentException("Invalid user uuId");
		}
		if( sector == null || sector == "" )
		{
			throw new IllegalArgumentException("Invalid user sector");
		}
		
		OsbmApproverModel osbmApproverModel = osbmApproverRepository.findBySector( sector );
		if ( osbmApproverModel == null ) {
			throw new IllegalArgumentException("Invalid osbmApproverModel");
		}
		if ( osbmApproverModel.getUuid() != null && osbmApproverModel.getUuid() != "" && !osbmApproverModel.getUuid().equals( uuId ) ) {
			throw new IllegalArgumentException("User can't search booking due to authentication");
		}
		
		if( applicationNumber == null && applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkSector( tenantId, sector );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& (applicationStatus == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkSector( tenantId, applicationNumber, sector );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& applicationStatus != null && applicationStatus != "" && (mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkSector( tenantId, applicationNumber, applicationStatus, sector );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && (fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSector( tenantId, applicationNumber, applicationStatus, mobileNumber, sector );
		}
		else if( applicationNumber != null && applicationNumber != "" && applicationStatus != null 
				&& applicationStatus != "" && mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( tenantId, applicationNumber, applicationStatus, mobileNumber, sector, fromDate, toDate );
		}
		
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && (applicationStatus == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSector( tenantId, applicationNumber, mobileNumber, sector );
		}
		else if( applicationNumber != null && applicationNumber != "" && mobileNumber != null 
				&& mobileNumber != "" && fromDate != null && toDate != null && (applicationStatus == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( tenantId, applicationNumber, mobileNumber, sector, fromDate, toDate );
		}
		else if( applicationNumber != null && applicationNumber != "" 
				&& fromDate != null && toDate != null && (applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationNumberAndBkSectorAndBkDateCreatedBetween( tenantId, applicationNumber, sector, fromDate, toDate );
		}
		
		else if( applicationStatus != null && applicationStatus != "" 
				&& (applicationNumber == null && mobileNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkSector( tenantId, applicationStatus, sector );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& (applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSector( tenantId, applicationStatus, mobileNumber, sector );
		}
		else if( applicationStatus != null && applicationStatus != "" && mobileNumber != null && mobileNumber != "" 
				&& fromDate != null && toDate != null && (applicationNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( tenantId, applicationStatus, mobileNumber, sector, fromDate, toDate );
		}
		else if( mobileNumber != null && mobileNumber != "" 
				&& (applicationStatus == null && applicationNumber == null && fromDate == null && toDate == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkSector( tenantId, mobileNumber, sector );
		}
		else if( mobileNumber != null && mobileNumber != "" && fromDate != null && toDate != null 
				&& (applicationNumber == null && applicationStatus == null  ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkMobileNumberAndBkSectorAndBkDateCreatedBetween( tenantId, mobileNumber, sector, fromDate, toDate );
		}
		if( fromDate != null && toDate != null && (applicationNumber == null && applicationStatus == null && mobileNumber == null ) )
		{
			myBookingList =  bookingsRepository.findByTenantIdAndBkSectorAndBkDateCreatedBetween( tenantId, sector, fromDate, toDate );
		}
		return myBookingList;
	
	}

}

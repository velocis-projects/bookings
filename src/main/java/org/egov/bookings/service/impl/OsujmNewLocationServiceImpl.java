package org.egov.bookings.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.Message;
import org.egov.bookings.contract.MessagesResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.repository.OsujmNewLocationRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.OsujmNewLocationService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.NewLocationRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	private static final Logger LOGGER = LogManager.getLogger(OsujmNewLocationServiceImpl.class.getName());
	
	@Override
	public NewLocationRequest addNewLocation(NewLocationRequest newLocationRequest) {
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
		return newLocationRequest;

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

}

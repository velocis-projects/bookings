package org.egov.bookings.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.DocumentFields;
import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.MasterService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterController.
 */
@RestController
@RequestMapping("/master")
public class MasterController {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger( MasterController.class.getName() );
	
	/** The master service. */
	@Autowired
	private MasterService masterService;
	
	/** The bookings fields validator. */
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	/** The bookings service. */
	@Autowired
	private BookingsService bookingsService;
	
	/**
	 * Gets the park community inventory details.
	 *
	 * @param documentfields the documentfields
	 * @return the park community inventory details
	 */
	@PostMapping(value = "/park/community/hall/inventory/_search")
	private ResponseEntity<?> getParkCommunityInventoryDetails(@RequestBody DocumentFields documentfields )
	{
		if (BookingsFieldsValidator.isNullOrEmpty(documentfields)) 
		{
			throw new IllegalArgumentException("Invalid documentfields");
		}
		List< InventoryModel > inventoryModelList = new ArrayList<>();
		try
		{
			inventoryModelList = masterService.getParkCommunityInventoryDetails(documentfields.getVenue(), documentfields.getSector());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getParkCommunityInventoryDetails " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(inventoryModelList);
	}
	
	
	/**
	 * Creates the osbm approver.
	 *
	 * @param osbmApproverRequest the osbm approver request
	 * @return the response entity
	 */
	@PostMapping("/approver/_create")
	public ResponseEntity<?> createOsbmApprover(@RequestBody OsbmApproverRequest osbmApproverRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(osbmApproverRequest)) 
		{
			throw new IllegalArgumentException("Invalid osbmApproverRequest");
		}
		ResponseModel rs = new ResponseModel();
		try {
			bookingsFieldsValidator.validateOsbmApproverBody(osbmApproverRequest);
			OsbmApproverModel osbmModel = masterService.createOsbmApprover(osbmApproverRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in osbm table");
			rs.setData(osbmModel);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createOsbmApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	
	/**
	 * Fetch all approver.
	 *
	 * @return the response entity
	 */
	@GetMapping("all/approver/_fetch")
	public ResponseEntity<?> fetchAllApprover() {
		ResponseModel rs = new ResponseModel();
		try {
			List<BookingApprover> bookingApproverList = masterService.fetchAllApprover();
			rs.setStatus("200");
			rs.setMessage("Data submitted successfully");
			rs.setData(bookingApproverList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
}

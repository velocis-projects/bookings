package org.egov.bookings.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.DocumentFields;
import org.egov.bookings.contract.MasterRequest;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.service.MasterService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	 * Creates the approver.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping(value = "/approver/_create")
	public ResponseEntity<?> createApprover(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmApproverModel> approverModelList = masterService.createApprover(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in approver table");
			rs.setData(approverModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update approver.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/approver/_update")
	public ResponseEntity<?> updateApprover(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver Model");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid Approver id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmApproverModel> approverModelList = masterService.updateApprover(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in approver table");
			rs.setData(approverModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Creates the OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osbm/fee/_create")
	public ResponseEntity<?> createOSBMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmFeeModel> osbmFeeModelList = masterService.createOSBMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in OSBM Fee table");
			rs.setData(osbmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createOSBMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osbm/fee/_update")
	public ResponseEntity<?> updateOSBMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmFeeModel> osbmFeeModelList = masterService.updateOSBMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in OSBM Fee table");
			rs.setData(osbmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Creates the OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osujm/fee/_create")
	public ResponseEntity<?> createOSUJMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<OsujmFeeModel> osujmFeeModelList = masterService.createOSUJMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in OSUJM Fee table");
			rs.setData(osujmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createOSUJMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osujm/fee/_update")
	public ResponseEntity<?> updateOSUJMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<OsujmFeeModel> osujmFeeModelList = masterService.updateOSUJMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in OSUJM Fee table");
			rs.setData(osujmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateOSUJMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	
	
	/**
	 * Fetch all approver.
	 *
	 * @return the response entity
	 */
	@PostMapping("all/approver/_fetch")
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
	
	
	/**
	 * Fetch all approver details.
	 *
	 * @return the response entity
	 */
	@PostMapping("/approver/_fetch")
	public ResponseEntity<?> fetchAllApproverDetails() {
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmApproverModel> osbmApproverList = masterService.fetchAllApproverDetails(); 
			rs.setStatus("200");
			rs.setMessage("Data submitted successfully");
			rs.setData(osbmApproverList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllOSBMfee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all OSB mfee.
	 *
	 * @return the response entity
	 */
	@PostMapping("osbm/fee/_fetch")
	public ResponseEntity<?> fetchAllOSBMfee() {
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmFeeModel> osbmFeeList = masterService.fetchAllOSBMfee(); 
			rs.setStatus("200");
			rs.setMessage("Data submitted successfully");
			rs.setData(osbmFeeList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllOSBMfee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all OSUJ mfee.
	 *
	 * @return the response entity
	 */
	@PostMapping("osujm/fee/_fetch")
	public ResponseEntity<?> fetchAllOSUJMfee() {
		ResponseModel rs = new ResponseModel();
		try {
			List<OsujmFeeModel> osbmFeeList = masterService.fetchAllOSUJMfee(); 
			rs.setStatus("200");
			rs.setMessage("Data submitted successfully");
			rs.setData(osbmFeeList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllOSUJMfee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
}

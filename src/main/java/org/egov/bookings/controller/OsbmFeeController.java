package org.egov.bookings.controller;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.OsbmSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.service.OsbmFeeService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class OsbmFeeController.
 */
@RestController
@RequestMapping("/osbm/fee")
public class OsbmFeeController {

	
	/** The bookings fields validator. */
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;

	/** The osbm fee service. */
	@Autowired
	OsbmFeeService osbmFeeService;
	
	/**
	 * Search osbm fee.
	 *
	 * @param osbmSearchCriteria the osbm search criteria
	 * @return the response entity
	 */
	@PostMapping("/_search")
	private ResponseEntity<?> searchOsbmFee(
			@RequestBody OsbmSearchCriteria osbmSearchCriteria) {
		
		bookingsFieldsValidator.validateOsbmSearchCriteria(osbmSearchCriteria);
		
		OsbmFeeModel osbmFeeModel = osbmFeeService.searchOsbmFee(osbmSearchCriteria);
		
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data Fetched successfully");
		rs.setData(osbmFeeModel);
		
		return ResponseEntity.ok(rs);
	}
	
}

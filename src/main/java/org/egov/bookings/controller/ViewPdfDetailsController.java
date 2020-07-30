package org.egov.bookings.controller;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.ViewPdfDetailsModel;
import org.egov.bookings.service.ViewPdfDetailsService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewPdfDetailsController {

	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	@Autowired
	ViewPdfDetailsService viewPdfDetailsService;
	
	@PostMapping("_save")
	private ResponseEntity<?> saveBuildingMaterial(
			@RequestBody ViewPdfDetailsModel viewPdfDetailsModel) {
		
		bookingsFieldsValidator.validatePdfDetails(viewPdfDetailsModel);
		ViewPdfDetailsModel viewPdfDetails = viewPdfDetailsService
				.save(viewPdfDetailsModel);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(viewPdfDetails);
		
		return ResponseEntity.ok(rs);
	}
	
}

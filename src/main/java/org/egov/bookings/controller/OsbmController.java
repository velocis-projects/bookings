package org.egov.bookings.controller;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.OsbmApproverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OsbmController {

	@Autowired
	private OsbmApproverService osbmApproverService;

	@PostMapping("_create")
	public ResponseEntity<?> createOsbmApprover(@RequestBody OsbmApproverModel osbmApproverModel) {

		OsbmApproverModel osbmModel = osbmApproverService.createOsbmApprover(osbmApproverModel);

		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(osbmModel);
		return ResponseEntity.ok(rs);

	}

}

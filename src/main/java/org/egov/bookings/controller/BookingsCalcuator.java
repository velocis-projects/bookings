package org.egov.bookings.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/demand")
public class BookingsCalcuator {
	

	@Autowired
	BookingsCalculatorService bookingsCalculatorService;
	
	/*@PostMapping("/_create")
	private ResponseEntity<?> createDemand(@RequestHeader HttpHeaders headers, @RequestBody @Valid DemandRequest demandRequest) {
		
		List<Demand> demands = bookingsCalculatorService
				.createDemand(demandRequest);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(demands);
		
		return ResponseEntity.ok(rs);
	}*/
	

}

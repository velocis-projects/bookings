package org.egov.bookings.controller;

import java.util.List;
import java.util.Optional;

import org.egov.bookings.model.OpenSpaceBuildingMaterialBookingsModel;
import org.egov.bookings.service.OpenSpaceBuildingMaterialBookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import org.egov.bookings.common.model.ResponseModel;

@RestController
@RequestMapping("/api")
public class OpenSpaceBuildingMaterialBookingsController {

	@Autowired
	private OpenSpaceBuildingMaterialBookingsService bookingsService;
	
	@Autowired
	private Environment env;

	@PostMapping("/save")
	private ResponseEntity<?> saveBuildingMaterial(
			@RequestBody OpenSpaceBuildingMaterialBookingsModel openSpaceBuildingMaterialBookingsModel) {
		
		OpenSpaceBuildingMaterialBookingsModel bookingsModel = bookingsService
				.save(openSpaceBuildingMaterialBookingsModel);
		ResponseModel rs = new ResponseModel();
		rs.setStatus("200");
		rs.setMessage("Data submitted successfully");
		rs.setData(bookingsModel);
		
		return ResponseEntity.ok(rs);
	}

	@GetMapping("/getAllBuildingMaterial")
	private ResponseEntity<?> getAllBuildingMaterial() {
		List<OpenSpaceBuildingMaterialBookingsModel> bookingsModel = bookingsService.getAllBuildingMaterial();
		
		if (bookingsModel.size() == 0)
			throw new ResourceAccessException(env.getProperty("NOT_FOUND"));
		return ResponseEntity.ok(bookingsModel);
	}
		
	
	@GetMapping("/getBuildingMaterialById/{id}")
	private ResponseEntity<?> getBuildingMaterialById(@PathVariable Long id) {
		OpenSpaceBuildingMaterialBookingsModel bookingsModel = bookingsService.getBuildingMaterialById(id);
		
		Optional.ofNullable(bookingsModel).orElseThrow(() -> new ResourceAccessException(env.getProperty("NOT_FOUND")));
		return ResponseEntity.ok(bookingsModel);
		
	}
	
}

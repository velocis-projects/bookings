package org.egov.bookings.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.egov.bookings.model.OpenSpaceBuildingMaterialBookingsModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.OpenSpaceBuildingMaterialBookingsRepository;
import org.egov.bookings.service.OpenSpaceBuildingMaterialBookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OpenSpaceBuildingMaterialBookingsServiceImpl implements OpenSpaceBuildingMaterialBookingsService {

	@Autowired
	private OpenSpaceBuildingMaterialBookingsRepository bookingsRepository;

	@Autowired
	private BookingsProducer bookingsProducer;

	@Value("${kafka.topics.save.service}")
	private String saveTopic;

	@Override
	public OpenSpaceBuildingMaterialBookingsModel save(OpenSpaceBuildingMaterialBookingsModel bookingsModel) {
		bookingsProducer.push(saveTopic, bookingsModel);
		return bookingsRepository.save(bookingsModel);
	}

	@Override
	public List<OpenSpaceBuildingMaterialBookingsModel> getAllBuildingMaterial() {
		return (List<OpenSpaceBuildingMaterialBookingsModel>) bookingsRepository.findAll();
	}

	@Override
	public OpenSpaceBuildingMaterialBookingsModel getBuildingMaterialById(Long id) {
		return bookingsRepository.findOne(id);
	}

}

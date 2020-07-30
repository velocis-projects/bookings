package org.egov.bookings.service.impl;

import org.egov.bookings.model.ViewPdfDetailsModel;
import org.egov.bookings.repository.ViewPdfDetailsRepository;
import org.egov.bookings.service.ViewPdfDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewPdfDetailsServiceImpl implements ViewPdfDetailsService {

	@Autowired
	ViewPdfDetailsRepository viewPdfDetailsRepository;
	
	@Override
	public ViewPdfDetailsModel save(ViewPdfDetailsModel viewPdfDetailsModel) {
		return viewPdfDetailsRepository.save(viewPdfDetailsModel);
	}

}

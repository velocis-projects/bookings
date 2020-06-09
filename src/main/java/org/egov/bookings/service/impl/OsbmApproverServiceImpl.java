package org.egov.bookings.service.impl;

import javax.transaction.Transactional;

import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.service.OsbmApproverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OsbmApproverServiceImpl implements OsbmApproverService{

	@Autowired
	private OsbmApproverRepository osbmApproverRepository; 
	
	@Override
	public OsbmApproverModel createOsbmApprover(OsbmApproverModel osbmApproverModel) {
		return osbmApproverRepository.save(osbmApproverModel);
	}

}

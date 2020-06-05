package org.egov.bookings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.IdResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.web.models.AuditDetails;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;

@Service
public class EnrichmentService {

	@Autowired
	private BookingsUtils bookingsUtil;

	@Autowired
	private BookingsConfiguration config;
	
	@Autowired
	private IdGenRepository idGenRepository;

	public void enrichTLCreateRequest(BookingsRequest bookingsRequest) {
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		/*AuditDetails auditDetails = bookingsUtil
				.getAuditDetails(requestInfo.getUserInfo().getUuid(), true);*/
		setIdgenIds(bookingsRequest);
		// setStatusForCreate(tradeLicenseRequest);
		// boundaryService.getAreaType(tradeLicenseRequest,config.getHierarchyTypeCode());
	}

	private void setIdgenIds(BookingsRequest bookingsRequest) {
		RequestInfo requestInfo = bookingsRequest.getRequestInfo();
		String tenantId = bookingsRequest.getBookingsModel().getTenantId();

		 BookingsModel bookingsModel = bookingsRequest.getBookingsModel();

		List<String> applicationNumbers = getIdList(requestInfo, tenantId, config.getApplicationNumberIdgenName(),
				config.getApplicationNumberIdgenFormat());
		ListIterator<String> itr = applicationNumbers.listIterator();

		Map<String, String> errorMap = new HashMap<>();
		/*if (applicationNumbers.size() != bookingsRequest.getBookingsModel().size()) {
			errorMap.put("IDGEN ERROR ",
					"The number of LicenseNumber returned by idgen is not equal to number of TradeLicenses");
		}*/

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);

		bookingsModel.setBkApplicationNumber(itr.next());
	}

	private List<String> getIdList(RequestInfo requestInfo, String tenantId, String idKey, String idformat) {
		List<IdResponse> idResponses = idGenRepository.getId(requestInfo, tenantId, idKey, idformat)
				.getIdResponses();

		if (CollectionUtils.isEmpty(idResponses))
			throw new CustomException("IDGEN ERROR", "No ids returned from idgen Service");

		return idResponses.stream().map(IdResponse::getId).collect(Collectors.toList());
	}

}

package org.egov.bookings.service;

import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.web.models.NewLocationRequest;

public interface OsujmNewLocationService {

	public OsujmNewLocationModel addNewLocation(NewLocationRequest newLocationRequest);

	public OsujmNewLocationModel updateNewLocation(NewLocationRequest newLocationRequest);

}

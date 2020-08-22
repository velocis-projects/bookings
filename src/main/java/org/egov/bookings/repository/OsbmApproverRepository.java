package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.OsbmApproverModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsbmApproverRepository extends CrudRepository<OsbmApproverModel, Long>{

	public  List<OsbmApproverModel> findBySector(String sector);
	public OsbmApproverModel findByUuid(String uuid);
	
	
}

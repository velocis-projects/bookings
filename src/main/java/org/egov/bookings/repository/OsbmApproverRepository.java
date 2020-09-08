package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.OsbmApproverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface OsbmApproverRepository.
 */
@Repository
public interface OsbmApproverRepository extends JpaRepository<OsbmApproverModel, Long>{

	/**
	 * Find by sector.
	 *
	 * @param sector the sector
	 * @return the list
	 */
	public  List<OsbmApproverModel> findBySector(String sector);
	
	/**
	 * Find by uuid.
	 *
	 * @param uuid the uuid
	 * @return the osbm approver model
	 */
	public OsbmApproverModel findByUuid(String uuid);

	/**
	 * Find by uuid and sector.
	 *
	 * @param uuid the uuid
	 * @param sector the sector
	 * @return the osbm approver model
	 */
	public OsbmApproverModel findByUuidAndSector(String uuid, String sector);
	
	
}

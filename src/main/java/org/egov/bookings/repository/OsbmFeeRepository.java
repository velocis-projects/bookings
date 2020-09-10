package org.egov.bookings.repository;

import org.egov.bookings.model.OsbmFeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsbmFeeRepository extends JpaRepository<OsbmFeeModel, String> {

	/**
	 * Find by village city and residential commercial and storage and duration in months and construction type.
	 *
	 * @param villageCity the village city
	 * @param residentialCommercial the residential commercial
	 * @param storage the storage
	 * @param durationInMonths the duration in months
	 * @param constructionType the construction type
	 * @return the osbm fee model
	 */
	OsbmFeeModel findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(
			String villageCity, String residentialCommercial, String storage, String durationInMonths,
			String constructionType);

}

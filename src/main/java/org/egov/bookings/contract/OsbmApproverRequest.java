package org.egov.bookings.contract;

import java.io.Serializable;

import javax.validation.Valid;

import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsujmNewLocationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OsbmApproverRequest implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The sector. */
	private String sector;

	/** The uuid. */
	private String uuid;
	
	/** The osbm approver model. */
	@JsonProperty("OsbmApproverDetails")
	@Valid
	private OsbmApproverModel osbmApproverModel = null;
}

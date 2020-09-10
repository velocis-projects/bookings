package org.egov.bookings.contract;

import java.io.Serializable;
import java.util.List;

import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7816338338999208980L;
	
	/** The approver list. */
	@JsonProperty("ApproverDetails")
	private List<OsbmApproverModel> approverList;
	
	/** The osbm fee list. */
	@JsonProperty("OsbmFeeDetails")
	private List<OsbmFeeModel> osbmFeeList;
	
	/** The osujm fee list. */
	@JsonProperty("OsujmFeeDetails")
	private List<OsujmFeeModel> osujmFeeList;
	
}

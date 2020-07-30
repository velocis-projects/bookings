package org.egov.bookings.contract;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new osbm approver request.
 *
 * @param sector the sector
 * @param uuid the uuid
 */
@AllArgsConstructor

/**
 * Instantiates a new osbm approver request.
 */
@NoArgsConstructor
public class OsbmApproverRequest implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6489944586553914355L;

	/** The sector. */
	private String sector;

	/** The uuid. */
	private String uuid;
}

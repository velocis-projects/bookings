package org.egov.bookings.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity(name = "BookingsRemarks")
@Table(name = "TL_BOOKINGS_REMARKS")
public class BookingsRemarks {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "BK_REMARKS_ID")
	private Long bkRemarksId;

	@Column(name = "BK_REMARKS")
	private String bkRemarks;

	@Column(name = "BK_CREATED_BY")
	private String bkCreatedBy;

	@Column(name = "BK_CREATED_ON")
	private Date bkCreatedOn;

}

package org.egov.bookings.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity(name = "ViewPdfDetailsModel")
@Table(name = "TT_VIEW_PDF_DETAILS")
public class ViewPdfDetailsModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "BK_APPLICATION_NUMBER")
	private String bkApplicationNumber;
	
	@Column(name = "FILE_STORE_ID")
	private String fileStoreId;
	
	@Column(name = "KEY")
	private String key;

}

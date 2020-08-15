package org.egov.bookings.contract;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;

// TODO: Auto-generated Javadoc
/**
 * The Class Booking.
 */
public class Booking implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3683257254333784296L;
	
	/** The booking model list. */
	private List< BookingsModel > bookingsModelList;
	
	/** The bookings model set. */
	private Set< BookingsModel > bookingsModelSet;
	
	/** The bookings count. */
	private int bookingsCount;
	
	/** The document map. */
	private Map< String, String > documentMap;
	
	/** The business service. */
	private String businessService;
	
	/** The osujm new location model list. */
	private List< OsujmNewLocationModel > osujmNewLocationModelList;
	
	/** The osujm new location model set. */
	private Set< OsujmNewLocationModel > osujmNewLocationModelSet;
	
	/** The osujm newlocation map. */
	private Map< String, List< MdmsJsonFields > > osujmNewlocationMap;
	
	/**
	 * Gets the bookings model list.
	 *
	 * @return the bookings model list
	 */
	public List<BookingsModel> getBookingsModelList() {
		return bookingsModelList;
	}

	/**
	 * Sets the bookings model list.
	 *
	 * @param bookingsModelList the new bookings model list
	 */
	public void setBookingsModelList(List<BookingsModel> bookingsModelList) {
		this.bookingsModelList = bookingsModelList;
	}

		/**
	 * Gets the bookings model set.
	 *
	 * @return the bookings model set
	 */
	public Set< BookingsModel > getBookingsModelSet() {
		return bookingsModelSet;
	}

	/**
	 * Sets the bookings model set.
	 *
	 * @param bookingsModelSet the new bookings model set
	 */
	public void setBookingsModelSet(Set< BookingsModel > bookingsModelSet) {
		this.bookingsModelSet = bookingsModelSet;
	}

	/**
	 * Gets the bookings count.
	 *
	 * @return the bookings count
	 */
	public int getBookingsCount() {
		return bookingsCount;
	}
	
	/**
	 * Sets the bookings count.
	 *
	 * @param bookingsCount the new bookings count
	 */
	public void setBookingsCount(int bookingsCount) {
		this.bookingsCount = bookingsCount;
	}

	/**
	 * Gets the document map.
	 *
	 * @return the document map
	 */
	public Map< String, String > getDocumentMap() {
		return documentMap;
	}

	/**
	 * Sets the document map.
	 *
	 * @param documentMap the document map
	 */
	public void setDocumentMap(Map< String, String > documentMap) {
		this.documentMap = documentMap;
	}

	/**
	 * Gets the business service.
	 *
	 * @return the business service
	 */
	public String getBusinessService() {
		return businessService;
	}

	/**
	 * Sets the business service.
	 *
	 * @param businessService the new business service
	 */
	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}

	/**
	 * Gets the osujm new location model list.
	 *
	 * @return the osujm new location model list
	 */
	public List< OsujmNewLocationModel > getOsujmNewLocationModelList() {
		return osujmNewLocationModelList;
	}

	/**
	 * Sets the osujm new location model list.
	 *
	 * @param osujmNewLocationModelList the new osujm new location model list
	 */
	public void setOsujmNewLocationModelList(List< OsujmNewLocationModel > osujmNewLocationModelList) {
		this.osujmNewLocationModelList = osujmNewLocationModelList;
	}

	/**
	 * Gets the osujm newlocation map.
	 *
	 * @return the osujm newlocation map
	 */
	public Map< String, List< MdmsJsonFields > > getOsujmNewlocationMap() {
		return osujmNewlocationMap;
	}

	/**
	 * Sets the osujm newlocation map.
	 *
	 * @param osujmNewlocationMap the osujm newlocation map
	 */
	public void setOsujmNewlocationMap(Map< String, List< MdmsJsonFields > > osujmNewlocationMap) {
		this.osujmNewlocationMap = osujmNewlocationMap;
	}

	/**
	 * Gets the osujm new location model set.
	 *
	 * @return the osujm new location model set
	 */
	public Set< OsujmNewLocationModel > getOsujmNewLocationModelSet() {
		return osujmNewLocationModelSet;
	}

	/**
	 * Sets the osujm new location model set.
	 *
	 * @param osujmNewLocationModelSet the new osujm new location model set
	 */
	public void setOsujmNewLocationModelSet(Set< OsujmNewLocationModel > osujmNewLocationModelSet) {
		this.osujmNewLocationModelSet = osujmNewLocationModelSet;
	}


}

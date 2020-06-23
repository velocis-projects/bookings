package org.egov.bookings.enums;

public enum BookingTypeEnum 
{
	
	OPEN_SPACE_FOR_BUILDING_METERIAL(1, "OSBM"),
	WATER_TANKER(2, "BWT"),
	PARKS_AND_COMMUNITY_CENTER(3, "PACC"),
	GROUND_FOR_COMMERCIAL_PURPOSE(4, "GFCP"),
	OPEN_SPACE_UNDER_JURIDICTION_OF_MCC(5, "OSUJM");
	
	private int id;
	private String name;
	
	private BookingTypeEnum( int id, String name )
	{
		this.id = id;
		this.name = name;
	}
	
	
	public int getId() {
		return id;
	}

	public String getName()
	{
		return name;
	}
	
//	public static BookingTypeEnum getByName( String name )
//	{
//		for (BookingTypeEnum objectType : BookingTypeEnum.values()) {
//			if (objectType.getName() == name) {
//				return objectType;
//			}
//		}
//		return BookingTypeEnum.OPEN_SPACE_FOR_BUILDING_METERIAL;
//	}

}

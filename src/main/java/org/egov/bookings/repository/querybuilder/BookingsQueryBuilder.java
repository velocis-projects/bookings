package org.egov.bookings.repository.querybuilder;

import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsQueryBuilder.
 */
@Component
public class BookingsQueryBuilder {

	/** The Constant FIND_ASSIGNEE_UUID. */
	public static final String FIND_ASSIGNEE_UUID = "select uuid from eg_user eu where eu.id = "
			+ "(select euv.user_id from eg_userrole_v1 euv where role_code in "
			+ "(select  roles from eg_wf_action_v2 where currentstate = "
			+ "(select action_v2.nextstate from public.eg_wf_businessservice_v2 business ,"
			+ " public.eg_wf_state_v2 state , public.eg_wf_action_v2 action_v2 where"
			+ " business .uuid  = state.businessserviceid and state .uuid  = action_v2.currentstate"
			+ "  and business .businessservice =:businessservice and business.tenantid=:tenantId and "
			+ "action_v2.\"action\" = :action) and euv.role_tenantid = :role_tenantId));";

	/** The Constant FIND_APPLICATION_NUMBER. */
	public static final String FIND_APPLICATION_NUMBER = "select businessid from eg_wf_processinstance_v2 as ewpv "
			+ "inner join eg_wf_state_v2 as ewsv on ewsv.uuid = ewpv.status " 
			+ "inner join eg_wf_action_v2 as ewav on ewav.currentstate = ewsv.uuid or ewav.nextstate = ewsv.uuid "
			+ "and ewav.roles = :roles";
	//where ewpv.action != 'INITIATE'
	
	/** The Constant FIND_SECTOR_LIST. */
	public static final String FIND_SECTOR_LIST = "select sector from tm_osbm_approver where uuid = :uuid";
	
	/** The Constant FIND_DOCUMENT_LIST. */
	public static final String FIND_DOCUMENT_LIST = "select distinct ef.filestoreid, ef.filename from eg_filestoremap as ef"
			+ " inner join eg_wf_document_v2 as ewd on ewd.filestoreid = ef.filestoreid"
			+ " inner join eg_wf_processinstance_v2 as ewp on ewp.id = ewd.processinstanceid and ewp.businessid = :applicationNumber";
	
	/** The Constant FIND_BUSINESS_SERVICE. */
	public static final String FIND_BUSINESS_SERVICE = "select businessservice from eg_wf_processinstance_v2 "
			+ " where businessid = :applicationNumber";
	
	/** The Constant CHECK_COMMERCIAL_GROUND_AVAILABILITY. */
	public static final String CHECK_COMMERCIAL_GROUND_AVAILABILITY = "select * from tt_bookings where bk_booking_venue =:bookingVenue and bk_Booking_Type =:bookingType  and bk_to_date >= :date and bk_action =:APPLY";
	
	/** The Constant FIND_JURISDICTION_AMOUNT. */
	public static final String FIND_JURISDICTION_AMOUNT = "select * from tm_osujm_fee where area_from <= :area and area_to >= :area and sector = :sector";
	
	/** The Constant CHECK_JURISDICTION_AVAILABILITY. */
	public static final String CHECK_JURISDICTION_AVAILABILITY = "select * from tt_bookings where bk_booking_venue =:bookingVenue and bk_Booking_Type =:bookingType  and bk_sector = :bkSector  and bk_to_date >= :date and bk_action =:PAY";
	/** The Constant FIND_DOCUMENTS. */
	public static final String FIND_DOCUMENTS = "select distinct ef.filestoreid, ef.filename, ewd.documenttype from eg_filestoremap as ef"
			+ " inner join eg_wf_document_v2 as ewd on ewd.filestoreid = ef.filestoreid"
			+ " inner join eg_wf_processinstance_v2 as ewp on ewp.id = ewd.processinstanceid and ewp.businessid = :applicationNumber";

	
	/** The Constant FIND_NEXT_STATE. */
	public static final String FIND_NEXT_STATE = "select ewav.nextstate from eg_wf_action_v2 as ewav"
			+ " inner join eg_wf_state_v2 as ewsv on ewsv.uuid = ewav.currentstate"
			+ " inner join eg_wf_processinstance_v2 as ewpv on ewpv.status = ewsv.uuid and ewpv.businessid = :applicationNumber and ewpv.action = :action";
	
	/** The Constant FIND_APPROVER_NAME. */
	public static final String FIND_APPROVER_NAME = "select roles from eg_wf_action_v2 where currentstate = :state";
	
	/** The Constant FIND_USER_ID. */
	public static final String FIND_USER_ID = "select user_id from eg_userrole_v1  where role_code = :approver";
	
	/** The Constant FIND_USER_LIST. */
	public static final String FIND_USER_LIST = "select uuid, username from eg_user  where id in (:userId)";

	public static final String CHECK_PARK_AND_COMMUNITY_AVAILABILITY = "select * from tt_bookings where bk_booking_venue =:bookingVenue and bk_Booking_Type =:bookingType and bk_sector =:sector and bk_to_date >= :date and bk_action =:APPLY";
}

package org.egov.bookings.repository.querybuilder;

import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class WorkflowQueryBuilder.
 */
@Component
public class WorkflowQueryBuilder {

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
	public static final String FIND_APPLICATION_NUMBER = "select businessid from eg_wf_processinstance_v2 where assignee = :uuid";
	
}
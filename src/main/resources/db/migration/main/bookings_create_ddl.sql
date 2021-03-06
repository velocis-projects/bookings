CREATE TABLE public.tm_commercial_ground_fee (
	id int8 NOT NULL,
	category varchar(255) NULL,
	locality varchar(255) NULL,
	rate_per_day int8 NULL,
	booking_venue varchar(255) NULL,
	CONSTRAINT tt_commercial_ground_fee_pkey PRIMARY KEY (id)
);



CREATE TABLE public.tm_osbm_approver (
	id int8 NOT NULL,
	sector varchar(255) NULL,
	uuid varchar(255) NULL,
	CONSTRAINT tt_osbm_approver_pkey PRIMARY KEY (id)
);


CREATE TABLE public.tm_osbm_fee (
	id int8 NOT NULL,
	amount int8 NULL,
	construction_type varchar(255) NULL,
	duration_in_months varchar(255) NULL,
	residential_commercial varchar(255) NULL,
	"storage" varchar(255) NULL,
	village_city varchar(255) NULL,
	CONSTRAINT tt_osbm_fee_pkey PRIMARY KEY (id)
);


CREATE TABLE public.tm_osujm_fee (
	id int8 NOT NULL,
	area_from int8 NULL,
	area_to int8 NULL,
	rate_per_sqr_feet_per_day int8 NULL,
	sector varchar(255) NULL,
	slab varchar(255) NULL,
	CONSTRAINT tt_osujm_fee_pkey PRIMARY KEY (id)
);


CREATE TABLE public.tt_osujm_new_location (
	application_number varchar(255) NOT NULL,
	"action" varchar(255) NULL,
	applicant_address varchar(255) NULL,
	applicant_name varchar(255) NULL,
	application_status varchar(255) NULL,
	area_requirement varchar(255) NULL,
	business_service varchar(255) NULL,
	contact varchar(255) NULL,
	date_created date NULL,
	id_proof varchar(255) NULL,
	landmark varchar(255) NULL,
	locality_address varchar(255) NULL,
	"location" varchar(255) NULL,
	mail_address varchar(255) NULL,
	sector varchar(255) NULL,
	tenant_id varchar(255) NULL,
	uuid varchar(255) NULL,
	CONSTRAINT tt_osujm_new_location_pkey PRIMARY KEY (application_number)
);



CREATE TABLE public.tt_bookings (
	bk_application_number varchar(255) NOT NULL,
	bk_account_type varchar(255) NULL,
	bk_action varchar(255) NULL,
	bk_actual_delivery_time varchar(255) NULL,
	bk_add_special_request_details varchar(255) NULL,
	bk_address varchar(255) NULL,
	bk_amount varchar(255) NULL,
	bk_applicant_contact varchar(255) NULL,
	bk_applicant_name varchar(255) NULL,
	bk_application_status varchar(255) NULL,
	bk_approved_by varchar(255) NULL,
	bk_area_required varchar(255) NULL,
	bk_bank_account_number varchar(255) NULL,
	bk_bank_name varchar(255) NULL,
	bk_booking_duration varchar(255) NULL,
	bk_booking_purpose varchar(255) NULL,
	bk_booking_reference_number varchar(255) NULL,
	bk_booking_time varchar(255) NULL,
	bk_booking_type varchar(255) NULL,
	bk_booking_venue varchar(255) NULL,
	bk_category varchar(255) NULL,
	bk_cgst varchar(255) NULL,
	bk_cleansing_charges varchar(255) NULL,
	bk_complete_address varchar(255) NULL,
	bk_construction_type varchar(255) NULL,
	bk_contact_no varchar(255) NULL,
	bk_created_by int8 NULL,
	bk_current_charges varchar(255) NULL,
	bk_customer_gst_no varchar(255) NULL,
	bk_date date NULL,
	bk_date_created date NULL,
	bk_dimension varchar(255) NULL,
	bk_document_uploaded_url varchar(255) NULL,
	bk_driver_name varchar(255) NULL,
	bk_duration varchar(255) NULL,
	bk_email varchar(255) NULL,
	bk_ending_date date NULL,
	bk_estimated_delivery_time varchar(255) NULL,
	bk_facilitation_charges varchar(255) NULL,
	bk_father_name varchar(255) NULL,
	bk_from_date date NULL,
	bk_house_no varchar(255) NULL,
	bk_id_proof varchar(255) NULL,
	bk_ifsc_code varchar(255) NULL,
	bk_landmark varchar(255) NULL,
	bk_location varchar(255) NULL,
	bk_location_change_amount varchar(255) NULL,
	bk_location_pictures varchar(255) NULL,
	bk_material_storage_area varchar(255) NULL,
	bk_mobile_number varchar(255) NULL,
	bk_module_type varchar(255) NULL,
	bk_normal_water_failure_request varchar(255) NULL,
	bk_open_space_location varchar(255) NULL,
	bk_park_or_community_center varchar(255) NULL,
	bk_payment_date date NULL,
	bk_payment_receipt_number varchar(255) NULL,
	bk_payment_status varchar(255) NULL,
	bk_plot_sketch varchar(255) NULL,
	bk_property_owner_name varchar(255) NULL,
	bk_refund_amount varchar(255) NULL,
	bk_rent varchar(255) NULL,
	bk_requirement_area varchar(255) NULL,
	bk_residence_proof varchar(255) NULL,
	bk_residential_or_commercial varchar(255) NULL,
	bk_sector varchar(255) NULL,
	bk_starting_date date NULL,
	bk_status varchar(255) NULL,
	bk_status_update_request varchar(255) NULL,
	bk_surcharge_rent varchar(255) NULL,
	bk_time varchar(255) NULL,
	bk_to_date date NULL,
	bk_type varchar(255) NULL,
	bk_update_status_option varchar(255) NULL,
	bk_utgst varchar(255) NULL,
	bk_vehicle_number varchar(255) NULL,
	bk_venue varchar(255) NULL,
	bk_vill_city varchar(255) NULL,
	bk_village varchar(255) NULL,
	bk_wf_status varchar(255) NULL,
	business_service varchar(255) NULL,
	tenant_id varchar(255) NULL,
	uuid varchar(255) NULL,
	approver_name varchar(255) NULL,
	financial_year varchar(64) NULL,
	CONSTRAINT tt_bookings_pkey PRIMARY KEY (bk_application_number)
);




CREATE TABLE public.tl_bookings_remarks (
	bk_remarks_id int8 NOT NULL,
	bk_created_by varchar(255) NULL,
	bk_created_on date NULL,
	bk_remarks varchar(255) NULL,
	bk_application_number varchar(255) NULL,
	application_number varchar(255) NULL,
	CONSTRAINT tl_bookings_remarks_pkey PRIMARY KEY (bk_remarks_id),
	CONSTRAINT fkjit9bp1rxi894cjxveegcywkh FOREIGN KEY (bk_application_number) REFERENCES tt_bookings(bk_application_number),
	CONSTRAINT fkp4h75nywyynmqgo70ex11itvn FOREIGN KEY (application_number) REFERENCES tt_osujm_new_location(application_number)
);


CREATE TABLE public.tt_commercial_ground_availability_lock (
	id int8 NOT NULL,
	booking_venue varchar(255) NULL,
	from_date date NULL,
	islocked bool NULL,
	to_date date NULL,
	CONSTRAINT tt_commercial_ground_availability_pkey PRIMARY KEY (id)
);
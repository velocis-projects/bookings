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
	approver_name varchar(255) NULL,
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
	bk_remarks varchar(255) NULL,
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
	financial_year varchar(64) NULL,
	tenant_id varchar(255) NULL,
	uuid varchar(255) NULL,
	CONSTRAINT tt_bookings_pkey PRIMARY KEY (bk_application_number)
);




INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(68, 4000, 'Renovation/Alteration/Addition', '4', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(70, 5000, 'Renovation/Alteration/Addition', '5', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(72, 6000, 'Renovation/Alteration/Addition', '6', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(85, 15000, 'New', '1', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(87, 15000, 'New', '2', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(89, 15000, 'New', '3', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(91, 15000, 'New', '4', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(93, 15000, 'New', '5', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(95, 15000, 'New', '6', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(86, 3000, 'Renovation/Alteration/Addition', '1', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(88, 6000, 'Renovation/Alteration/Addition', '2', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(90, 9000, 'Renovation/Alteration/Addition', '3', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(92, 12000, 'Renovation/Alteration/Addition', '4', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(94, 15000, 'Renovation/Alteration/Addition', '5', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(96, 18000, 'Renovation/Alteration/Addition', '6', 'Commercial', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(49, 4000, 'New', '1', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(51, 2000, 'New', '2', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(53, 2000, 'New', '3', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(55, 2000, 'New', '4', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(57, 2000, 'New', '5', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(59, 2000, 'New', '6', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(50, 1000, 'Renovation/Alteration/Addition', '1', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(52, 2000, 'Renovation/Alteration/Addition', '2', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(54, 3000, 'Renovation/Alteration/Addition', '3', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(56, 4000, 'Renovation/Alteration/Addition', '4', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(58, 5000, 'Renovation/Alteration/Addition', '5', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(60, 6000, 'Renovation/Alteration/Addition', '6', 'Residential', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(73, 8000, 'New', '1', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(75, 8000, 'New', '2', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(77, 8000, 'New', '3', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(79, 8000, 'New', '4', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(81, 8000, 'New', '5', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(83, 8000, 'New', '6', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(74, 2000, 'Renovation/Alteration/Addition', '1', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(76, 4000, 'Renovation/Alteration/Addition', '2', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(78, 6000, 'Renovation/Alteration/Addition', '3', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(80, 8000, 'Renovation/Alteration/Addition', '4', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(82, 10000, 'Renovation/Alteration/Addition', '5', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(84, 12000, 'Renovation/Alteration/Addition', '6', 'Commercial', 'Less than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(61, 8000, 'New', '1', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(63, 4000, 'New', '2', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(65, 4000, 'New', '3', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(67, 4000, 'New', '4', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(69, 4000, 'New', '5', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(71, 4000, 'New', '6', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(62, 1000, 'Renovation/Alteration/Addition', '1', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(37, 15000, 'New', '1', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(39, 15000, 'New', '2', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(41, 15000, 'New', '3', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(43, 15000, 'New', '4', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(45, 15000, 'New', '5', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(47, 15000, 'New', '6', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(38, 3000, 'Renovation/Alteration/Addition', '1', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(40, 6000, 'Renovation/Alteration/Addition', '2', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(42, 9000, 'Renovation/Alteration/Addition', '3', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(44, 12000, 'Renovation/Alteration/Addition', '4', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(46, 15000, 'Renovation/Alteration/Addition', '5', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(48, 18000, 'Renovation/Alteration/Addition', '6', 'Commercial', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(8, 2400, 'Renovation/Alteration/Addition', '4', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(10, 3000, 'Renovation/Alteration/Addition', '5', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(12, 3600, 'Renovation/Alteration/Addition', '6', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(25, 8000, 'New', '1', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(27, 8000, 'New', '2', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(29, 8000, 'New', '3', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(31, 8000, 'New', '4', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(33, 8000, 'New', '5', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(35, 8000, 'New', '6', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(26, 2000, 'Renovation/Alteration/Addition', '1', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(28, 4000, 'Renovation/Alteration/Addition', '2', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(30, 6000, 'Renovation/Alteration/Addition', '3', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(32, 8000, 'Renovation/Alteration/Addition', '4', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(34, 10000, 'Renovation/Alteration/Addition', '5', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(36, 12000, 'Renovation/Alteration/Addition', '6', 'Commercial', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(13, 4000, 'New', '1', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(15, 4000, 'New', '2', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(17, 4000, 'New', '3', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(19, 4000, 'New', '4', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(21, 4000, 'New', '5', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(23, 4000, 'New', '6', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(14, 1200, 'Renovation/Alteration/Addition', '1', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(16, 2400, 'Renovation/Alteration/Addition', '2', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(18, 3600, 'Renovation/Alteration/Addition', '3', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(20, 4800, 'Renovation/Alteration/Addition', '4', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(22, 6000, 'Renovation/Alteration/Addition', '5', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(24, 7200, 'Renovation/Alteration/Addition', '6', 'Residential', 'More than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(64, 2000, 'Renovation/Alteration/Addition', '2', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(66, 3000, 'Renovation/Alteration/Addition', '3', 'Residential', 'More than 1000 sqft', 'City');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(1, 2000, 'New', '1', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(3, 2000, 'New', '2', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(5, 2000, 'New', '3', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(7, 2000, 'New', '4', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(9, 2000, 'New', '5', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(11, 2000, 'New', '6', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(2, 600, 'Renovation/Alteration/Addition', '1', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(4, 1200, 'Renovation/Alteration/Addition', '2', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');
INSERT INTO public.tm_osbm_fee
(id, amount, construction_type, duration_in_months, residential_commercial, "storage", village_city)
VALUES(6, 1800, 'Renovation/Alteration/Addition', '3', 'Residential', 'Less than 1000 sqft', 'Village/Re-habilitation');




INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(4, 0, 225, 40, 'OTHER', '1st');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(5, 226, 450, 30, 'OTHER', '2nd');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(1, 0, 225, 80, 'SECTOR-17', '1st');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(2, 226, 450, 60, 'SECTOR-17', '2nd');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(7, 0, 225, 80, 'SECTOR-22', '1st');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(8, 226, 450, 60, 'SECTOR-22', '2nd');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(6, 451, 1000, 15, 'OTHER', '3rd');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(3, 451, 1000, 30, 'SECTOR-17', '3rd');
INSERT INTO public.tm_osujm_fee
(id, area_from, area_to, rate_per_sqr_feet_per_day, sector, slab)
VALUES(9, 451, 1000, 30, 'SECTOR-22', '3rd');









CREATE TABLE public.tt_commercial_ground_availability_lock (
	id int8 NOT NULL,
	booking_venue varchar(255) NULL,
	from_date date NULL,
	islocked bool NULL,
	to_date date NULL,
	CONSTRAINT tt_commercial_ground_availability_pkey PRIMARY KEY (id)
);
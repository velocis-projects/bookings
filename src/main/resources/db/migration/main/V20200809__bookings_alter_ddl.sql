ALTER TABLE public.tm_osbm_fee ALTER COLUMN id TYPE varchar USING id::varchar;

ALTER TABLE public.tm_osbm_approver ALTER COLUMN id TYPE varchar USING id::varchar;

ALTER TABLE public.tm_commercial_ground_fee ALTER COLUMN id TYPE varchar USING id::varchar;

ALTER TABLE public.tm_osujm_fee ALTER COLUMN id TYPE varchar USING id::varchar;

ALTER TABLE public.tt_commercial_ground_availability_lock ALTER COLUMN id TYPE varchar USING id::varchar;
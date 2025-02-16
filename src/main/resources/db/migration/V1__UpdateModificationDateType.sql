ALTER TABLE public.diagrams ALTER COLUMN modification_date TYPE varchar USING modification_date::varchar;

ALTER TABLE public.diagrams ALTER COLUMN modification_date TYPE timestamp USING modification_date::timestamp;

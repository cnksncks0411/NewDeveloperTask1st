CREATE DATABASE test
  WITH OWNER = test1
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Korean_Korea.949'
       LC_CTYPE = 'Korean_Korea.949'
       CONNECTION LIMIT = -1;

CREATE TABLE public."USER"
(
  id character varying(16) NOT NULL,
  pwd character varying(32) NOT NULL,
  name character varying(128) NOT NULL,
  level character(1) NOT NULL,
  "desc" character varying(256),
  reg_date timestamp with time zone NOT NULL,
  CONSTRAINT "USER_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."USER"
  OWNER TO test1;
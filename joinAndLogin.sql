CREATE USER test1
 PASSWORD 'pass'
 CREATEDB LOGIN;

CREATE DATABASE test
  WITH OWNER = test1
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Korean_Korea.949'
       LC_CTYPE = 'Korean_Korea.949'
       CONNECTION LIMIT = -1;

CREATE TABLE public."USER_INFO"
(
  id character varying(16) NOT NULL,
  pw character varying(15),
  name character varying(20),
  phone character varying(11),
  email character varying(100),
  addr character varying(256),
  CONSTRAINT "USER_INFO_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."USER_INFO"
  OWNER TO test1;
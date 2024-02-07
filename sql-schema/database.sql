--
-- TABLE BID
--
CREATE TABLE bid (
	id SERIAL NOT NULL,
	bid double precision NOT NULL,
	id_product integer NOT NULL,
	id_buyer integer NOT NULL,
	id_seller integer NOT NULL,
	message character varying(250),
	date_create timestamp with time zone NOT NULL,
	date_update timestamp with time zone,
	active boolean NOT NULL,
	CONSTRAINT pk_bid_id PRIMARY KEY (id)
);
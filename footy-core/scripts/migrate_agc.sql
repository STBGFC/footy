-- Table: age_group

-- DROP TABLE age_group;

BEGIN TRANSACTION;

CREATE TABLE age_group
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  coordinator_id bigint,
  under boolean NOT NULL,
  year integer NOT NULL,
  CONSTRAINT age_group_pkey PRIMARY KEY (id),
  CONSTRAINT fk8059d73f9f27e948 FOREIGN KEY (coordinator_id)
      REFERENCES person (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT age_group_under_year_key UNIQUE (under, year)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE age_group OWNER TO stbgfc_admin;


-- team table changes
ALTER TABLE team DROP CONSTRAINT team_club_id_key;

ALTER TABLE team ADD COLUMN age_group_id bigint;
ALTER TABLE team ADD 
CONSTRAINT fk36425dad26a204 FOREIGN KEY (age_group_id)
      REFERENCES age_group (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE UNIQUE INDEX team_club_id_key ON team ( club_id, age_group_id, name );

-- migrate data
insert into age_group(id, version, coordinator_id, under, year) values(6, 0, null, true, 6);
insert into age_group(id, version, coordinator_id, under, year) values(7, 0, null, true, 7);
insert into age_group(id, version, coordinator_id, under, year) values(8, 0, null, true, 8);
insert into age_group(id, version, coordinator_id, under, year) values(9, 0, null, true, 9);
insert into age_group(id, version, coordinator_id, under, year) values(10, 0, null, true, 10);
insert into age_group(id, version, coordinator_id, under, year) values(11, 0, null, true, 11);
insert into age_group(id, version, coordinator_id, under, year) values(12, 0, null, true, 12);
insert into age_group(id, version, coordinator_id, under, year) values(13, 0, null, true, 13);
insert into age_group(id, version, coordinator_id, under, year) values(14, 0, null, true, 14);
insert into age_group(id, version, coordinator_id, under, year) values(15, 0, null, true, 15);
insert into age_group(id, version, coordinator_id, under, year) values(16, 0, null, true, 16);
insert into age_group(id, version, coordinator_id, under, year) values(17, 0, null, true, 17);
insert into age_group(id, version, coordinator_id, under, year) values(18, 0, null, true, 18);
insert into age_group(id, version, coordinator_id, under, year) values(21, 0, null, true, 21);
insert into age_group(id, version, coordinator_id, under, year) values(35, 0, null, false, 35);

update team set age_group_id = age_band;

alter table team drop column age_band;
alter table team drop column vets_team;

COMMIT;

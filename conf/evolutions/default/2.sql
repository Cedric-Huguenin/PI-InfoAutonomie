# --- !Ups

alter table time_interval ALTER COLUMN timestamp_start integer;
alter table time_interval ALTER COLUMN timestamp_start rename to begin_hour;
alter table time_interval ADD begin_minutes integer;

alter table time_interval ALTER COLUMN timestamp_end integer;
alter table time_interval ALTER COLUMN timestamp_end rename to end_hour;
alter table time_interval ADD end_minutes integer;


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

alter table time_interval ALTER COLUMN begin_hour rename to timestamp_start;
alter table time_interval ALTER COLUMN end_hour rename to timestamp_end;
alter table time_interval ALTER COLUMN timestamp_start bigint;
alter table time_interval ALTER COLUMN timestamp_end bigint;
alter table time_interval DROP COLUMN begin_minutes;
alter table time_interval DROP COLUMN end_minutes;

SET REFERENTIAL_INTEGRITY TRUE;

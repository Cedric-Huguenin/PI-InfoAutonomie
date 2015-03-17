# --- !Ups

alter table event ADD COLUMN begin_time timestamp;
alter table event ADD COLUMN end_time timestamp;

alter table event drop constraint fk_event_timeInterval_4 ;
drop table if exists time_interval;
drop sequence if exists time_interval_seq;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

alter table event DROP COLUMN begin_time;
alter table event DROP COLUMN end_time;

create table time_interval (
  id                        varchar(255) not null,
  timestamp_start           bigint,
  timestamp_end             bigint,
  constraint pk_time_interval primary key (id))
;

create sequence time_interval_seq;

alter table event add constraint fk_event_timeInterval_4 foreign key (time_interval_id) references time_interval (id) on delete restrict on update restrict;
create index ix_event_timeInterval_4 on event (time_interval_id);

SET REFERENTIAL_INTEGRITY TRUE;

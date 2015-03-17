# --- !Ups

alter table basic_event drop constraint fk_basic_event_detectionMethod_2;
drop table if exists detection;

alter table basic_event DROP COLUMN detection_method_id;
alter table basic_event ADD COLUMN detection_type integer;
alter table basic_event ADD COLUMN simple_threshold double;
alter table basic_event ADD COLUMN min_value double;
alter table basic_event ADD COLUMN max_value double;
alter table basic_event ADD COLUMN delta double;

alter table basic_event ADD constraint ck_basic check (detection_type in (0,1,2));

# --- !Downs

create table detection (
  id                        varchar(255) not null,
  detection_type            integer,
  simple_threshold          double,
  min_value                 double,
  max_value                 double,
  delta                     double,
  constraint ck_detection_detection_type check (detection_type in (0,1,2)),
  constraint pk_detection primary key (id))
;

alter table basic_event DROP constraint ck_basic;

alter table basic_event ADD COLUMN detection_method_id varchar(255);
alter table basic_event DROP COLUMN detection_type;
alter table basic_event DROP COLUMN simple_threshold;
alter table basic_event DROP COLUMN min_value;
alter table basic_event DROP COLUMN max_value;
alter table basic_event DROP COLUMN delta;

alter table basic_event ADD COLUMN detection_method_id varchar(255);

alter table basic_event add constraint fk_basic_event_detectionMethod_2 foreign key (detection_method_id) references detection (id) on delete restrict on update restrict;
create index ix_basic_event_detectionMethod_2 on basic_event (detection_method_id);
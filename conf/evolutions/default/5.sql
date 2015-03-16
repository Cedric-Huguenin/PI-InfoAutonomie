# --- !Ups

alter table alert ADD COLUMN start_basic_event_id varchar(255);
alter table alert ADD COLUMN start_event_id varchar(255);
alter table alert_occurrence ADD COLUMN seen boolean;

alter table alert add constraint fk_alert_startBasicEvent_1 foreign key (start_basic_event_id) references basic_event (id) on delete restrict on update restrict;
create index ix_alert_startBasicEvent_1 on alert (start_basic_event_id);
alter table alert add constraint fk_alert_startEvent_2 foreign key (start_event_id) references event (id) on delete restrict on update restrict;
create index ix_alert_startEvent_2 on alert (start_event_id);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

ALTER TABLE alert DROP CONSTRAINT fk_alert_startBasicEvent_1;

ALTER TABLE alert DROP CONSTRAINT fk_alert_startEvent_2;


alter table alert DROP COLUMN start_basic_event_id;
alter table alert DROP COLUMN start_event_id;
alter table alert_occurrence DROP COLUMN seen;

SET REFERENTIAL_INTEGRITY TRUE;

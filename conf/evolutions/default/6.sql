# --- !Ups

ALTER TABLE alert DROP CONSTRAINT fk_alert_startBasicEvent_1;
alter table alert add constraint fk_alert_startBasicEvent_1 foreign key (start_basic_event_id) references basic_event (id) on delete CASCADE on update CASCADE;

ALTER TABLE alert DROP CONSTRAINT fk_alert_startEvent_2;
alter table alert add constraint fk_alert_startEvent_2 foreign key (start_event_id) references event (id) on delete CASCADE on update CASCADE;

ALTER TABLE alert_occurrence DROP CONSTRAINT fk_alert_occurrence_alert;
alter table alert_occurrence add constraint fk_alert_occurrence_alert foreign key (alert_id) references alert (id) on delete CASCADE on update CASCADE;

ALTER TABLE basic_event DROP CONSTRAINT fk_basic_event_sensor_1;
alter table basic_event add constraint fk_basic_event_sensor_1 foreign key (sensor_id) references sensor (id) on delete CASCADE on update CASCADE;

ALTER TABLE basic_event DROP CONSTRAINT fk_basic_event_detectionMethod_2;
alter table basic_event add constraint fk_basic_event_detectionMethod_2 foreign key (detection_method_id) references detection (id) on delete CASCADE on update CASCADE;

ALTER TABLE basic_event_occurrence DROP CONSTRAINT fk_basic_event_occurrence_basi_3;
alter table basic_event_occurrence add constraint fk_basic_event_occurrence_basi_3 foreign key (basic_event_id) references basic_event (id) on delete CASCADE on update CASCADE;

ALTER TABLE event_occurrence DROP CONSTRAINT fk_event_occurrence_event_5;
alter table event_occurrence add constraint fk_event_occurrence_event_5 foreign key (event_id) references event (id) on delete CASCADE on update CASCADE;


# --- !Downs

ALTER TABLE alert DROP CONSTRAINT fk_alert_startBasicEvent_1;
alter table alert add constraint fk_alert_startBasicEvent_1 foreign key (start_basic_event_id) references basic_event (id) on delete restrict on update restrict;

ALTER TABLE alert DROP CONSTRAINT fk_alert_startEvent_2;
alter table alert add constraint fk_alert_startEvent_2 foreign key (start_event_id) references event (id) on delete restrict on update restrict;

ALTER TABLE alert_occurrence DROP CONSTRAINT fk_alert_occurrence_alert;
alter table alert_occurrence add constraint fk_alert_occurrence_alert foreign key (alert_id) references alert (id) on delete restrict on update restrict;

ALTER TABLE basic_event DROP CONSTRAINT fk_basic_event_sensor_1;
alter table basic_event add constraint fk_basic_event_sensor_1 foreign key (sensor_id) references sensor (id) on delete restrict on update restrict;

ALTER TABLE basic_event DROP CONSTRAINT fk_basic_event_detectionMethod_2;
alter table basic_event add constraint fk_basic_event_detectionMethod_2 foreign key (detection_method_id) references detection (id) on delete restrict on update restrict;

ALTER TABLE basic_event_occurrence DROP CONSTRAINT fk_basic_event_occurrence_basi_3;
alter table basic_event_occurrence add constraint fk_basic_event_occurrence_basi_3 foreign key (basic_event_id) references basic_event (id) on delete restrict on update restrict;

ALTER TABLE event_occurrence DROP CONSTRAINT fk_event_occurrence_event_5;
alter table event_occurrence add constraint fk_event_occurrence_event_5 foreign key (event_id) references event (id) on delete restrict on update restrict;

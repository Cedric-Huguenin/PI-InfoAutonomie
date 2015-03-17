/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#ifndef SENSORS_HEADER_INCLUDED
#define SENSORS_HEADER_INCLUDED

#include "temperature_sensors.h"
#include "contact_sensors.h"

/**
	RORG (types of radio packet).
*/
typedef enum rorg_tag {
	/**
		Repeated switch communication.
	*/
	RPS=0xf6,
	/**
		1 Byte communication.
	*/
	ONEBS=0xd5,
	/**
		4 Bytes communication.
	*/
	FOURBS=0xa5,
	/**
		Variable length data.
	*/
	VLD=0xD2,
	/**
		Unsupported RORG
	*/
	UNSUPPORTED=0xFF
} Rorg;

union sensors_union {
	TemperatureSensorTelegram* tempSensor;
	TenBitsTemperatureSensorTelegram* tempTenSensor;
	ContactSensorTelegram* contactSensor;
};

typedef enum sensor_type_tag {
	TEMPERATURE,
	TEN_BITS_TEMPERATURE,
	CONTACT,
	UNSUPPORTED_SENSOR
} SensorType;

typedef struct sensor_tag {
	union sensors_union sensor;
	SensorType type;
} Sensor;

Sensor* detectSensor(Erp1Packet* packet);

#endif
/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#ifndef TEMPERATURE_HEADER_INCLUDED
#define TEMPERATURE_HEADER_INCLUDED

#include "../erp1.h"

/**
	A5-02-01: temperature sensor range -40°C to 0°C.
	A5-02-02: temperature sensor range -30°C to 10°C.
	A5-02-03: temperature sensor range -20°C to 20°C.
	A5-02-04: temperature sensor range -10°C to 30°C.
	A5-02-05: temperature sensor range 0°C to 40°C.
	A5-02-06: temperature sensor range 10°C to 50°C.
	A5-02-07: temperature sensor range 20°C to 60°C.
	A5-02-08: temperature sensor range 30°C to 70°C.
	A5-02-09: temperature sensor range 40°C to 80°C.
	A5-02-0A: temperature sensor range 50°C to 90°C.
	A5-02-0B: temperature sensor range 60°C to 100°C.
	A5-02-10: temperature sensor range -60°C to 20°C.
	A5-02-11: temperature sensor range -50°C to 30°C.
	A5-02-12: temperature sensor range -40°C to 40°C.
	A5-02-13: temperature sensor range -30°C to 50°C.
	A5-02-14: temperature sensor range -20°C to 60°C.
	A5-02-15: temperature sensor range -10°C to 70°C.
	A5-02-16: temperature sensor range 0°C to 80°C.
	A5-02-17: temperature sensor range 10°C to 90°C.
	A5-02-18: temperature sensor range 20°C to 100°C.
	A5-02-19: temperature sensor range 30°C to 110°C.
	A5-02-1A: temperature sensor range 40°C to 120°C.
	A5-02-1B: temperature sensor range 50°C to 130°C.
*/
typedef struct temp_tag {
	/**
		The ID of the sender.
	*/
	unsigned char sender[4];
	/**
		1: the telegram is a learning one.
		0: data telegram.
	*/
	unsigned char lrnOn;
	/**
		The temperature raw (on 1 Byte).
	*/
	unsigned char temperature;
	/**
		The interpreted (in °C) temperature.
	*/
	double interpretedTemperature;
	/**
		The type of the temperature sensor (giving the range of temperatures). See
		the above information to choose the right one (last number of the 3-number
		ID).
	*/
	unsigned char type;
} TemperatureSensorTelegram;

/**
	A5-02-20: 10 bits temperature sensor range -10°C to 41.2°C.
	A5-02-30: 10 bits temperature sensor range -40°C to 62.3°C.
*/
typedef struct tenbitstemp_tag {
	/**
		The ID of the sender.
	*/
	unsigned char sender[4];
	/**
		1: the telegram is a learning one.
		0: data telegram.
	*/
	unsigned char lrnOn;
	/**
		The temperature raw (on 10 bits).
	*/
	unsigned char temperature[2];
	/**
		The interpreted (in °C) temperature.
	*/
	double interpretedTemperature;
	/**
		The type of the temperature sensor (giving the range of temperatures). See
		the above information to choose the right one (last number of the 3-number
		ID).
	*/
	unsigned char type;
} TenBitsTemperatureSensorTelegram;

TemperatureSensorTelegram* makeTemperatureSensorTelegram(Erp1Packet *packet);
TenBitsTemperatureSensorTelegram* makeTenBitsTemperatureSensorTelegram(Erp1Packet *packet);


#endif
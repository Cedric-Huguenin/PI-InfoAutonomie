/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#include <stdlib.h>

#include "temperature_sensors.h"

TemperatureSensorTelegram* makeTemperatureSensorTelegram(Erp1Packet *packet) {
	int min, max;
	if(packet == NULL || packet->data[0] != 0xa5 || packet->dataLength != 10 || packet->data[1] != 0 || packet->data[2] != 0) {
		return NULL;
	}
	TemperatureSensorTelegram* returnValue = malloc(sizeof(TemperatureSensorTelegram));
	
	
	
	return NULL;
}

TenBitsTemperatureSensorTelegram* makeTenBitsTemperatureSensorTelegram(Erp1Packet *packet) {
	return NULL;
}
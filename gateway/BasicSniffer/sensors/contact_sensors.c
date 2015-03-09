/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#include <stdlib.h>

#include "contact_sensors.h"

ContactSensorTelegram* makeContactSensorTelegram(Erp1Packet *packet) {
	if(packet == NULL || packet->data[0] != 0xd5 || packet->dataLength != 7) {
		return NULL;
	}
	
	ContactSensorTelegram* returnValue = malloc(sizeof(ContactSensorTelegram));
	
	if(packet->data[2] == 0x01 && (packet->data[1] | 0x09) == 0x09) {
		returnValue->sender[0] = packet->data[3];
		returnValue->sender[1] = packet->data[4];
		returnValue->sender[2] = packet->data[5];
		returnValue->sender[3] = packet->data[6];
	
		if((packet->data[1] & 0x08) == 0) {
			returnValue->lrnPressed = 1;
		} else {
			returnValue->lrnPressed = 0;
		}
		if((packet->data[1] & 0x01) == 0) {
			returnValue->contactClosed = 0;
		} else {
			returnValue->contactClosed = 1;
		}
	}
	return returnValue;
}
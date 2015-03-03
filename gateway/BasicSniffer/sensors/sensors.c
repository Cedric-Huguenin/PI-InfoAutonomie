/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#include "sensors.h"

Sensor* detectSensor(Erp1Packet* packet) {
	int i;
	switch(packet->data[0]) {
	case RPS:
		break;
	case ONEBS:
		if(packet->data[2] == 0x01 && (packet->data[1] | 0x09) == 0x09) {
			Sensor* returnValue = malloc(sizeof(Sensor));
			returnValue->sensor.contactSensor = makeContactSensorTelegram(packet);
			returnValue->type = CONTACT;
			return returnValue;
		}
		break;
	case FOURBS:
		printf("4BS\n");
		break;
	case VLD:
		printf("VLD\n");
		break;
	default:
		printf("Unknown\n");
		break;
	}
	
	
	/*printf("Sender ID: ");
	for(i = 0; i < 4; i++)
		printf("%02x ", packet.data[i]);
	printf("\n");*/
	
	printf("Destination ID: ");
	for(i = 0; i < 4; i++)
		printf("%02x ", packet->destinationID[i]);
	printf("\n");
	printf("Security level: %02x\n", packet->securityLevel);
	printf("Best RSSI: %d dBm\n", packet->dBm);
	
	return NULL;
}
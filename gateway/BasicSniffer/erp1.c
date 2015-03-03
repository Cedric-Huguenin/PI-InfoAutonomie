/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#include "erp1.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/**
	Creates an Erp1Packet from the given EspPacket.
	@param packet the ESP3 original packet.
	@return NULL if packet is not a valid ESP3 packet or wrong data type.
*/
Erp1Packet* makeErp1Packet(EspPacket* packet) {
	int i;
	if(packet == NULL || packet->packetType != RADIO_ERP1 || packet->optionalDataLength != 7)
		return NULL;
	Erp1Packet* newPacket = malloc(sizeof(Erp1Packet));
	newPacket->data = packet->data;
	newPacket->dataLength = packet->dataLength;
	
	newPacket->subTelNum = packet->optionalData[0];
	newPacket->destinationID[0] =  packet->optionalData[1];
	newPacket->destinationID[1] =  packet->optionalData[2];
	newPacket->destinationID[2] =  packet->optionalData[3];
	newPacket->destinationID[3] =  packet->optionalData[4];
	newPacket->dBm = - (0x00ff & packet->optionalData[5]);
	newPacket->securityLevel = packet->optionalData[6];
	
	return newPacket;
}

/**
	Displays a human readable string describing the content of the ERP1 packet.
	@param datagram the ERP1 datagram;
*/
void interpretErp1Packet(Erp1Packet datagram) {
	int i;
	printf("\n--ERP1 datagram--\n");
	printf("Datagram type: ");
	switch(datagram.data[0]) {
	case RPS:
		printf("RPS\n");
		break;
	case ONEBS:
		printf("1BS\n");
		/* Contact sensor */
		if(datagram.data[2] == 0x01 && (datagram.data[1] | 0x09) == 0x09) {
			printf("Contact sensor\n");
			if((datagram.data[1] & 0x08) == 0) {
				printf("Learn button pressed\n");
			} else {
				printf("Learn button not pressed\n");
			}
			if((datagram.data[1] & 0x01) == 0) {
				printf("Contact open\n");
			} else {
				printf("Contact closed\n");
			}
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
		printf("%02x ", datagram.data[i]);
	printf("\n");*/
	
	printf("Destination ID: ");
	for(i = 0; i < 4; i++)
		printf("%02x ", datagram.destinationID[i]);
	printf("\n");
	printf("Security level: %02x\n", datagram.securityLevel);
	printf("Best RSSI: %d dBm\n", datagram.dBm);

}

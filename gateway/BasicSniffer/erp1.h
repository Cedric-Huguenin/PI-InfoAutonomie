/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#ifndef ERP1_HEADER_INCLUDED
#define ERP1_HEADER_INCLUDED

#include "serial.h"

/**
	An ERP1 radio packet
*/
typedef struct erp1_tag {
	/**
		Raw data.
	*/
	unsigned char* data;
	/**
		Length of data.
	*/
	unsigned int dataLength;
	/**
		Number of subtelegram (must be 3 when sent ?).
	*/
	unsigned char subTelNum;
	/**
		Destination address (FF FF FF FF if broadcast).
	*/
	unsigned char destinationID[4];
	/**
		Best RSSI of received subtelegrams.
	*/
	int dBm;
	/**
		Security level : 0 unencrypted, n = type of encryption.
	*/
	unsigned char securityLevel;
} Erp1Packet;

/*
	Begin function definitions
*/

Erp1Packet* makeErp1Packet(EspPacket* packet);
/* void interpretErp1Packet(Erp1Packet datagram); */


#endif
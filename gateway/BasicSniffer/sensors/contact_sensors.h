/*
  Created for EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#ifndef CONTACT_SENSORS_INCLUDED
#define CONTACT_SENSORS_INCLUDED

#include "../erp1.h"

/**
	D5-00-01: contact sensors.
*/
typedef struct contact_tag {
	/**
		The ID of the sender.
	*/
	unsigned char sender[4];
	/**
		1: the learn button is pressed.
		0: the learn button is not pressed.
	*/
	unsigned char lrnPressed;
	/**
		0: the contact is open.
		1: the contact is closed.
	*/
	unsigned char contactClosed;
} ContactSensorTelegram;

ContactSensorTelegram* makeContactSensorTelegram(Erp1Packet *packet);

#endif
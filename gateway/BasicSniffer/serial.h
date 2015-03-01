/*
  Adapted to EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Matthieu Morainville <matthieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.
*/

#ifndef SERIAL_H_INCLUDED
#define SERIAL_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <poll.h>
#include <string.h>

/**
	States of the input decoding.
*/
typedef enum {
	/**
		Waiting for a sync byte.
	*/
	GET_SYNC_STATE=0,
	/**
		State where we wait the data length.
	*/
	GET_DATA_LENGTH_STATE,
	/**
		State where we wait the optional data length.
	*/
	GET_OPTIONAL_DATA_LENGTH_STATE,
	/**
		State where we wait the packet type.
	*/
	GET_PACKET_TYPE,
	/**
		State where we check the header integrity.
	*/
	CHECK_CRC8H_STATE,
	/**
		State where we read the data.
	*/
	GET_DATA_STATE,
	/**
		State where we read the optional data.
	*/
	GET_OPTIONAL_DATA_STATE,
	/**
		State where we check the data integrity.
	*/
	CHECK_CRC8D_STATE
} StateGetPacket;

/**
Enum to describe the packet type following the ESP3 specification below :
------------------------------------------------------------------------------
Type No.  | Value hex | Name               | Description 
0         | 0x00      | ----               | Reserved
1         | 0x01      | RADIO_ERP1         | Radio telegram 
2         | 0x02      | RESPONSE           | Response to any packet 
3         | 0x03      | RADIO_SUB_TEL      | Radio subtelegram 
4         | 0x04      | EVENT              | Event message
5         | 0x05      | COMMON_COMMAND     | Common command 
6         | 0x06      | SMART_ACK_COMMAND  | Smart Ack command
7         | 0x07      | REMOTE_MAN_COMMAND | Remote management command
8         | 0x08      | ---                | Reserved for EnOcean
9         | 0x09      | RADIO_MESSAGE      | Radio message
10        | 0x0A      | RADIO_ERP2         | ERP2 protocol radio telegram
11...127  | 0x08..7F  | ---                | Reserved for EnOcean 
128...255 | 0x80...FF |                    | available Manufacturer specific commands and messages
*/
typedef enum {
	RADIO_ERP1=0x01,
	RESPONSE=0x02,
	RADIO_SUB_TEL=0x03,
	EVENT=0x04,
	COMMON_COMMAND=0x05,
	SMART_ACK_COMMAND=0x06,
	REMOTE_MAN_COMMAND=0x07,
	RADIO_MESSAGE=0x09,
	RADIO_ERP2=0x0A
} PacketType;

/**
	ESP3 packet structure.
*/
typedef struct packet_tag {
	/**
		Length of the data.
	*/
	unsigned short int dataLength;
	/**
		Length of the optional data.
	*/
	unsigned char optionalDataLength;
	/**
		Type of the packet.
	*/
	unsigned char packetType;
	/**
		CRC8 of the header fileds.
	*/
	unsigned char hcrc;
	/**
		Pointer to the data.
	*/
	unsigned char *data;
	/**
		Pointer to the optional data.
	*/
	unsigned char *optionalData;
	/**
		CRC8 of the data.
	*/
	unsigned char dcrc;
} EspPacket;

int getch(int fd, int ms);
int set_tty(int fd, struct termios *oldt, struct termios *newt);
int restore_tty(int fd, struct termios *oldt);
int open_tty(char *devname);
int read_tty(int fd, char *buf, size_t lbuf);
int readEsp(EspPacket *packet, char* buffer, size_t buf_len);
char isValid(EspPacket *packet);
char* typeToString(PacketType type);

#endif
#ifndef SERIAL_H_INCLUDED
#define SERIAL_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <poll.h>
#include <string.h>

typedef enum {
	GET_SYNC_STATE=0,
	GET_DATA_LENGTH_STATE,
	GET_OPTIONAL_DATA_LENGTH_STATE,
	GET_PACKET_TYPE,
	CHECK_CRC8H_STATE,
	GET_DATA_STATE,
	GET_OPTIONAL_DATA_STATE,
	CHECK_CRC8D_STATE
} StateGetPacket;

/*
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

/*
ESP3 packet
*/
typedef struct packet_tag {
	unsigned short int dataLength;
	unsigned char optionalDataLength;
	unsigned char packetType;
	unsigned char hcrc;
	unsigned char *data;
	unsigned char *optionalData;
	unsigned char dcrc;
} EspPacket;

int getch(int fd, int ms);
int set_tty(int fd, struct termios *oldt, struct termios *newt);
int restore_tty(int fd, struct termios *oldt);
int open_tty(char *devname);
int read_tty(int fd, char *buf, size_t lbuf);
void readEsp(EspPacket *packet, char* buffer, size_t buf_len);
char isValid(EspPacket *packet);
char* typeToString(PacketType type);

#endif
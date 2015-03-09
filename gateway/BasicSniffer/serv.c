/*

  Adapted from Paul Griffiths TIMESERV.C
  
  Very Simple EnOcean  serial port to stdout
  
  Adapted to EnOcean ESP3 by Cédric Huguenin <cedric.huguenin@telecomnancy.eu>,
  Mathieu Morainville <mathieu.morainville@telecomnancy.eu>
  and Mickaël Walter <mickael.walter@telecomnancy.eu> for TELECOM Nancy and 
  Institut Mines Télécom.

*/

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <string.h>
#include <inttypes.h>

#include "serial.h"
#include "helper.h"
#include "erp1.h"
#include "sensors/sensors.h"

/*  Global constants  */

/* This must at least be set to 21 (less seems to be not working) */
#define MAX_LINE           (21)

void displayErp1Packet(Erp1Packet datagram);

int main(int argc, char *argv[])
{
    char      buffer[MAX_LINE];
	EspPacket packet;
	int lastReadBufferSize = 0;
	
    int fd;
    struct termios oldt, newt;
    if (argc < 2)
        return(-1);
    fd = open_tty(argv[1]);
    if (fd == -1)
        return(-1);
    set_tty(fd, &oldt, &newt);
	
	memset(&packet, 0, sizeof(packet));
    /* enter infinite loop reading tty and outputing until ^C */
    printf("entering serial read, write loop. Press CTRL+C to quit.\n");
    while ( 1 )
    {
        int ret = 0;
		if(lastReadBufferSize == ret)
			ret = read_tty(fd, buffer, sizeof(buffer));
		
		ret = readEsp(&packet, buffer, ret);
		if(isValid(&packet)) {
			int i;
			Erp1Packet* erpPacket = NULL;
			
			printf("\n--- ESP 3 Packet ---\n");
			printf("Packet type:%s\n", typeToString(packet.packetType));
			printf("Data Length:%d Bytes\n", packet.dataLength);
			printf("Optional Data Length:%d Bytes\n", packet.optionalDataLength);
			printf("Data:\n");
			
			for(i = 0; i < packet.dataLength; i++) {
				printf("%02x ", packet.data[i]);
				if((i != 0 && i % MAX_LINE == 0) || i == packet.dataLength-1)
					printf("\n");
			}
			printf("Optional Data:\n");
			for(i = 0; i < packet.optionalDataLength; i++)
			{
				printf("%02x ", packet.optionalData[i]);
				if(i != 0 && i % MAX_LINE == 0)
					printf("\n");
			}
			erpPacket = makeErp1Packet(&packet);
			if(erpPacket != NULL) {
				displayErp1Packet(*erpPacket);
				free(erpPacket);
				erpPacket = NULL;
			}
			printf("\n\n");
			free(packet.data);
			free(packet.optionalData);
			memset(&packet, 0, sizeof(packet));
		}
    }

    printf("restoring previous tty settings\n");
    restore_tty(fd, &oldt);

    printf("exiting\n");
    return(0);
}

void displayErp1Packet(Erp1Packet datagram) {
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
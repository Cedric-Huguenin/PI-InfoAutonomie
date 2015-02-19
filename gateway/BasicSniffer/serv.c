
/* todo:
1. trap ^C signal to properly exit with cleanup
2. Decoding of EnOcean packets is very simplistic. Only supports Radio packet.
*/

/*

  Adapted from Paul Griffiths TIMESERV.C
  
  Very Simple EnOcean  serial port to TCP/IP gateway

*/

#include "helper.h"           /*  our own helper functions  */

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <string.h>
#include <inttypes.h>

#include "serial.h"

/*  Global constants  */

/* This must at least be set to 21 (less seems to be not working) */
#define MAX_LINE           (21)

int main(int argc, char *argv[])
{
    char      buffer[MAX_LINE];      /*  character buffer          */
	EspPacket packet;
	
    int fd;
    struct termios oldt, newt;
    if (argc < 2)
        return(-1);
    fd = open_tty(argv[1]);
    if (fd == -1)
        return(-1);
    set_tty(fd, &oldt, &newt);

	/*
	typedef struct packet_tag {
		uint16 dataLength;
		uint8 optionalDataLength;
		uint8 packetType;
		uint8 hcrc;
		void *data;
		void *optionalData;
		uint8 dcrc;
	};
	*/
	
	memset(&packet, 0, sizeof(packet));
    /* enter infinite loop reading tty and outputing until ^C */
    printf("entering serial read, write loop. Press CTRL+C to quit.\n");
    while ( 1 )
    {
        int ret;
		ret = read_tty(fd, buffer, sizeof(buffer));
		
		readEsp(&packet, buffer, ret);
		if(isValid(&packet)) {
			int i;
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

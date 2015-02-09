
/* todo:
1. trap ^C signal to properly exit with cleanup
2. Decoding of EnOcean packets is very simplistic. Only supports Radio packet.
*/

/*

  Adapted from Paul Griffiths TIMESERV.C
  
  Very Simple EnOcean  serial port to TCP/IP gateway

*/


#include <sys/socket.h>       /*  socket definitions        */
#include <sys/types.h>        /*  socket types              */
#include <arpa/inet.h>        /*  inet (3) funtions         */
#include <unistd.h>           /*  misc. UNIX functions      */

#include "helper.h"           /*  our own helper functions  */

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <string.h>

#include "serial.h"

/*  Global constants  */

#define TIME_PORT          (2002)
#define MAX_LINE           (21)


int main(int argc, char *argv[])
{
    int       list_s;                /*  listening socket          */
    int       conn_s;                /*  connection socket         */
    short int port;                  /*  port number               */
    struct    sockaddr_in servaddr;  /*  socket address structure  */
    char      buffer[MAX_LINE];      /*  character buffer          */
    char     *endptr;                /*  for strtol()              */

    int fd;
    struct termios oldt, newt;
    if (argc < 2)
        return(-1);
    fd = open_tty(argv[1]);
    if (fd == -1)
        return(-1);
    set_tty(fd, &oldt, &newt);

    /*  Get port number from the command line, and
        set to default port if no arguments were supplied  */

    if ( argc == 3 )
    {
	port = strtol(argv[2], &endptr, 0);
	if ( *endptr ) {
	    fprintf(stderr, "ECHOSERV: Invalid port number.\n");
	    exit(EXIT_FAILURE);
	}
    }
    else if (argc == 2)
	port = TIME_PORT;
    else {
	fprintf(stderr, "ECHOSERV: Invalid arguments.\n");
	exit(EXIT_FAILURE);
    }

    /*  Create the listening socket  */
    printf("creating socket\n");
    if ( (list_s = socket(AF_INET, SOCK_STREAM, 0)) < 0 ) {
	fprintf(stderr, "ECHOSERV: Error creating listening socket.\n");
	exit(EXIT_FAILURE);
    }

    /*  Set all bytes in socket address structure to
        zero, and fill in the relevant data members   */
    memset(&servaddr, 0, sizeof(servaddr));
    servaddr.sin_family      = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port        = htons(port);

    /*  Bind our socket addresss to the 
	listening socket, and call listen()  */
    printf("binding to socket on port %d\n",port);
    if ( bind(list_s, (struct sockaddr *) &servaddr, sizeof(servaddr)) < 0 )
    {
	fprintf(stderr, "ECHOSERV: Error calling bind()\n");
	exit(EXIT_FAILURE);
    }

    printf("create listener\n");
    if ( listen(list_s, LISTENQ) < 0 )
    {
	fprintf(stderr, "ECHOSERV: Error calling listen()\n");
	exit(EXIT_FAILURE);
    }

    /*  Wait for a connection, then accept() it  */
    printf("waiting for connection\n");
    if ( (conn_s = accept(list_s, NULL, NULL) ) < 0 )
    {
        fprintf(stderr, "ECHOSERV: Error calling accept()\n");
        exit(EXIT_FAILURE);
    }

    /* enter infinite loop reading tty and outputing until ^C */
    printf("entering serial read, tcpip write loop\n");
    while ( 1 )
    {
        int ret;
	ret = read_tty(fd, buffer, sizeof(buffer));
	if (ret == -1)
	    break;
	Writeline(conn_s, buffer, sizeof(buffer));
    }

    /*  Close the connected socket  */
    printf("closing connection\n");
    if ( close(conn_s) < 0 )
    {
        fprintf(stderr, "ECHOSERV: Error calling close()\n");
        exit(EXIT_FAILURE);
    }

    printf("restoring previous tty settings\n");
    restore_tty(fd, &oldt);

    printf("exiting\n");
    return(0);
}

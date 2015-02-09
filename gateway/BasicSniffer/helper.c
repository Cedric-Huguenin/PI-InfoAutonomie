/*

  Adapted from Paul Griffiths example who in turn adapted from
  "Unix Network Programming", W Richard Stevens (Prentice Hall).

*/

#include "helper.h"
#include <sys/socket.h>
#include <unistd.h>
#include <errno.h>
#if 1
#include <stdio.h>
#endif


/*  Read a line from a socket  */

ssize_t Readline(int sockd, void *vptr, size_t maxlen) {
    ssize_t n, rc;
    char    c, *buffer;

    buffer = vptr;

    for ( n = 1; n < maxlen; n++ ) {
	
	if ( (rc = read(sockd, &c, 1)) == 1 ) {
	    *buffer++ = c;
	    if ( c == '\n' )
		break;
	}
	else if ( rc == 0 ) {
	    if ( n == 1 )
		return 0;
	    else
		break;
	}
	else {
	    if ( errno == EINTR )
		continue;
	    return -1;
	}
    }

    *buffer = 0;
    return n;
}


/*  Write a line to a socket  */

ssize_t Writeline(int sockd, const void *vptr, size_t n) {
    size_t      nleft;
    ssize_t     nwritten;
    const char *buffer;

    buffer = vptr;
    nleft  = n;

    while ( nleft > 0 ) {
#if 1
	char buf[1024],*p = buf;
	int i;
	for(i=0;i<nleft;i++)
	    p += sprintf(p,"%02x ",buffer[i] & 0xff);
	/*p += sprintf(p," length=%d\n",nleft);*/
	p += sprintf(p,"\r\n");
	nwritten = nleft;
	if ( (i = write(sockd, buf, p-buf)) <= 0 ) {
#else
	if ( (nwritten = write(sockd, buffer, nleft)) <= 0 ) {
#endif
	    if ( errno == EINTR )
		nwritten = 0;
	    else
		return -1;
	}
	nleft  -= nwritten;
	buffer += nwritten;
    }

    return n;
}


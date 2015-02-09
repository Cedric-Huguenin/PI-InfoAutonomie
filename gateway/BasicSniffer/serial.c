
    #include "serial.h"

    int getch(int fd, int ms)
    {
        int ret;
        struct pollfd pfds[1];

        pfds[0].fd = fd;
        pfds[0].events = POLLIN;
        poll(pfds, 1, ms);
        if (pfds[0].revents & POLLIN) {
            char ch;
            read(fd, &ch, 1);
            ret = ch;
        } else {
            ret = 0;
        }
        return ret;
    }

    int set_tty(int fd, struct termios *oldt, struct termios *newt)
    {
        tcgetattr(fd, oldt);
        newt = oldt;
        newt->c_iflag &= ~(IGNBRK | BRKINT | ICRNL |
                    INLCR | PARMRK | INPCK | ISTRIP | IXON);
        newt->c_oflag = 0;
        newt->c_lflag &= ~(ECHO | ECHONL | ICANON | IEXTEN | ISIG);
        newt->c_cflag &= ~(CSIZE | PARENB);
        newt->c_cflag |= CS8;
        newt->c_cc[VMIN]  = 1;
        newt->c_cc[VTIME] = 0;
        cfsetispeed(newt, B57600);
        cfsetospeed(newt, B57600);
        tcsetattr(fd, TCSANOW, newt);
	return(0);
    }

    int restore_tty(int fd, struct termios *oldt)
    {
        return(tcsetattr(fd, TCSANOW, oldt));
    }

    int open_tty(char *devname)
    {
	int fd;
        printf("opening %s\n",devname);
        fd = open(devname,O_RDWR | O_NOCTTY | O_NDELAY);
        printf("fd=%d\n",fd);
	return(fd);
    }

    int read_tty(int fd, char *buf, size_t lbuf)
    {
        int c;
	char *p = buf;
	char *pe = buf+lbuf;
	do
	{
           if ((c = getch(fd,500)) > 0)
	   {
              printf("%02x ", c);
	      if (c == 0x03)
	          return(-1);
	   }
        } while (c != 0x55);
	printf("\n");
	*p++ = c;
	while(p<pe)
	{
           c = getch(fd,500);
           printf("%02x ", c);
	   *p++ = c;
	   if (c == 0x03)
	       return(-1);
        };
	printf(" length=%d\n",p-buf);
	return(p-buf);
    }

#if 0
    int main(int argc, char *argv[])
    {
	int fd;
        int x;
        struct termios oldt, newt;
	char buf[21];
        if (argc < 2)
            return(-1);
	fd = open_tty(argv[1]);
        if (fd == -1)
            return(-1);
	set_tty(fd, &oldt, &newt);
	read_tty(fd, buf, sizeof(buf));
	restore_tty(fd, &oldt);
        return 0;
    }
#endif

    #include <stdio.h>
    #include <fcntl.h>
    #include <unistd.h>
    #include <termios.h>
    #include <poll.h>

    int getch(int ms);
    int fd;

    int main(int argc, char *argv[])
    {
        int x;
        if (argc < 2)
            return(-1);
        printf("opening %s\n",argv[1]);
        fd = open(argv[1],O_RDWR | O_NOCTTY | O_NDELAY);
        printf("fd=%d\n",fd);
        if (fd == -1)
            return(-1);
        do {
           if ((x = getch(500)))
              printf("%02x ", x);
           else
              printf("Not yet!\n");
        } while (x != 3);
	printf("\n");
        return 0;
    }

    int getch(int ms)
    {
        int ret;
        struct termios oldt, newt;
        struct pollfd pfds[1];

        tcgetattr(fd, &oldt);
        newt = oldt;
        newt.c_iflag &= ~(IGNBRK | BRKINT | ICRNL |
                    INLCR | PARMRK | INPCK | ISTRIP | IXON);
        newt.c_oflag = 0;
        newt.c_lflag &= ~(ECHO | ECHONL | ICANON | IEXTEN | ISIG);
        newt.c_cflag &= ~(CSIZE | PARENB);
        newt.c_cflag |= CS8;
        newt.c_cc[VMIN]  = 1;
        newt.c_cc[VTIME] = 0;
        cfsetispeed(&newt, B57600);
        cfsetospeed(&newt, B57600);
        tcsetattr(fd, TCSANOW, &newt);
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
        tcsetattr(fd, TCSANOW, &oldt);
        return ret;
    }


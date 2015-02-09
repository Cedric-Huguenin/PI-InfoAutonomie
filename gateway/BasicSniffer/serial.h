
    #include <stdio.h>
    #include <fcntl.h>
    #include <unistd.h>
    #include <termios.h>
    #include <poll.h>

    int getch(int fd, int ms);
    int set_tty(int fd, struct termios *oldt, struct termios *newt);
    int restore_tty(int fd, struct termios *oldt);
    int open_tty(char *devname);
    int read_tty(int fd, char *buf, size_t lbuf);


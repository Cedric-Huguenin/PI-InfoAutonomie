
#include "serial.h"
#include "helper.h"

typedef enum {
	WAITING_SYNC=0,
	SYNC_RECEIVED
} ReadingState;

static ReadingState readingState = WAITING_SYNC;

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
	
	/* TODO add something to avoid to have to wait having a full buffer */
	if(readingState == WAITING_SYNC) {
		do
		{
			c = getch(fd,500);
		} while (c != 0x55);
		readingState = SYNC_RECEIVED;
	}
	
	*p++ = c;
	while(p<pe)
	{
        c = getch(fd,500);
	    *p++ = c;
    };
	return(p-buf);
}

/**
 * Reads the given buffer and tries to find the specification of an ESP packet.
 * @param packet the EspPacket structure to be filled.
 * @param buffer the buffer in which the data is stored.
 * @param buf_len the length of the buffer.
*/
void readEsp(EspPacket *packet, char *buffer, size_t buf_len) {
	/*
	ESP3 packet structure through the serial port.
	Protocol bytes are generated and sent by the application
	Sync = 0x55
	CRC8H
	CRC8D
	1 2 1 1 1 u16DataLen + u8OptionLen 1
	+------+------------------+---------------+-----------+-----------+-------------/------------+-----------+
	| 0x55 | u16DataLen | u8OptionLen | u8Type | CRC8H | DATAS | CRC8D |
	+------+------------------+---------------+-----------+-----------+-------------/------------+-----------+
	DATAS structure:
	u16DataLen u8OptionLen
	+--------------------------------------------+----------------------+
	| Data | Optional |
	+--------------------------------------------+----------------------+
	*/
	
	static StateGetPacket getPacketState = GET_SYNC_STATE;
	
	static int dataOffset = 0;
	
	static char msb = 1;
	
	int i;
	
	char header[4];
		
	for(i = 0; i < buf_len; i++) {
		printf("%02x ", buffer[i]);
		if((i !=0 && i % 21 == 0) || i == buf_len-1)
			printf("\n");
	}
	
	for(i = 0; i < buf_len; i++) {
		switch(getPacketState) {
			case GET_SYNC_STATE:
				if(buffer[i] == 0x55) {
					getPacketState = GET_DATA_LENGTH_STATE;
					readingState = SYNC_RECEIVED;
				} else {
					readingState = WAITING_SYNC;
				}
				break;
			case GET_DATA_LENGTH_STATE:
				if(msb == 1) {
					packet->dataLength = buffer[i] * 0x100;
					msb = 0;
				} else {
					packet->dataLength += buffer[i];
					msb = 1;
					getPacketState = GET_OPTIONAL_DATA_LENGTH_STATE;
				}
				break;
			case GET_OPTIONAL_DATA_LENGTH_STATE:
				packet->optionalDataLength = buffer[i];
				getPacketState = GET_PACKET_TYPE;
				break;
			case GET_PACKET_TYPE:
				packet->packetType = buffer[i];
				getPacketState = CHECK_CRC8H_STATE;
				break;
			case CHECK_CRC8H_STATE:
				packet->hcrc = buffer[i];
				
				header[0] = (packet->dataLength & 0xff00)/0x100;
				header[1] = packet->dataLength & 0x00ff;
				header[2] = packet->optionalDataLength;
				header[3] = packet->packetType;
				if(crc8check(header, sizeof(header)) != packet->hcrc) {
					memset(packet, 0, sizeof(packet));
					getPacketState = GET_SYNC_STATE;
					readingState = WAITING_SYNC;
					printf("\nBad CRC8 for header\n");
				} else {
					getPacketState = GET_DATA_STATE;
				}
				break;
			case GET_DATA_STATE:
				if(dataOffset == 0) {
					packet->data = malloc(packet->dataLength*sizeof(char));
				}
				packet->data[dataOffset] = buffer[i];
				if(dataOffset < packet->dataLength-1) {
					dataOffset++;
				} else {
					dataOffset = 0;
					getPacketState = GET_OPTIONAL_DATA_STATE;
				}
				break;
			case GET_OPTIONAL_DATA_STATE:
				if(dataOffset == 0) {
					packet->optionalData = malloc(packet->optionalDataLength*sizeof(char));
				}
				packet->optionalData[dataOffset] = buffer[i];
				if(dataOffset < packet->optionalDataLength-1) {
					dataOffset++;
				} else {
					dataOffset = 0;
					getPacketState = CHECK_CRC8D_STATE;
				}
				break;
			case CHECK_CRC8D_STATE: ;
				char* data = malloc((packet->dataLength+packet->optionalDataLength)*sizeof(char));
				packet->dcrc = buffer[i];
				memcpy(data, packet->data, packet->dataLength);
				if(packet->optionalDataLength != 0)
					memcpy(data+packet->dataLength, packet->optionalData, packet->optionalDataLength);
				getPacketState = GET_SYNC_STATE;
				readingState = WAITING_SYNC;
				
				if(crc8check(data, packet->dataLength+packet->optionalDataLength) != packet->dcrc) {
					memset(packet, 0, sizeof(packet));
					free(data);
					printf("\nBad CRC8 for data\n");
				} else {
					free(data);
					return;
				}
				break;
			default:
				return;
				break;
		}
	}
}

/**
 * Checks if the given packet is valid by using the given CRCs
 * @param packet a pointer to the packet to be checked
 * @return 1 if valid, 0 otherwise
 */
char isValid(EspPacket *packet) {
	/* Checking header integrity */
	char header[4];
	/* Checking data integrity */
	char* data = malloc((packet->dataLength+packet->optionalDataLength)*sizeof(char));
	
	header[0] = (packet->dataLength & 0xff00)/0x100;
	header[1] = packet->dataLength & 0x00ff;
	header[2] = packet->optionalDataLength;
	header[3] = packet->packetType;
	if(crc8check(header, sizeof(header)) != packet->hcrc) {
		return 0;
	}
	
	memcpy(data, packet->data, packet->dataLength);
	if(packet->optionalDataLength !=0 && packet->optionalData != NULL)
		memcpy(data+packet->dataLength, packet->optionalData, packet->optionalDataLength);
	if(crc8check(data, packet->dataLength+packet->optionalDataLength) != packet->dcrc) {
		free(data);
		return 0;
	}
	free(data);
	return 1;
}

char* typeToString(PacketType type) {
	static char* radioErp1 = "RADIO ERP1";
	static char* response = "RESPONSE";
	static char* radioSubTel = "RADIO SUB TEL";
	static char* event = "EVENT";
	static char* commonCommand = "COMMON COMMAND";
	static char* smartAckCommand = "SMART ACK COMMAND";
	static char* remoteManCommand = "REMOTE MAN COMMAND";
	static char* radioMessage = "RADIO MESSAGE";
	static char* radioErp2 = "RADIO ERP2";
	
	switch(type) {
	case RADIO_ERP1:
		return radioErp1;
		break;
	case RESPONSE:
		return response;
		break;
	case RADIO_SUB_TEL:
		return radioSubTel;
		break;
	case EVENT:
		return event;
		break;
	case COMMON_COMMAND:
		return commonCommand;
		break;
	case SMART_ACK_COMMAND:
		return smartAckCommand;
		break;
	case REMOTE_MAN_COMMAND:
		return remoteManCommand;
		break;
	case RADIO_MESSAGE:
		return radioMessage;
		break;
	default:
		return radioErp2;
		break;
	}
}

all : serv.o helper.o serial.o erp1.o
	gcc -o serv serv.o helper.o serial.o erp1.o -Wall

serv.o: serv.c helper.h serial.h erp1.h
	gcc -o serv.o serv.c -c -pedantic -Wall

helper.o: helper.c helper.h
	gcc -o helper.o helper.c -c -pedantic -Wall

serial.o: serial.c serial.h
	gcc -o serial.o serial.c -c -pedantic -Wall
	
erp1.o: erp1.h erp1.c serial.h
	gcc -o erp1.o erp1.c -c -pedantic -Wall
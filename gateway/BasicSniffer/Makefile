all : serv.o helper.o serial.o erp1.o temperature_sensors.o contact_sensors.o sensors.o
	gcc -o serv $^  -Wall

serv.o: serv.c helper.h serial.h erp1.h
	gcc -o serv.o serv.c -c -pedantic -Wall

helper.o: helper.c helper.h
	gcc -o helper.o helper.c -c -pedantic -Wall

serial.o: serial.c serial.h
	gcc -o serial.o serial.c -c -pedantic -Wall
	
erp1.o: erp1.h erp1.c serial.h sensors/sensors.h
	gcc -o erp1.o erp1.c -c -pedantic -Wall
	

sensors.o: sensors/sensors.c sensors/sensors.h
	gcc -o sensors.o sensors/sensors.c -c -pedantic -Wall
	
temperature_sensors.o: sensors/temperature_sensors.c sensors/temperature_sensors.h
	gcc -o temperature_sensors.o sensors/temperature_sensors.c -c -pedantic -Wall
	
contact_sensors.o: sensors/contact_sensors.c sensors/contact_sensors.h
	gcc -o contact_sensors.o sensors/contact_sensors.c -c -pedantic -Wall
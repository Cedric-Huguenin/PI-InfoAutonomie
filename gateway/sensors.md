# EnOcean sensors quick manual

## Introduction
EnOcean sensors are sensors working without any battery. That's why they need almost no attention when working.
There are many sensor types. Here are a few example we are using :
* Presence sensor
* Temperature and humidity sensor
* Contact sensors (for doors or windows)
* Power consumption sensors

They work after a learning phase in which they identify their neighbors. This is necessary to make pairs between two components like the gateway and the sensor or the sensor and an EnOcean switch.
After that they send regularly data when they measure something. Sometimes, if there is a big delta in the measure, they instantly send the data.

## Sensors identification

The sensors identify themselves to indicate the nature of their measure. Theses codes are called EEP for EnOcean Equipment Profile.
For example, a temperature sensor can be identified as A5-02-05.

## Using EnOcean Link Library

For some reasons you could have to use directly EnOcean Link for debugging purposes.
To compile : g++ -o yourprogram yourprogram.cpp the/path/to/the/lib.a
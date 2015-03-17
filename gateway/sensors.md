# EnOcean sensors quick manual

## Introduction
EnOcean sensors are sensors working without any battery. That's why they need almost no attention when working.
There are many sensor types. Here are a few example we are using :
* Presence sensor
* Temperature sensor
* Contact sensors (for doors or windows)
* Power consumption sensors

They work after a learning phase in which they identify their neighbors. This is necessary to make pairs between two components like the gateway and the sensor or the sensor and an EnOcean switch.
After that they send regularly data when they measure something. Sometimes, if there is a big delta in the measure, they instantly send the data.

## Sensors identification

The sensors identify themselves to indicate the nature of their measure. Theses codes are called EEP for **EnOcean Equipment Profile**.
For example, a temperature sensor can be identified as *A5-02-05*.

## EnOcean sensors and EnoPush

Two steps must be taken in account when you use a EnOcean sensor: the **learning phase** and the **normal phase**. EnoPush is constantly listening to the two types of datagrams. That is why **you just have to push the learn button on your EnOcean device to add it to the EnoPush base**.

**Note:** some devices learn other devices (like power consumption sensors which act also as a actuator) and have a long learning time. Make sure to wait the end of it before launching another.

When a TeachIn datagram from a supported sensor arrives, EnoPush adds it to its base, sends its informations to the associated platform and saves it on the file system. So you can add the sensors anytime and one by one to know the affectation of the sensor (fridge's door for example).

If a sensor has not sent a TeachIn datagram to the gateway, EnoPush will ignore the following datagrams the sensor will send.

EnoPush controls the D2-01-08 (power cunsumption sensor and switch actuator) and sets it always on.   Moreover, the sensor is configured to send the power consumption within 4 minutes since the last measurement (less if a significant change occurs).

**Note:** Everytime a measurement is received by EnoPush, it is sent to the platform in JSON format but not saved on the gateway.
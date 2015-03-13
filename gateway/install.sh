#!/bin/bash

# Verifying we are root
#....
if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi
# ...

echo 'Entering gateway initial configuration...'
echo 'Updating packages information'
apt-get update
echo 'Would you like to add a bit more security ? (yes/no) (useful if the Pi is directly accessible from the Internet (not behind the NAT or the Box firewall)'
read choice
if [[ $choice == "yes" || $choice == "y" ]]
then
    apt-get install fail2ban
fi
echo 'Installing Wifi utilities for WiPi'
sudo apt-get install wpasupplicant wireless-tools
if [[ -f "./wipi-config.sh" ]]
then
    echo 'Would you like to configure the Wifi interface now ? (yes/no)'
    read choice
    if [[ $choice == 'yes' || $choice == 'y' ]]
    then
        ./wipi-config.sh
    fi
fi

echo 'Installing tools to run EnOcean softwares'
apt-get install gcc g++
apt-get install perl libdevice-serialport-perl libio-socket-ssl-perl libwww-perl
apt-get install –f
apt-get install openjdk-7-jdk
echo 'Make sure you choose the right JRE : openjdk-7-jre' 
update-alternatives --config java
apt-get install librxtx-java

echo 'Deactivating the GPIO used by Linux to connect to EnOceanPi'
if [[ !( -f "./serial.sh" ) ]]
then
	wget https://raw.github.com/lurch/rpi-serial-console/master/rpi-serial-console -O ./serial.sh
fi
chmod +x ./serial.sh
./serial.sh disable

# echo 'Installing FHEM'
# if [[ -d "/opt/fhem" ]]
# then
	# echo "Warning : it seems that FHEM is already installed. If you continue, you could lose data."
	# echo "Would you like to continue anyway ? (yes/no)"
	# read choice
	# if [[ $choice != "yes" || $choice != "y" ]]
	# then
		# echo 'Note : A reboot could be necessary (for the serial access for example)'
		# exit 0
	# fi
# fi
# if [[ !( -f "./fhem-5.6.tar.gz" ) ]]
# then
	# wget http://fhem.de/fhem-5.6.tar.gz
# fi
# tar -xf ./fhem-5.6.tar.gz
# cd ./fhem-5.6
# make install
# cd /opt/fhem
# echo "define TCM310_0 TCM 310 /dev/ttyAMA0@57600" >> fhem.cfg
# perl fhem.pl fhem.cfg

echo 'Installing EnoPush'
# TODO configure EnoPush

echo 'Note : A reboot could be necessary (for the serial access for example)'
echo 'Installation finished'
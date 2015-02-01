#!/bin/bash

# Verifying we are root
FILE="/tmp/out.$$"
GREP="/bin/grep"
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
apt-get install gcc
apt-get install g++

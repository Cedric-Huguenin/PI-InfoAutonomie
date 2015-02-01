#!/bin/bash

echo "This is the Wireless configuration utility"

# Verifying we are root
FILE="/tmp/out.$$"
GREP="/bin/grep"
#....
if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi
# ...

if [[ `ifconfig | grep "wlan0"` == "" ]]
then
    echo "There are no WLAN interface found... Did you plug in the WiPi module ?"
    exit 1
fi

if [[ `dpkg -l | grep wireless-tools` == "" ]]
then
    echo "Wireless-tools are not installed. Would you like to intall it ? (yes/no) "
    read choice
    if [[ $choice == "yes" || $choice == "y" ]]
    then
        apt-get install wireless-tools
    else
        echo "Cannot continue without wireless-tools. Exiting."
        exit 0
    fi
fi

while [[ $choice != '4' ]]
do
    echo "1. Show the list of available wifi networks"
    echo "2. Connect to a particular wifi network"
    echo "3. Disconnect from the current wifi network"
    echo "4. Quit"
    read choice
    
    case $choice in
    '1')
        iwlist wlan0 scan | less
        ;;
    '2')
        echo "Turning off and on the interface to reinitialize it"
        ifconfig wlan0 down
        dhclient -r wlan0 up
        ifconfig wlan0 up
        echo "Do you want to list networks ? (yes/no)"
        read choice2
        if [[ $choice2 == "yes" || $choice2 == "y" ]]
        then
            iwlist wlan0 scan | less
        fi
        
        echo "Type the ESSID of the network (e.g. MyNetwork)"
        read $essid
        echo "Type the password/key of your network (enter if not secured)"
        read $password
        
        if [[ $password != "" ]]
        then
            iwconfig wlan0 mode managed key $password essid $essid
        else
            iwconfig wlan0 mode managed key off essid $essid
        fi
        ;;
    '3')
        echo "Disconnecting..."
        ifconfig wlan0 down
        dhclient -r wlan0 up
        ifconfig wlan0 up
        ;;
esac
    
done

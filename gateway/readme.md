# Configure the Raspberry Pi gateway

## Default parameters
Default user : pi
Default password : PiGatewayEnOcean

Hostname : PiGateway

## If the configuration starts from NOOBs unconfigured ## 
To make an initial configuration of the Gateway (Raspberry Pi) you will have to follow these steps.

First of all, we need the hardware to work :
* Connect a keyboard to the USB port.
* Connect a screen to the HDMI port.
* We'll need an Internet connection : connect a Ethernet cable.
* Of course the Pi needs power : use the micro-USB.

When the Pi is powered it will immediately start on the NOOBs configuration menu.

Use the item 2 of the NOOBs menu to change user password. Make sure with the item 3 to start in console.
Use the item 4 to configure the locale. You can set it to french (FR-fr UTF-8). With the same item, set the Timezone to current local timezone.

Change the hostname by using the item 8. Select hostname and name it.

You can explore other menus but for the minimal configuration that's it. Highlight finish and press enter to exit, reboot.

Log in.

# To access from a remote client
The openssh-server is already installed. You just have to identify the Gateway IP address : ifconfig if you access it from the LAN.

You can now execute the installation script : install.sh
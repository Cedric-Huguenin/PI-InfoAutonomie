# Configure the Raspberry Pi gateway

## Default parameters

Default user : pi  
Default password : PiGatewayEnOcean

Hostname : PiGateway

## If the configuration starts from NOOBs unconfigured

To make an initial configuration of the Gateway (Raspberry Pi) you will have to follow these steps.

First of all, we need the hardware to work :
* Connect a keyboard to the USB port.
* Connect a screen to the HDMI port.
* We'll need an Internet connection : connect a Ethernet cable.
* Of course the Pi needs power : use the micro-USB.

When the Pi is powered it will immediately start on the NOOBs configuration menu.

Use the item 2 of the NOOBs menu to change user password. Make sure with the item 3 to start in console.

Use the item 4 to configure the locale. You can set it for example to french (FR-fr UTF-8). With the same item, set the Timezone to current local timezone.

Change the hostname by using the item 8. Select hostname and name it.

You can explore other menus but for the minimal configuration that's it. Highlight finish and press enter to exit, reboot.

## To access from a remote client

The openssh-server is already installed. You just have to identify the Gateway IP address.

You may want to install vsftpd (see [this tutorial](https://www.digitalocean.com/community/tutorials/how-to-set-up-vsftpd-on-ubuntu-12-04 "Install a vsftpd server")) to upload the required files on the Raspberry Pi.

## Installation of the gateway

Copy these files to your working folder (keeping these names and relative paths if you want to use the install script):
* install.sh
* wipi-config.sh if you want to use wifi
* serialtty.sh (if not copied, the install script will try to download it from the Internet)
* EnoPush/start.sh
* EnoPush/stop.sh
* EnoPush/enopush
* EnoPush/EnoPush.jar

Now make sure you have some time, an **Internet connection** (you have to use Ethernet first unfortunately) and the super-user rights (aka sudo).

Before launching the script, you may want to modify the **$installDir** variable (but you should keep it in a subfolder of */usr*) in **install.sh**.

You can now execute the installation script: install.sh with the command:
> sudo ./install.sh

Follow the steps of the installation script. Be ready to answer some questions and configurations. Keep in mind that:
* Choosing "yes" to the question of security is necessary only if the Raspberry Pi is configured to be accessible from the Internet (like a server and not a client if there is only the gateway service).
* You will face (roughly) the following file: 

		lib_path=/usr/lib/jni  
		devices_file=devices.enp  
		default_serial=/dev/ttyAMA0  
		api_path=http\://localhost/api  
		api_token=hj1456bsdg1bfsg846bg1sb125gfd  
	
	* The lib_path should not be modified unless you know what you do
	* The devices file could be anything (but if you change it, the learned devices will be forgotten if it's not properly moved)
	* The default serial is the serial port used if nothing is given in args (see EnoPush usage below)
	* API path is the URL to the platform API you would like to use to send the collected data
	* API token is a token used to authenticate on the server
	
Congratulations! You should now have a ready-to-launch EnOcean gateway!  
Make sure you reboot, the gateway should start at the next boot.

**Note:** by default the program and its configuration are stored in */usr/local/bin/enopush*. You can stop the bootlaunch with:
> sudo rm /etc/init.d/enopush

## Using EnoPush
EnoPush is a Java 7 software running on JRE 7 (openjre-7 if you used install.sh).  
It is necessary to install the external library RXTX (installed in install.sh) to access to the serial port.

EnoPush has been tested with a EnOceanPi (an antenna installed on the Raspberry Pi GPIOs) but should work also with USB300 EnOcean USB key aswell.

To run it simply use the following command while being in the EnoPush folder:  
> java -jar EnoPush.jar  

Note: if you used install.sh EnoPush should be already running if you rebooted. Don't run several instances of it because of the serial port. You can stop it by using */usr/local/bin/enopush/stop.sh* (being root).

If you want to execute EnoPush on a different serial port than the default one, just specify it in program arguments:  
> java -jar EnoPush.jar serial_port  

To view how to manage the sensors and add them in EnoPush, see the [sensors decription](./sensors.md "Sensors").
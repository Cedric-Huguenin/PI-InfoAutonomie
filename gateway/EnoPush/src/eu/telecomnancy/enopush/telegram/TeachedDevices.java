package eu.telecomnancy.enopush.telegram;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA50205;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.RadioChoice;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.radio.userdata.UserData1BS;
import eu.telecomnancy.enopush.Main;
import eu.telecomnancy.enopush.Settings;
import eu.telecomnancy.enopush.aleonceanext.RemoteDeviceEEPA50701;
import eu.telecomnancy.enopush.aleonceanext.RemoteDeviceEEPD50001;

/**
 * The class in charge of memorizing the sensors learned by the software.
 * It provides data persistence to avoid having to put the sensors in TeachIn mode every
 * time we start the gateway.
 * @author Mickael
 *
 */
public class TeachedDevices {

	/**
	 * The Map of the devices saved.
	 */
	private static HashMap<String,Device> learntDevices = new HashMap<>();
	
	/**
	 * Adds a new device to the list and saves it on the hard drive. If the device already exists and equals the new one
	 * nothing is done.
	 * @param radioPacket the radioPacket received. It will be determined if it is a TeachIn packet. If it is not, it will be ignored. 
	 */
	public static void addDevice(RadioPacket radioPacket) {
		switch(radioPacket.getChoice()) {
		case RadioChoice.RORG_1BS:
			UserData1BS userData1 = new UserData1BS(radioPacket.getData());
			if(userData1.isTeachIn()) {
				if(radioPacket.getData()[1] == 0 && radioPacket.getData()[2] == 1) {
					RemoteDeviceEEPD50001 device = new RemoteDeviceEEPD50001(Main.serialConnection,
							radioPacket.getSenderId(),
							radioPacket.getSenderId());
					put(radioPacket.getSenderId().toString(), device);
				}
			}
			break;
		case RadioChoice.RORG_4BS:
			if(((radioPacket.getData()[4] & 0x08) >> 3) == 0) {
				int func,type;
				func = (radioPacket.getData()[1] & 0xfc) >> 2;
				type = ((radioPacket.getData()[1] & 0x03) << 5) + ((radioPacket.getData()[2] & 0xf8) >> 3);
				System.out.println("Func : "+ func + " Type : " + type);
				if(func == 0x02 && type == 0x05) {
					RemoteDeviceEEPA50205 device = new RemoteDeviceEEPA50205(Main.serialConnection,
							radioPacket.getSenderId(),
							radioPacket.getSenderId());
					put(radioPacket.getSenderId().toString(), device);
				}
				if(func == 0x07 && type == 0x01) {
					RemoteDeviceEEPA50701 device = new RemoteDeviceEEPA50701(Main.serialConnection,
							radioPacket.getSenderId(),
							radioPacket.getSenderId());
					put(radioPacket.getSenderId().toString(), device);
				}
			}
			break;
		case RadioChoice.RORG_ADT:
			break;
		case RadioChoice.RORG_MSC:
			break;
		case RadioChoice.RORG_RPS:
			break;
		case RadioChoice.RORG_UTE:
			break;
		case RadioChoice.RORG_VLD:
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * Puts the specified device in the learned devices map. No verification is done.
	 * @param key the key of the Entry in the Map (typically the EnOcean device address).
	 * @param device the device to add or replace.
	 */
	public static void put(String key, Device device) {
		learntDevices.put(key, device);
		saveDevices();
		DataManager.sendDevices();
	}
	
	/**
	 * Returns the devices list as a string.
	 * @return a string describing the devices saved.
	 */
	public static String devicesToString() {
		return learntDevices.toString();
	}
	
	/**
	 * Saves the list on the file system. The list is serialized.
	 */
	public static void saveDevices() {
	    ObjectOutputStream oos = null;

	    try {
	        final FileOutputStream file = new FileOutputStream(Settings.getProperty("devices_file"));
	        oos = new ObjectOutputStream(file);
	        oos.writeObject(learntDevices);

	        oos.flush();
	    } catch (final java.io.IOException e) {
	    	e.printStackTrace();
	    } finally {
	    	try {
		    	if (oos != null) {
		    	    oos.flush();
		    	    oos.close();
		      	}
	    	} catch (final IOException ex) {
	    		ex.printStackTrace();
	    	}
	    }
	}
	
	/**
	 * Loads the list from the file system.
	 * @return the number of elements of the list loaded.
	 */
	public static int loadDevices() {
	    ObjectInputStream ois = null;

	    try {
	        final FileInputStream file = new FileInputStream(Settings.getProperty("devices_file"));
	        ois = new ObjectInputStream(file);
	        
	        @SuppressWarnings("unchecked")
			HashMap<String,Device> temp = (HashMap<String,Device>) ois.readObject();
	        if(temp != null && temp.size() != 0) {
	        	learntDevices = temp;
	        	return learntDevices.size();
	        }

	    } catch (final IOException e) {
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
	    	try {
		    	if (ois != null) {
		    	    ois.close();
		      	}
	    	} catch (final IOException ex) {
	    		ex.printStackTrace();
	    	}
	    }
	    return 0;
	}

	/**
	 * Returns if the map contains the device identified by the given id. 
	 * @param id the EnOcean ID of the device searched.
	 * @return true if the device is part of the map, false otherwise.
	 */
	public static boolean containsDevices(EnOceanId id) {
		return learntDevices.containsKey(id.toString());
	}

	/**
	 * Returns the specified device.
	 * @param senderId the EnOcean ID of the desired device.
	 * @return the desired device or null if not found.
	 */
	public static Device getDevice(EnOceanId senderId) {
		if(containsDevices(senderId))
			return learntDevices.get(senderId.toString());
		return null;
	}

	/**
	 * Returns the map containing all devices.
	 * @return the devices map.
	 */
	public static HashMap<String,Device> getDevicesMap() {
		return learntDevices;
	}
}

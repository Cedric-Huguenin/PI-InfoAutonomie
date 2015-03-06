package eu.telecomnancy.enopush.telegram;

import java.util.ArrayList;

import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.packet.RadioChoice;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.radio.userdata.UserData1BS;

public class TeachedDevices {

	private static ArrayList<Device> learntDevices = new ArrayList<>();
	
	public static void addDevice(RadioPacket radioPacket) {
		switch(radioPacket.getChoice()) {
		case RadioChoice.RORG_1BS:
			UserData1BS userData = new UserData1BS(radioPacket.getData());
			if(userData.isTeachIn()) {
				
			}
			break;
		case RadioChoice.RORG_4BS:
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

}

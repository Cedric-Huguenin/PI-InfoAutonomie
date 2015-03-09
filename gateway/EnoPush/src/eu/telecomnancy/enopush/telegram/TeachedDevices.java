package eu.telecomnancy.enopush.telegram;

import java.util.HashMap;

import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA50205;
import eu.aleon.aleoncean.packet.RadioChoice;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.radio.userdata.UserData1BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;
import eu.telecomnancy.enopush.Main;
import eu.telecomnancy.enopush.aleonceanext.RemoteDeviceEEPA50701;
import eu.telecomnancy.enopush.aleonceanext.RemoteDeviceEEPD50001;

public class TeachedDevices {

	private static HashMap<String,Device> learntDevices = new HashMap<>();
	
	public static void addDevice(RadioPacket radioPacket) {
		switch(radioPacket.getChoice()) {
		case RadioChoice.RORG_1BS:
			UserData1BS userData1 = new UserData1BS(radioPacket.getData());
			if(userData1.isTeachIn()) {
				if(radioPacket.getData()[1] == 0 && radioPacket.getData()[2] == 1) {
					RemoteDeviceEEPD50001 device = new RemoteDeviceEEPD50001(Main.serialConnection,
							radioPacket.getSenderId(),
							radioPacket.getSenderId());
					learntDevices.put(radioPacket.getSenderId().toString(), device);
				}
			}
			break;
		case RadioChoice.RORG_4BS:
			UserData4BS userData4 = new UserData4BS(radioPacket.getData());
			if(userData4.isTeachIn()) {
				int func,type;
				func = (radioPacket.getData()[1] & 0xfc) >> 2;
				type = ((radioPacket.getData()[1] & 0x03) << 5) + ((radioPacket.getData()[2] & 0xf8) >> 3);
				System.out.println("Func : "+ func + " Type : " + type);
				if(func == 0x02 && type == 0x05) {
					RemoteDeviceEEPA50205 device = new RemoteDeviceEEPA50205(Main.serialConnection,
							radioPacket.getSenderId(),
							radioPacket.getSenderId());
					learntDevices.put(radioPacket.getSenderId().toString(), device);
				}
				if(func == 0x07 && type == 0x01) {
					RemoteDeviceEEPA50701 device = new RemoteDeviceEEPA50701(Main.serialConnection,
							radioPacket.getSenderId(),
							radioPacket.getSenderId());
					learntDevices.put(radioPacket.getSenderId().toString(), device);
				}
			}
			System.out.println(userData4.toString());
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
	
	public static String devicesToString() {
		return learntDevices.toString();
	}

}

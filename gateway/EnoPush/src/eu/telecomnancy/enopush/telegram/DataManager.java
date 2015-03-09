package eu.telecomnancy.enopush.telegram;

import java.util.Date;
import java.util.Set;

import org.json.simple.JSONObject;

import eu.aleon.aleoncean.device.Device;
import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;
import eu.aleon.aleoncean.packet.RadioChoice;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.radio.RadioPacket1BS;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.RadioPacketADT;
import eu.aleon.aleoncean.packet.radio.RadioPacketMSC;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS;
import eu.aleon.aleoncean.packet.radio.RadioPacketUTE;
import eu.aleon.aleoncean.packet.radio.RadioPacketVLD;

public class DataManager {
	public enum DataPurpose {
		DATA,
		SENSOR
	}
	
	
	public static final String platformAddress = "http://vps91071.ovh.net/api/";

	@SuppressWarnings("unchecked")
	public static void process(RadioPacket radioPacket) {
		if(TeachedDevices.containsDevices(radioPacket.getSenderId())) {
			Device device = TeachedDevices.getDevice(radioPacket.getSenderId());
			RadioPacket packetAdapted = radioPacket;
			switch(radioPacket.getChoice()) {
			case RadioChoice.RORG_1BS:
				packetAdapted = new RadioPacket1BS();
				break;
			case RadioChoice.RORG_4BS:
				packetAdapted = new RadioPacket4BS();
				break;
			case RadioChoice.RORG_ADT:
				packetAdapted = new RadioPacketADT();
				break;
			case RadioChoice.RORG_MSC:
				packetAdapted = new RadioPacketMSC();
				break;
			case RadioChoice.RORG_RPS:
				packetAdapted = new RadioPacketRPS();
				break;
			case RadioChoice.RORG_UTE:
				packetAdapted = new RadioPacketUTE();
				break;
			case RadioChoice.RORG_VLD:
				packetAdapted = new RadioPacketVLD();
				break;
			default:
				break;
			}
			
			packetAdapted.setData(radioPacket.getData());
			packetAdapted.setOptionalData(radioPacket.getOptionalData());
			
			device.parseRadioPacket(packetAdapted);
			
			
			Set<DeviceParameter> parameters = device.getParameters();
			
			for(DeviceParameter param : parameters) {
				Object valueObtained = null;
				
				JSONObject dataTransfert = new JSONObject();
				String type = "";
				dataTransfert.put("timestamp", (Long) new Date().getTime()/1000);
				
				try {
					valueObtained = device.getByParameter(param);
				} catch (IllegalDeviceParameterException e) {
				}
				
				switch(param) {
				case BUTTON_DIM_A:
					break;
				case BUTTON_DIM_B:
					break;
				case ENERGY_WS:
					break;
				case HUMIDITY_PERCENT:
					dataTransfert.put("value", (Double) valueObtained);
					dataTransfert.put("label", "humidity");
					type = "HUMIDITY";
					break;
				case ILLUMINATION_LUX:
					break;
				case MOTION:
					dataTransfert.put("value", (Boolean) valueObtained);
					dataTransfert.put("label", "presence");
					type = "PRESENCE";
					break;
				case OCCUPANCY_BUTTON:
					break;
				case POSITION_PERCENT:
					break;
				case POWER_W:
					break;
				case SETPOINT_POSITION_PERCENT:
					break;
				case SETPOINT_TEMPERATURE_CELSIUS:
					break;
				case SUPPLY_VOLTAGE_V:
					// dataTransfert.put("supply_voltage", (Double) valueObtained);
					break;
				case SWITCH:
					dataTransfert.put("value", (Boolean) valueObtained);
					dataTransfert.put("label", "door");
					type = "DOOR";
					break;
				case TEMPERATURE_CELSIUS:
					dataTransfert.put("value", (Double) valueObtained);
					dataTransfert.put("label", "temperature");
					type = "TEMP";
					break;
				case TEMPERATURE_CONTROL_CUR_TEMP:
					break;
				case TEMPERATURE_CONTROL_ENABLE:
					break;
				case TMP_RECV_ACTUATOR_OBSTRUCTED:
					break;
				case TMP_RECV_CHANGE_BATTERY:
					break;
				case TMP_RECV_COVER_OPEN:
					break;
				case TMP_RECV_ENERGY_INPUT_ENABLED:
					break;
				case TMP_RECV_ENERGY_STORAGE_SUFFICIENT:
					break;
				case TMP_RECV_SERVICE_ON:
					break;
				case TMP_RECV_TEMPERATURE_SENSOR_FAILURE:
					break;
				case TMP_RECV_WINDOW_OPEN:
					break;
				case TMP_SEND_CONFIGURATION:
					break;
				case TMP_SEND_LIFT_SET:
					break;
				case TMP_SEND_REDUCED_ENERGY_CONSUMPTION:
					break;
				case TMP_SEND_RUN_INIT_SEQUENCE:
					break;
				case TMP_SEND_VALVE_CLOSED:
					break;
				case TMP_SEND_VALVE_OPEN:
					break;
				case WINDOW_HANDLE_POSITION:
					break;
				default:
					break;
				}
				dataTransfert.put("mote", packetAdapted.getSenderId().toString()+"."+type);
				if(!type.equals(""))
					sendData(dataTransfert.toJSONString(), DataPurpose.DATA);
			}
		}
	}
	
	public static void sendData(String jsonData, DataPurpose purpose) {
		System.out.println(jsonData);
	}

}

package eu.telecomnancy.enopush.telegram;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import eu.telecomnancy.enopush.Settings;


/**
 * Class in charge of the data processing to send it to the main platform.
 * @author Mickael
 *
 */
public class DataManager {
	/**
	 * The purpose of the sent data.
	 * @author Mickael
	 *
	 */
	public enum DataPurpose {
		/**
		 * This is data retrieved from sensors.
		 */
		DATA,
		/**
		 * This is sensor information.
		 */
		SENSOR
	}
	
	/**
	 * The logger to do runtime logs.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

	/**
	 * Processes the received data to adapt it to the platform and sends it.
	 * @param radioPacket
	 */
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
					double temp = 0;
					if((Boolean) valueObtained)
						temp = 1;
					dataTransfert.put("value", temp);
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
					double temp2 = 0;
					if((Boolean) valueObtained)
						temp2 = 1;
					dataTransfert.put("value", temp2);
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
	
	/**
	 * Formats and sends the list of devices to the main platform.
	 */
	@SuppressWarnings("unchecked")
	public static void sendDevices() {
		for(Entry<String, Device> entry : TeachedDevices.getDevicesMap().entrySet()) {
			Set<DeviceParameter> parameters = entry.getValue().getParameters();
			
			for(DeviceParameter param : parameters) {
				JSONObject dataTransfert = new JSONObject();
				
				switch(param) {
				case BUTTON_DIM_A:
					break;
				case BUTTON_DIM_B:
					break;
				case ENERGY_WS:
					break;
				case HUMIDITY_PERCENT:
					dataTransfert.put("type", "HUMIDITY");
					break;
				case ILLUMINATION_LUX:
					break;
				case MOTION:
					dataTransfert.put("type", "PRESENCE");
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
					break;
				case SWITCH:
					dataTransfert.put("type", "DOOR");
					break;
				case TEMPERATURE_CELSIUS:
					dataTransfert.put("type", "TEMP");
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
				dataTransfert.put("address", entry.getValue().getAddressRemote().toString());
				if(dataTransfert.containsKey("type"))
					sendData(dataTransfert.toJSONString(), DataPurpose.SENSOR);
			}
		}
	}
	
	/**
	 * Sends data via HTTP or HTTPS to the platform.
	 * @param jsonData the data to be sent.
	 * @param purpose sensor information or physical measurement.
	 */
	public static void sendData(final String jsonData, final DataPurpose purpose) {
		System.out.println(jsonData);
	    
	    Thread newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				URL url;
			    HttpURLConnection connection = null;
			    try {
			        //Create connection
			    	switch(purpose) {
					case DATA:
						url = new URL(Settings.getProperty("api_path")+"/data");
						break;
					case SENSOR:
						url = new URL(Settings.getProperty("api_path")+"/sensor");
						break;
					default:
						url = new URL(Settings.getProperty("api_path")+"/data");
						break;
			    	
			    	}
			    	
			    	System.out.println(url.toString());
			        
			        connection = (HttpURLConnection)url.openConnection();
			        connection.setRequestMethod("POST");
			        connection.setRequestProperty("Content-Type","application/json");
			        connection.setRequestProperty("AUTH", Settings.getProperty("api_token"));
			
			        connection.setUseCaches (false);
			        connection.setDoInput(true);
			        connection.setDoOutput(true);
			
			        //Send request
			        DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
			        wr.writeBytes(jsonData);
			        wr.flush ();
			        wr.close ();
			        
			        if(connection.getResponseCode() >= 200 && connection.getResponseCode() <= 400) {
			        	//Get Response    
				        InputStream is = connection.getInputStream();
				        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				        String line;
				        
				        StringBuffer response = new StringBuffer(); 
				        while((line = rd.readLine()) != null) {
				        	response.append(line);
				        	response.append('\n');
				        }
				        rd.close();
			        } else {
			        	if(purpose == DataPurpose.DATA)
			        		LOGGER.info("Something went wrong with the server");
			        }
			   } catch (IOException e) {
				   LOGGER.warn("Failed to send HTTP message. Verify server information.");
				   e.printStackTrace();
		       } finally {
			       if(connection != null) {
			    	   connection.disconnect(); 
			       }
		       }
			}
	    	
	    });
	    newThread.start();
	}
}

package eu.telecomnancy.enopush.aleonceanext;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.DeviceParameterUpdatedInitiation;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;
import eu.aleon.aleoncean.device.RemoteDevice;
import eu.aleon.aleoncean.device.StandardDevice;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 * This class represents an A5-07-01 EnOcean device corresponding to a presence sensor with voltage information.
 * @author Mickael
 *
 */
public class RemoteDeviceEEPA50701 extends StandardDevice implements RemoteDevice {

	/**
	 * Serial UID for the serialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The logger to do runtime logs.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPA50701.class);
	/**
	 * The voltage of the power supply.
	 */
	private double supplyVoltage;
	/**
	 * The value of the PIR (presence) sensor. Someone is present when this value is above 128.
	 */
	private char pirStatus;
	
	/**
	 * Creates a new A5-07-01 device.
	 * @param conn the serial port from which the radio packet comes.
	 * @param addressRemote the address of the remote device.
	 * @param addressLocal ?
	 */
	public RemoteDeviceEEPA50701(ESP3Connector conn, EnOceanId addressRemote,
			EnOceanId addressLocal) {
		super(conn, addressRemote, addressLocal);
	}

	@Override
	public void parseRadioPacket(RadioPacket packet) {
		if (packet instanceof RadioPacket4BS) {
            parseRadioPacket4BS((RadioPacket4BS) packet);
        } else {
            LOGGER.warn("Don't know how to handle radio choice {}", String.format("0x%02X", packet.getChoice()));
        }
	}

	/**
	 * Parses a 4BS radio packet, interprets it as A5-07-01 data and stores it.
	 * @param radioPacket the radio packet to parse.
	 */
	public void parseRadioPacket4BS(RadioPacket4BS radioPacket) {
		if (radioPacket.isTeachIn()) {
            LOGGER.debug("Ignore teach-in packets.");
            return;
        }
		
		final UserDataEEPA50701 userData = new UserDataEEPA50701(radioPacket.getUserDataRaw());
		
		try {
			setSupplyVoltage(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getSupplyVoltage());
		} catch (UserDataScaleValueException e) {
			e.printStackTrace();
		}
		setPirStatus(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getPIRValue());
	}

	@Override
	protected void fillParameters(Set<DeviceParameter> params) {
		params.add(DeviceParameter.SUPPLY_VOLTAGE_V);
		params.add(DeviceParameter.MOTION);

	}

	/**
	 * Returns the supply voltage in volts.
	 * @return the supply voltage in volts.
	 */
	public double getSupplyVoltage() {
		return supplyVoltage;
	}

	/**
	 * Updates the supply voltage.
	 * @param initiation the origin of the change.
	 * @param supplyVoltage the new voltage in volts.
	 */
	public void setSupplyVoltage(DeviceParameterUpdatedInitiation initiation, double supplyVoltage) {
		final double oldState = this.supplyVoltage;
		if(oldState != supplyVoltage) {
	        this.supplyVoltage = supplyVoltage;
	        fireParameterChanged(DeviceParameter.SUPPLY_VOLTAGE_V, initiation, oldState, supplyVoltage);
		}
	}

	/**
	 * Returns if the presence sensor detects somebody.
	 * @return true if someone is there, false otherwise.
	 */
	public boolean getPirStatus() {
		return pirStatus >= 128;
	}

	/**
	 * Updates the presence status.
	 * @param initiation the origin of the change.
	 * @param pirStatus the new value.
	 */
	public void setPirStatus(DeviceParameterUpdatedInitiation initiation, char pirStatus) {
		final double oldState = this.pirStatus;
        this.pirStatus = pirStatus;
        fireParameterChanged(DeviceParameter.MOTION, initiation, oldState, pirStatus);
	}
	
	@Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case SUPPLY_VOLTAGE_V:
                return getSupplyVoltage();
            case MOTION:
            	return getPirStatus();
            default:
                return super.getByParameter(parameter);
        }
    }
}

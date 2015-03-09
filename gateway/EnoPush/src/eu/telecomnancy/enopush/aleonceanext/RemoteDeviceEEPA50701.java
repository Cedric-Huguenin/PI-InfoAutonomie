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

public class RemoteDeviceEEPA50701 extends StandardDevice implements RemoteDevice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPA50701.class);
	private double supplyVoltage;
	private char pirStatus;
	
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

	public double getSupplyVoltage() {
		return supplyVoltage;
	}

	public void setSupplyVoltage(DeviceParameterUpdatedInitiation initiation, double supplyVoltage) {
		final double oldState = this.supplyVoltage;
        this.supplyVoltage = supplyVoltage;
        fireParameterChanged(DeviceParameter.SUPPLY_VOLTAGE_V, initiation, oldState, supplyVoltage);
	}

	public boolean getPirStatus() {
		return pirStatus >= 128;
	}

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

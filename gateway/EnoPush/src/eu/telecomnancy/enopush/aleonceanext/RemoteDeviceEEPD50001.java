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
import eu.aleon.aleoncean.packet.radio.RadioPacket1BS;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 * Represents a D5-00-01 EnOcean device (contact sensor/switch).
 * @author Mickael
 *
 */
public class RemoteDeviceEEPD50001 extends StandardDevice implements RemoteDevice  {
	/**
	 * The serial version UID for the serialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The logger to do runtime logs.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPD50001.class);
	/**
	 * The state of the contact.
	 */
	private boolean switchState;

	/**
	 * Creates a new D5-00-01 EnOcean device. 
	 * @param conn the serial interface from which the device comes.
	 * @param addressRemote the address of the EnOcean devce.
	 * @param addressLocal ?
	 */
	public RemoteDeviceEEPD50001(ESP3Connector conn, EnOceanId addressRemote,
			EnOceanId addressLocal) {
		super(conn, addressRemote, addressLocal);
	}

	/**
	 * Parses a 1BS radio packet, interpret its data as D5-00-01 and stores it.
	 * @param radioPacket the input 1BS radio packet.
	 */
	public void parseRadioPacket1BS(RadioPacket1BS radioPacket) {
		if (radioPacket.isTeachIn()) {
            LOGGER.debug("Ignore teach-in packets.");
            return;
        }
		
		setSwitch(DeviceParameterUpdatedInitiation.RADIO_PACKET, (radioPacket.getData()[1] & 0x01) == 1);
	}
	
	/**
	 * Updates the state of the contact.
	 * @param initiation the origin of the change.
	 * @param contactClosed the new state of the contact.
	 */
	public void setSwitch(DeviceParameterUpdatedInitiation initiation,
			boolean contactClosed) {
		final boolean oldState = this.switchState;
        this.switchState = contactClosed;
        fireParameterChanged(DeviceParameter.SWITCH, initiation, new Boolean(oldState), new Boolean(contactClosed));
		
	}
	
	/**
	 * Returns the state of the contact.
	 * @return true if closed, false otherwise.
	 */
	public boolean getSwitch() {
		return switchState;
	}

	@Override
    public void parseRadioPacket(final RadioPacket packet) {
        if (packet instanceof RadioPacket1BS) {
            parseRadioPacket1BS((RadioPacket1BS) packet);
        } else {
            LOGGER.warn("Don't know how to handle radio choice {}", String.format("0x%02X", packet.getChoice()));
        }
    }

	@Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.SWITCH);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case SWITCH:
                return new Boolean(getSwitch());
            default:
                return super.getByParameter(parameter);
        }
    }

    @Override
    public void setByParameter(final DeviceParameter parameter, final Object value) throws IllegalDeviceParameterException {
        assert DeviceParameter.getSupportedClass(parameter).isAssignableFrom(value.getClass());
        super.setByParameter(parameter, value);
    }

}

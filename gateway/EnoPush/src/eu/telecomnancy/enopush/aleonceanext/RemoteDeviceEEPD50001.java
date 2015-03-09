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
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPD50001;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

public class RemoteDeviceEEPD50001 extends StandardDevice implements RemoteDevice  {
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPD50001.class);
	private boolean switchState;

	public RemoteDeviceEEPD50001(ESP3Connector conn, EnOceanId addressRemote,
			EnOceanId addressLocal) {
		super(conn, addressRemote, addressLocal);
	}

	public void parseRadioPacket1BS(RadioPacket1BS radioPacket) {
		if (radioPacket.isTeachIn()) {
            LOGGER.debug("Ignore teach-in packets.");
            return;
        }
		
		final UserDataEEPD50001 userData = new UserDataEEPD50001(radioPacket.getUserDataRaw());
		
		setSwitch(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isContactClosed());
	}
	
	public void setSwitch(DeviceParameterUpdatedInitiation initiation,
			boolean contactClosed) {
		final boolean oldState = this.switchState;
        this.switchState = contactClosed;
        fireParameterChanged(DeviceParameter.SWITCH, initiation, oldState, contactClosed);
		
	}
	
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
                return getSwitch();
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

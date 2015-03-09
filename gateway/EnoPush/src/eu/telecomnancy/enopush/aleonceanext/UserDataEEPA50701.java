package eu.telecomnancy.enopush.aleonceanext;

import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.values.Unit;

public class UserDataEEPA50701 extends UserData4BS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final long SUPPLY_VOLTAGE_RANGE_MIN = 0;
    public static final long SUPPLY_VOLTAGE_RANGE_MAX = 250;
    public static final double SUPPLY_VOLTAGE_SCALE_MIN = 0;
    public static final double SUPPLY_VOLTAGE_SCALE_MAX = 5;
    public static final Unit SUPPLY_VOLTAGE_UNIT = Unit.VOLTAGE;

    public UserDataEEPA50701(final byte[] eepData) {
        super(eepData);
    }

    public double getSupplyVoltage() throws UserDataScaleValueException {
    	// TODO handle error codes
    	if(getDataBit(0,0) == 1)
    		return getScaleValue(3, 7, 3, 0, SUPPLY_VOLTAGE_RANGE_MIN, SUPPLY_VOLTAGE_RANGE_MAX, SUPPLY_VOLTAGE_SCALE_MIN, SUPPLY_VOLTAGE_SCALE_MAX);
    	else
    		return 0;
    }

    public Unit getSupplyVoltageUnit() {
        return SUPPLY_VOLTAGE_UNIT;
    }

    public boolean getPIRState(){
        return ((char) getDb(1)) >= 128;
    }
    
    public char getPIRValue() {
    	return (char) getDb(1);
    }

    public boolean isPIRStatusOn() {
        return getDataBit(0, 7) == 1;
    }

}

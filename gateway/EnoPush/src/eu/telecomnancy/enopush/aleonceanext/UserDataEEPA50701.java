package eu.telecomnancy.enopush.aleonceanext;

import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.values.Unit;

/**
 * Data container for A5-07-01 EnOcean device.
 * @author Mickael
 *
 */
public class UserDataEEPA50701 extends UserData4BS {

	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Minimum raw value of the supply voltage.
	 */
	public static final long SUPPLY_VOLTAGE_RANGE_MIN = 0;
	/**
	 * Maximum raw value of the supply voltage.
	 */
    public static final long SUPPLY_VOLTAGE_RANGE_MAX = 250;
    /**
     * Minimum scaled value of the supply voltage.
     */
    public static final double SUPPLY_VOLTAGE_SCALE_MIN = 0;
    /**
     * Maximum scaled value of the supply voltage.
     */
    public static final double SUPPLY_VOLTAGE_SCALE_MAX = 5;
    /**
     * Unit of the voltage measure.
     */
    public static final Unit SUPPLY_VOLTAGE_UNIT = Unit.VOLTAGE;

    /**
     * Create the data container with the given data.
     * @param eepData the raw data from radio packet.
     */
    public UserDataEEPA50701(final byte[] eepData) {
        super(eepData);
    }

    /**
     * Return the supply voltage.
     * @return the value scaled of the supply voltage.
     * @throws UserDataScaleValueException if the value is out of bounds.
     */
    public double getSupplyVoltage() throws UserDataScaleValueException {
    	// TODO handle error codes
    	if(getDataBit(0,0) == 1)
    		return getScaleValue(3, 7, 3, 0, SUPPLY_VOLTAGE_RANGE_MIN, SUPPLY_VOLTAGE_RANGE_MAX, SUPPLY_VOLTAGE_SCALE_MIN, SUPPLY_VOLTAGE_SCALE_MAX);
    	else
    		return 0;
    }

    /**
     * Returns the voltage unit.
     * @return the voltage unit.
     */
    public Unit getSupplyVoltageUnit() {
        return SUPPLY_VOLTAGE_UNIT;
    }

    /**
     * Returns if there is someone of not.
     * @return true if someone is present, false otherwise.
     */
    public boolean getPIRState(){
        return ((char) getDb(1)) >= 128;
    }
    
    /**
     * Returns the raw value of the PIR.
     * @return a value representing the PIR measure.
     */
    public char getPIRValue() {
    	return (char) getDb(1);
    }

    /**
     * Returns if the PIR is given or not.
     * @return true if given, false otherwise.
     */
    public boolean isPIRStatusOn() {
        return getDataBit(0, 7) == 1;
    }

}

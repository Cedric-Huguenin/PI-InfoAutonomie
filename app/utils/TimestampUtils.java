package utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ced on 26/01/2015.
 */
public class TimestampUtils {
	public static String timestampToString(long timestamp) {
		Calendar cal= GregorianCalendar.getInstance();
		timestamp *= 1000;
		cal.setTimeInMillis(timestamp);
		return cal.get(GregorianCalendar.YEAR) + "/" + (cal.get(GregorianCalendar.MONTH)+1) + "/" + cal.get(GregorianCalendar.DAY_OF_MONTH) + " " + cal.get(GregorianCalendar.HOUR) + ":" + cal.get(GregorianCalendar.MINUTE) + ":" + cal.get(GregorianCalendar.SECOND);
	}
}

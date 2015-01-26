package utils;

import java.text.SimpleDateFormat;
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
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return cal.get(GregorianCalendar.YEAR) + "-" + (cal.get(GregorianCalendar.MONTH)+1) + "-" + cal.get(GregorianCalendar.DAY_OF_MONTH) + " " + sdf.format(cal.getTime());
	}
}

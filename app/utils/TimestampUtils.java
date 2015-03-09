package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This is an utility class to handle timestamps.
 * Created by Ced on 26/01/2015.
 */
public class TimestampUtils {

	/**
	 * Convert a timestamp in a readable format for a human in the format : YYYY/mm/dd HH:mm:ss
	 * @param timestamp the timestamp to be converted.
	 * @return the human friendly date as mentioned in the description.
	 */
	public static String timestampToString(long timestamp) {
		Calendar cal= GregorianCalendar.getInstance();
		timestamp *= 1000;
		cal.setTimeInMillis(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return cal.get(GregorianCalendar.YEAR) + "-" + (cal.get(GregorianCalendar.MONTH)+1) + "-" + cal.get(GregorianCalendar.DAY_OF_MONTH) + " " + sdf.format(cal.getTime());
	}

	/**
	 * Take a timestamp, and turn it into a string with the specified format.
	 * @param timestamp the given unix timestamp
	 * @param datePattern the date pattern (for example "dd-MM-yyyy HH:mm:SS")
	 * @return the formatted date as a string
	 */
	public static String formatToString(long timestamp, String datePattern) {
		return (new Timestamp(timestamp*1000).toLocalDateTime()).format(DateTimeFormatter.ofPattern(datePattern));
	}
}

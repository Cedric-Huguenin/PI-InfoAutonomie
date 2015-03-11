package eu.telecomnancy.enopush;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Global configuration of EnoPush
 * @author Mickael
 *
 */
public class Settings {

	private static Properties props = null;
	
	public static void init() {
		File configFile = new File("config.properties");
		
		props = new Properties();
		 
		FileReader reader;
		try {
			reader = new FileReader(configFile);
			props.load(reader);
			reader.close();
		} catch (IOException e) {
		}
		
		if(props.size() == 0) {
			initDefaultValues();
		}
	}

	private static void initDefaultValues() {
		props = new Properties();
		
		props.setProperty("api_path", "http://localhost/api");
		props.setProperty("api_token", "hj1456bsdg1bfsg846bg1sb125gfd");
		props.setProperty("lib_path", "/usr/lib/jni");
		props.setProperty("default_serial", "/dev/ttyAMA0");
		props.setProperty("devices_file", "devices.enp");
		saveProperties();
	}
	
	public static void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	public static void saveProperties() {
		try {
			FileOutputStream out = new FileOutputStream("config.properties");
			props.store(out, "---EnoPush properties---");
			out.close();
		} catch (IOException e) {
		}	
	}

	public static String getProperty(String key) {
		if(props == null)
			init();
		if(props.containsKey(key))
			return props.getProperty(key);
		return "";
	}
}

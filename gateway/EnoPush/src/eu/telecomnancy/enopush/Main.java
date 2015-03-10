package eu.telecomnancy.enopush;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.aleon.aleoncean.packet.ESP3Packet;
import eu.aleon.aleoncean.packet.PacketType;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.rxtx.ReaderShutdownException;
import eu.aleon.aleoncean.rxtx.USB300;
import eu.telecomnancy.enopush.telegram.DataManager;
import eu.telecomnancy.enopush.telegram.TeachedDevices;

/**
 * Main class running the Gateway.
 * @author Mickael
 *
 */
public class Main {
	/**
	 * Logger to use to write logs while running.
	 */
	private static final Logger log = Logger.getLogger( Main.class.getName() );
	/**
	 * Timeout in seconds to wait a radio packet on the interface. This freezes the main thread.
	 */
	private static final long TIMEOUT = 2;
	/**
	 * The serial interface.
	 */
	public static ESP3Connector serialConnection = new USB300();

	/**
	 * Runs the program.
	 * @param args you have to provide in args on which serial interface you would like to connect (example : /dev/ttyAMA0).
	 * Make sure the serial interface provided is available and not in use by the system.
	 */
	public static void main(String[] args) {
		
		if(args.length < 1) {
			System.out.println("Incorrect arguments number. You must specify the serial port to listen.");
			System.exit(1);
		}
		
		System.setProperty("gnu.io.rxtx.SerialPorts", args[0]);
		System.setProperty("java.library.path", "/usr/lib/jni");
		
		if(!serialConnection.connect(args[0])) {
			log.log(Level.SEVERE, "Impossible to connect to the specified serial interface");
			System.exit(1);
		}
		log.log(Level.INFO, "Connected to the serial interface " + args[0]);
		
		if(TeachedDevices.loadDevices() > 0) {
			log.log(Level.INFO, "Loaded some devices from file: \n" + TeachedDevices.devicesToString());
			DataManager.sendDevices();
		}
		
		while(true) {
			try {
				ESP3Packet packet = serialConnection.read(TIMEOUT, TimeUnit.SECONDS);
				if(packet != null) {
					// System.out.println(packet.toString());
					
					if(packet.getPacketType() == PacketType.RADIO) {
						RadioPacket radioPacket = new RadioPacket(packet.getData()[0]);
						radioPacket.setData(packet.getData());
						radioPacket.setOptionalData(packet.getOptionalData());
						DataManager.process(radioPacket);
						TeachedDevices.addDevice(radioPacket);
					}
				}
			} catch (ReaderShutdownException e) {
				e.printStackTrace();
			}
		}
		
		// TODO exit properly when CTRL+C
		// serialConnection.disconnect();

	}

}

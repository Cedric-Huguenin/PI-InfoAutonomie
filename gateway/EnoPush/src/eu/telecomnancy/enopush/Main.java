package eu.telecomnancy.enopush;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.aleon.aleoncean.packet.ESP3Packet;
import eu.aleon.aleoncean.packet.PacketType;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.rxtx.ReaderShutdownException;
import eu.aleon.aleoncean.rxtx.USB300;
import eu.telecomnancy.enopush.telegram.TeachedDevices;

public class Main {
	private static final Logger log = Logger.getLogger( Main.class.getName() );
	private static final long TIMEOUT = 2;

	public static void main(String[] args) {
		
		if(args.length < 1) {
			System.out.println("Incorrect arguments number. You must specify the serial port to listen.");
			System.exit(1);
		}
		
		System.setProperty("gnu.io.rxtx.SerialPorts", args[0]);
		System.setProperty("java.library.path", "/usr/lib/jni");
		
		USB300 serialConnection = new USB300();
		
		if(!serialConnection.connect(args[0])) {
			log.log(Level.SEVERE, "Impossible to connect to the specified serial interface");
			System.exit(1);
		}
		log.log(Level.INFO, "Connected to the serial interface " + args[0]);
		
		while(true) {
			try {
				ESP3Packet packet = serialConnection.read(TIMEOUT, TimeUnit.SECONDS);
				if(packet != null) {
					System.out.println(packet.toString());
					
					if(packet.getPacketType() == PacketType.RADIO) {
						RadioPacket radioPacket = new RadioPacket(packet.getData()[0]);
						radioPacket.setData(packet.getData());
						radioPacket.setOptionalData(packet.getOptionalData());
						TeachedDevices.addDevice(radioPacket);
					}
				}
			} catch (ReaderShutdownException e) {
				e.printStackTrace();
			}
		}
		
		// serialConnection.disconnect();

	}

}

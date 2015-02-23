package radiusserver.se.nexus.interview.radius;

import java.io.*;
import java.net.*;

public class Main {

	static int defaultServerPort = 1812;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int serverPort = defaultServerPort;
		
		// TODO: Parse command line arguments
		try {
			DatagramSocket serverSocket = new DatagramSocket(serverPort);
			
			byte[] buffer = new byte[65536];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			System.out.println("Waiting for packets");
			
			while (true) {
				serverSocket.receive(packet);
				System.out.println("Received packet!");
				System.out.println("First byte: "+buffer[0]);
				
				RadiusPackage radiusPackage = new RadiusPackage(buffer, buffer.length);
				System.out.println("Code: "+radiusPackage.code);
				System.out.println("identifier: "+radiusPackage.identifier);
				System.out.println("Length: "+radiusPackage.length);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

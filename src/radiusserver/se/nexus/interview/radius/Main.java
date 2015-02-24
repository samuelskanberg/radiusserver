package radiusserver.se.nexus.interview.radius;

import java.io.*;
import java.net.*;

import radiusserver.se.nexus.interview.radius.RadiusPackage.Code;

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
				
				try {
					serverSocket.receive(packet);
					System.out.println("Received packet! Length: "+packet.getLength());

					for (int i = 0; i < packet.getLength(); i++) {
						System.out.println("["+i+"] = "+buffer[i]);
					}

					RadiusPackage radiusPackage = new RadiusPackage(buffer, packet.getLength());
					System.out.println("Code: "+radiusPackage.code);
					System.out.println("identifier: "+radiusPackage.identifier);
					System.out.println("Length: "+radiusPackage.length);
					System.out.println("Authenticator: "+radiusPackage.authenticator.toString());
					System.out.println("Attribute list");
					for (Attribute attribute : radiusPackage.attributes) {
						System.out.println("Attribute type: "+attribute.type);
						System.out.println("Attribute length: "+attribute.length);
						System.out.println("Attribute value: "+attribute.toString());
					}
					
					// If not exceptions have been thrown here, the package is good, at least in format
					RadiusResponsePackage responsePackage = new RadiusResponsePackage(radiusPackage);
					
				} catch (SilentlyIgnoreException e) {
					System.out.println("Ignoring package: "+e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
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

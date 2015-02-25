package radiusserver.se.nexus.interview.radius;

import java.io.*;
import java.net.*;

import radiusserver.se.nexus.interview.radius.RadiusPackage.Code;

public class Main {

	static int defaultServerPort = 1812;
	static String defaultSecret = "1";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int serverPort = defaultServerPort;
		Model.getModel().setSecret(defaultSecret);
		
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-port")) {
					i++;
					serverPort = Integer.parseInt(args[i]);
				} else if (args[i].equals("-secret")) {
					i++;
					String newSecret = args[i];
					Model.getModel().setSecret(newSecret);
				} else {
					// TODO: Create specific exception
					throw new Exception("No such argument");
				}
			}	
		} catch (Exception e) {
			System.err.println("Usage: java Main.class [-port <port>] [-secret <secret>]");
			System.exit(1);
		}
		
		try {
			DatagramSocket serverSocket = new DatagramSocket(serverPort);
			
			byte[] buffer = new byte[65536];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			System.out.println("Waiting for packets");
			
			while (true) {
				
				try {
					serverSocket.receive(packet);
					System.out.println("Received packet! Length: "+packet.getLength());
					System.out.println("----------------------------");

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
					
					// Respond
					int sendingPort = packet.getPort();
					InetAddress sendingAddress = packet.getAddress();
					
					byte []responsePackageData = responsePackage.toByteArray();
					DatagramPacket response = new DatagramPacket(responsePackageData, responsePackageData.length, sendingAddress, sendingPort);
					
					System.out.println("Responding to "+sendingAddress.getHostAddress()+":"+sendingPort+" as response to id: "+radiusPackage.identifier);
					System.out.println("-------------------------");
					
					serverSocket.send(response);
					
				} catch (SilentlyIgnoreException e) {
					System.out.println("Ignoring package: "+e.getMessage());
				} catch (NotImplemented e) {
					System.out.println("Not implemented: "+e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

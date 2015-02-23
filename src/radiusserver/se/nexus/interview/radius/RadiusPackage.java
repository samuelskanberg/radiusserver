package radiusserver.se.nexus.interview.radius;

import java.util.ArrayList;

public class RadiusPackage {
	byte code;
	byte identifier;
	short length;
	byte authenticator[] = new byte[16];
	ArrayList<Attribute> attributes;
	
	public RadiusPackage(byte buffer[], int length) {
		this.code = buffer[0];
		this.identifier = buffer[1];
		
		// The short spans over two bytes
		this.length = buffer[2];
		this.length |= buffer[3];
	}
}

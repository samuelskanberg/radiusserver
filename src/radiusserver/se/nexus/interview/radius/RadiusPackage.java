package radiusserver.se.nexus.interview.radius;

import java.nio.ByteBuffer;
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
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		this.length = byteBuffer.getShort(2);
	}
}

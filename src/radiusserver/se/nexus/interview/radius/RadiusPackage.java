package radiusserver.se.nexus.interview.radius;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RadiusPackage {
	byte code;
	byte identifier;
	short length;
	Authenticator authenticator;
	ArrayList<Attribute> attributes;
	
	public RadiusPackage(byte buffer[], int length) {
		this.code = buffer[0];
		this.identifier = buffer[1];
		
		// The short spans over two bytes
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		this.length = byteBuffer.getShort(2);
		
		byte authenticatorBuf[] = new byte[16];
		for (int i = 0; i < 16; i++) {
			authenticatorBuf[i] = buffer[4+i];
		}
		
		switch (this.code) {
		case Code.AccessRequest:
			authenticator = new RequestAuthenticator(authenticatorBuf);
			break;
		case Code.AccessAccept:
		case Code.AccessReject:
		case Code.AccessChallenge:
			authenticator = new ResponseAuthenticator(authenticatorBuf);
			break;
		default:
			// TODO: Not respond at all?
			break;
		}
		
		
	}
	
	public static class Code {
		public static final byte AccessRequest = 1;
		public static final byte AccessAccept = 2;
		public static final byte AccessReject = 3;
		public static final byte AccountingRequest = 4;
		public static final byte AccountingResponse = 5;
		public static final byte AccessChallenge = 11;
		public static final byte StatusServer = 12;
		public static final byte StatusClient = 13;
	}
}

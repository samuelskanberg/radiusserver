package radiusserver.se.nexus.interview.radius;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RadiusPackage {
	byte code;
	byte identifier;
	short length;
	Authenticator authenticator;
	ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
	public RadiusPackage(byte buffer[], int length) throws SilentlyIgnoreException, Exception {
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
			throw new SilentlyIgnoreException("Unknown code");
		}
		
		// If the packet is shorter than the Length field indicates, it MUST be silently discarded.
		if (length < this.length) {
			throw new SilentlyIgnoreException("Packet is shorter than indicated");
		}
		
		// After 20 bytes, everything is attributes
		int i = 20;
		while (i < this.length) {
			byte attributeType = buffer[i];
			byte attributeLength = buffer[i+1];
			if (i + attributeLength > this.length) {
				// Should or must send an Access-Reject due to bad length
				// TODO: Send Access-Reject
				throw new Exception("Bad attribute length");
			}
			
			byte attributeValue[] = new byte[attributeLength-2];
			
			for (int j = 0; j < attributeLength-2; j++) {
				attributeValue[j] = buffer[i+2+j];
			}
			
			// Set the index to next attribute
			i = i+attributeLength;
			
			Attribute attribute = new Attribute(attributeType, attributeLength, new String(attributeValue));
		
			this.attributes.add(attribute);
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

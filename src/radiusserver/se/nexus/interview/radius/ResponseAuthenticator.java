package radiusserver.se.nexus.interview.radius;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ResponseAuthenticator extends Authenticator {

	public ResponseAuthenticator(byte[] authenticatorBuf) {
		super(authenticatorBuf);
	}

	public ResponseAuthenticator(byte code, byte identifier, short length,
			Authenticator authenticator, ArrayList<Attribute> attributes,
			String secret) {
		int totalAttributesLength = 0;
		for (Attribute attribute : attributes) {
			totalAttributesLength += attribute.toByteArray().length;
		}
		byte secretBytes[] = secret.getBytes();
		
		byte []concatinatedFields = new byte[1+1+2+16+totalAttributesLength+secretBytes.length];
		concatinatedFields[0] = code;
		concatinatedFields[1] = identifier;
		// Most significant octet first
		concatinatedFields[2] = (byte)(length & 0xFF00);
		concatinatedFields[3] = (byte)(length & 0x00FF);
		
		System.arraycopy(authenticator.data, 0, concatinatedFields, 4, 16);
		
		int i = 20;
		for (Attribute attribute : attributes) {
			byte attributeBytes[] = attribute.toByteArray();
			System.arraycopy(attributeBytes, 0, concatinatedFields, i, attributeBytes.length);
			i+= attributeBytes.length;
		}
		
		System.arraycopy(secretBytes, 0, concatinatedFields, i, secretBytes.length);
		
		
		// Do digest

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			byte []md5OverAll = digest.digest(concatinatedFields);
			this.data = md5OverAll;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

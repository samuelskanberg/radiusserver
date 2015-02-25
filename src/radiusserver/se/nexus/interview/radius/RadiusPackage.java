package radiusserver.se.nexus.interview.radius;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class RadiusPackage {
	byte code;
	byte identifier;
	short length;
	Authenticator authenticator;
	ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
	public RadiusPackage() {
		
	}
	
	public RadiusPackage(byte buffer[], int length) throws SilentlyIgnoreException, NotImplemented, Exception {
		this.code = buffer[0];
		this.identifier = buffer[1];
		
		// The short spans over two bytes
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		this.length = byteBuffer.getShort(2);
		
		byte authenticatorBuf[] = new byte[16];
		for (int i = 0; i < 16; i++) {
			authenticatorBuf[i] = buffer[4+i];
		}
		
		authenticator = Authenticator.getAuthenticatorFromFactory(this.code, authenticatorBuf);
		
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
			
			// Will also make sure that the while loop won't be eternal
			if (attributeLength < 2) {
				throw new Exception("Bad attribute length, too small");
			}
			
			byte attributeValue[] = new byte[attributeLength-2];
			
			for (int j = 0; j < attributeLength-2; j++) {
				attributeValue[j] = buffer[i+2+j];
			}
			
			// Set the index to next attribute
			i = i+attributeLength;
			
			Attribute attribute = Attribute.getAttributeFromFactory(attributeType, attributeLength, attributeValue);
		
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

	public String getUserName() throws UserNameNotFound {
		for (Attribute attribute : this.attributes) {
			if (attribute instanceof UserNameAttribute) {
				return attribute.toString();
			}
		}
		
		throw new UserNameNotFound();
	}

	public boolean hasCorrectPassword(String password) throws UserPasswordNotFound, NotImplemented {
		for (Attribute attribute : this.attributes) {
			if (attribute instanceof UserPasswordAttribute) {
				// Reverse the process 
				//String password = "frans123!";
				byte RA[] = this.authenticator.data;
				String secret = Model.getModel().getSecret();
				
				byte generatedPassword[] = encryptPassword(password, RA, secret);
								
				// Compare password attribute with generated password
				return Arrays.equals(attribute.data, generatedPassword);
			}
		}
		
		throw new UserPasswordNotFound();
	}
	
	public byte[] encryptPassword(String password, byte[] RA, String secret) throws NotImplemented {
		if (password.getBytes().length > 16) {
			throw new NotImplemented("Longer password not yet implemented");
		}
		
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			byte passwordBytes[] = password.getBytes();
			byte paddedPasswordBytes[] = new byte[16];
			System.arraycopy(passwordBytes, 0, paddedPasswordBytes, 0, passwordBytes.length);
			
			byte secretBytes[] = secret.getBytes();
			
			byte secretAndRA[] = new byte[secretBytes.length+RA.length];
			System.arraycopy(secretBytes, 0, secretAndRA, 0, secretBytes.length);
			System.arraycopy(RA, 0, secretAndRA, secretBytes.length, RA.length);
			
			byte md5OnSecretAndRA[] = digest.digest(secretAndRA);
			
			byte c1[] = new byte[16];
			for (int i = 0; i < 16; i++) {
				c1[i] = (byte)(md5OnSecretAndRA[i] ^ paddedPasswordBytes[i]);
			}
			
			return c1;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public void calculateLength() {
		// code, identifier and authenticator is 20 octets
		int length = 20;
		for (Attribute attribute : this.attributes) {
			length += 2;
			length += attribute.data.length;
		}
		
		this.length = (byte)length;
	}

	public void setAuthenticator(byte[] data) throws SilentlyIgnoreException {
		authenticator = Authenticator.getAuthenticatorFromFactory(this.code, data);
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	public byte[] toByteArray() {
		// Code, identifier, length and authenticator
		int length = 20;
		
		// Loop through attributes
		for (Attribute attribute : this.attributes) {
			byte attributeBytes[] = attribute.toByteArray();
			length += attributeBytes.length;
		}
		
		byte data[] = new byte[length];
		
		data[0] = this.code;
		data[1] = this.identifier;
		// Most significant octet first
		data[2] = (byte)(this.length & 0xFF00);
		data[3] = (byte)(this.length & 0x00FF);
		
		// Copy authenticator data
		System.arraycopy(this.authenticator.data, 0, data, 4, this.authenticator.data.length);
		
		int i = 20;
		// Copy all attributes
		for (Attribute attribute : this.attributes) {
			byte attributeBytes[] = attribute.toByteArray();
			System.arraycopy(attributeBytes, 0, data, i, attributeBytes.length);
			i+= attributeBytes.length;
		}
		
		return data;
	}
}

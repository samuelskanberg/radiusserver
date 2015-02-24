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

	public boolean hasCorrectPassword(String password) throws UserPasswordNotFound {
		for (Attribute attribute : this.attributes) {
			if (attribute instanceof UserPasswordAttribute) {
				// Reverse the process 
				//String password = "frans123!";
				byte RA[] = this.authenticator.data;
				String secret = "1";
				
				byte generatedPassword[] = encryptPassword(password, RA, secret);
				
				System.out.print("Encrypted password: ");
				for (byte b : generatedPassword) {
					System.out.print(b+", ");
				}
				System.out.println("");
				
				// Compare password attribute with generated password
				System.out.println("Attribute data.length: "+attribute.data.length);
				System.out.println("Generated password.length: "+generatedPassword.length);
				return Arrays.equals(attribute.data, generatedPassword);
			}
		}
		
		throw new UserPasswordNotFound();
	}
	
	public byte[] encryptPassword(String password, byte[] RA, String secret) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			byte passwordBytes[] = password.getBytes();
			byte paddedPasswordBytes[] = new byte[16];
			System.arraycopy(passwordBytes, 0, paddedPasswordBytes, 0, passwordBytes.length);
			System.out.print("Password bytes: ");
			for (byte b : passwordBytes) {
				System.out.print(b+", ");
			}
			System.out.println("");
			
			System.out.print("Padded password bytes: ");
			for (byte b : paddedPasswordBytes) {
				System.out.print(b+", ");
			}
			System.out.println("");
			

			byte secretBytes[] = secret.getBytes();
			
			byte secretAndRA[] = new byte[secretBytes.length+RA.length];
			System.arraycopy(secretBytes, 0, secretAndRA, 0, secretBytes.length);
			System.arraycopy(RA, 0, secretAndRA, secretBytes.length, RA.length);
			
			System.out.print("Secret bytes: ");
			for (byte b : secretBytes) {
				System.out.print(b+", ");
			}
			System.out.println("");
			
			System.out.print("RA: ");
			for (byte b : RA) {
				System.out.print(b+", ");
			}
			System.out.println("");
			
			System.out.print("SecretAndRA: ");
			for (byte b : secretAndRA) {
				System.out.print(b+", ");
			}
			System.out.println("");
			
			byte md5OnSecretAndRA[] = digest.digest(secretAndRA);
			System.out.print("MD5 on secretAndRA: ");
			for (byte b : md5OnSecretAndRA) {
				System.out.print(b+", ");
			}
			System.out.println("");
			
			byte c1[] = new byte[16];
			for (int i = 0; i < 16; i++) {
				c1[i] = (byte)(md5OnSecretAndRA[i] ^ paddedPasswordBytes[i]);
			}
			
			System.out.print("c1: ");
			for (byte b : c1) {
				System.out.print(b+", ");
			}
			System.out.println("");
			
			return c1;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
}

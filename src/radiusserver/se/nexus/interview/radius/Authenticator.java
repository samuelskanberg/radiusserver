package radiusserver.se.nexus.interview.radius;

import radiusserver.se.nexus.interview.radius.RadiusPackage.Code;

public class Authenticator {
	byte data[] = new byte[16];
	
	public Authenticator() {
		
	}
	
	public Authenticator(byte[] authenticatorBuf) {
		this.data = authenticatorBuf;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			builder.append(data[i]+", ");
		}
		return builder.toString();
	}

	public static Authenticator getAuthenticatorFromFactory(byte code,
			byte[] authenticatorBuf) throws SilentlyIgnoreException {
		switch (code) {
		case Code.AccessRequest:
			return new RequestAuthenticator(authenticatorBuf);
		case Code.AccessAccept:
		case Code.AccessReject:
		case Code.AccessChallenge:
			return new ResponseAuthenticator(authenticatorBuf);
		default:
			// TODO: Not respond at all?
			throw new SilentlyIgnoreException("Unknown code: "+code);
		}
		
	}
}

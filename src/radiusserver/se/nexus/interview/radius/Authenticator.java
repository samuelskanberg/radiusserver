package radiusserver.se.nexus.interview.radius;

public class Authenticator {
	byte data[] = new byte[16];
	
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
}

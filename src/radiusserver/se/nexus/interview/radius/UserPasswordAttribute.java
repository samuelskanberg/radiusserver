package radiusserver.se.nexus.interview.radius;

public class UserPasswordAttribute extends Attribute {

	public UserPasswordAttribute(byte type, byte length, String stringField) {
		super(type, length, stringField);
	}
	
	public String toString() {
		char chars[] = this.stringField.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			builder.append((byte)chars[i]+",");
		}
		return builder.toString();
	}

}

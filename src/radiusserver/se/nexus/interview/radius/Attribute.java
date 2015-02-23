package radiusserver.se.nexus.interview.radius;

public class Attribute {
	byte type;
	byte length;
	String stringField;
	
	public Attribute(byte type, byte length, String stringField) {
		this.type = type;
		this.length = length;
		this.stringField = stringField;
	}
}

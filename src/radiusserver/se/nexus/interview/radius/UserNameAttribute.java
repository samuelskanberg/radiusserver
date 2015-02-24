package radiusserver.se.nexus.interview.radius;

public class UserNameAttribute extends Attribute {

	public UserNameAttribute(byte type, byte length, byte[] data) {
		super(type, length, data);
	}
	
	public UserNameAttribute(String userName) {
		this.type = Type.UserName;
		this.data = userName.getBytes();
		this.length = (byte)(this.data.length+2);
	}

	public String toString() {
		return new String(data);
	}
}

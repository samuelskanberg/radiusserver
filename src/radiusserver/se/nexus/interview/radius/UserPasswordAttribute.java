package radiusserver.se.nexus.interview.radius;

public class UserPasswordAttribute extends Attribute {

	public UserPasswordAttribute(byte type, byte length, byte[] data) {
		super(type, length, data);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.data.length; i++) {
			builder.append(this.data[i]+",");
		}
		return builder.toString();
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getType() {
		return "UserPassword";
	}

}

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
	
	public static Attribute getAttributeFromFactory(byte type, byte length, String stringField) throws NotImplemented {
		switch (type) {
		case Type.UserName:
			return new UserNameAttribute(type, length, stringField);
		case Type.UserPassword:
			return new UserPasswordAttribute(type, length, stringField);
		default:
			throw new NotImplemented("Type not implemented: "+type);
		}
	}
	
	public String toString() {
		return this.stringField;
	}
	
	public static class Type {
		public static final byte UserName = 1;
		public static final byte UserPassword = 2;
		public static final byte CHAPPassword = 3;
		public static final byte NASIPAddress = 4;
		public static final byte NASPort = 5;
		public static final byte ServiceType = 6;
		public static final byte FramedProtocol = 7;
		public static final byte FramedIPAddress = 8;
		public static final byte FramedIPNetmask = 9;
		public static final byte FramedRouting = 10;
		public static final byte FilterId = 11;
		public static final byte FramedMTU = 12;
		public static final byte FramedCompression = 13;
		public static final byte LoginIPHost = 14;
		public static final byte LoginService = 15;
		public static final byte LoginTCPPort = 16;
		// 17 unassigned
		public static final byte ReplyMessage = 18;
		public static final byte CallbackNumber = 19;
		public static final byte CallbackId = 20;
		// 21 unassigned
		public static final byte FramedRoute = 22;
		public static final byte FramedIPXNetwork = 23;
		public static final byte State = 24;
		public static final byte Class = 25;
		public static final byte VendorSpecific = 26;
		public static final byte SessionTimeout = 27;
		public static final byte IdleTimeout = 28;
		public static final byte TerminationAction = 29;
		public static final byte CalledStationId = 30;
		public static final byte CallingStationId = 31;
		public static final byte NASIdentifier = 32;
		public static final byte ProxyState = 33;
		public static final byte LoginLATService = 34;
		public static final byte LoginLATNode = 35;
		public static final byte LoginLATGroup = 36;
		public static final byte FramedAppleTalkLink = 37;
		public static final byte FramedAppleTalkNetwork = 38;
		public static final byte FramedAppleTalkZone = 39;
		// 40 reserver for accounting
		public static final byte CHAPChallenge = 60;
		public static final byte NASPortType = 61;
		public static final byte PortLimit = 62;
		public static final byte LoginLATPort = 63;
	}
}

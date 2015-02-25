package radiusserver.se.nexus.interview.radius;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.jws.WebParam.Mode;

import radiusserver.se.nexus.interview.radius.RadiusPackage.Code;

public class RadiusResponsePackage extends RadiusPackage {

	public RadiusResponsePackage() {
		
	}

	public RadiusResponsePackage(RadiusPackage radiusPackage) {
		switch (radiusPackage.code) {
		case Code.AccessRequest:
			// Check that the username and password match
			System.out.println("An access request");
			try {
				this.identifier = radiusPackage.identifier;
				
				String userName = radiusPackage.getUserName();
				
				System.out.println("Username: "+userName);
				String password = Model.getModel().getPassword(userName);
				boolean correctPassword = radiusPackage.hasCorrectPassword(password);
				
				System.out.println("Password correct: "+correctPassword);
				
				// We won't add any attributes so the size is the minimum
				calculateLength();
				
				if (correctPassword) {
					this.code = Code.AccessAccept;
				} else {
					this.code = Code.AccessReject;
				}
				
				this.authenticator = new ResponseAuthenticator(this.code, radiusPackage.identifier, this.length, radiusPackage.authenticator, this.attributes, Model.getModel().getSecret());
				
			} catch (UserNameNotFound e) {
				System.out.println("Username not found!");
				this.code = Code.AccessReject;
				calculateLength();
				this.authenticator = new ResponseAuthenticator(this.code, radiusPackage.identifier, this.length, radiusPackage.authenticator, this.attributes, Model.getModel().getSecret());
			} catch (UserPasswordNotFound e) {
				System.out.println("Userpassword is not found!");
				this.code = Code.AccessReject;
				calculateLength();
				this.authenticator = new ResponseAuthenticator(this.code, radiusPackage.identifier, this.length, radiusPackage.authenticator, this.attributes, Model.getModel().getSecret());
			} catch (NotImplemented e) {
				System.out.println("Not yet implemented: "+e.getMessage());
				e.printStackTrace();
			}
			
			break;
			
		default:
				
			break;
		}
		
	}
}

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
				
				if (correctPassword) {
					this.code = Code.AccessAccept;
					this.authenticator = new ResponseAuthenticator(this.code, radiusPackage.identifier, radiusPackage.authenticator, radiusPackage.attributes, Model.getModel().getSecret());
				} else {
					this.code = Code.AccessReject;
					this.authenticator = new ResponseAuthenticator(this.code, radiusPackage.identifier, radiusPackage.authenticator, radiusPackage.attributes, Model.getModel().getSecret());
				}
				
				
			} catch (UserNameNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserPasswordNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
		default:
				
			break;
		}
		
	}
}

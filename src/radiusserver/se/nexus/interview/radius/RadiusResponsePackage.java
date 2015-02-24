package radiusserver.se.nexus.interview.radius;

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
				String userName = radiusPackage.getUserName();
				
				System.out.println("Username: "+userName);
				String password = Model.getModel().getPassword(userName);
				boolean correctPassword = radiusPackage.hasCorrectPassword(password);
				
				System.out.println("Password correct: "+correctPassword);
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

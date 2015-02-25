package radiusserver.se.nexus.interview.radius;

import java.util.HashMap;

public class Model {
	private static Model model;
	
	private HashMap<String, String> userNameAndPasswords;
	private String secret;
	
	private Model() {
		userNameAndPasswords = new HashMap<String, String>();
		
		// These should exists
		// "frans1"/"fran123!"
		// "frans2"/"fran123!""
		
		// Should maybe be stored in md5sum format
		userNameAndPasswords.put("frans1", "fran123!");
		userNameAndPasswords.put("frans2", "fran123!\"");
	}
	
	public String getPassword(String userName) {
		return userNameAndPasswords.get(userName);
	}
	
	// Singleton access to the model
	public static Model getModel() {
		if (Model.model == null) {
			Model.model = new Model();
		}
		return Model.model;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return this.secret;
	}
}

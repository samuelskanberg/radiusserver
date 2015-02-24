package radiusserver.se.nexus.interview.radius;

import java.util.HashMap;

public class Model {
	private static Model model;
	
	private HashMap<String, String> userNameAndPasswords;
	
	private Model() {
		userNameAndPasswords = new HashMap<String, String>();
		
		// These should exists
		// "frans1"/"fran123!"
		// "frans2"/"fran123!""
		
		// Should maybe be stored in md5sum format
		userNameAndPasswords.put("frans1", "frans123!");
		userNameAndPasswords.put("frans2", "frans123!\"");
	}
	
	// Singleton access to the model
	public static Model getModel() {
		if (Model.model == null) {
			Model.model = new Model();
		}
		return Model.model;
	}
}

package utilitaire;

import java.util.ResourceBundle;

public class ConfigUtil {

	private static ResourceBundle bundle=null;
	
	public static ResourceBundle getBundle(){
		if(bundle==null)
			bundle=ResourceBundle.getBundle("domaine.properties.project");
		return bundle;
	}
}

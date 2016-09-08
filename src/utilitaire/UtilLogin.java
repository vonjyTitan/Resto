package utilitaire;

public class UtilLogin {

	public static String generatePasse(){
		String reponse="";
		String ensemble="azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789-_@!?";
		int taille=ensemble.length()-1;
		for(int i=0;i<10;i++)
			reponse+=ensemble.toCharArray()[0+(int)(Math.random()*((taille-0) + 1))];
		return reponse;
	}

}

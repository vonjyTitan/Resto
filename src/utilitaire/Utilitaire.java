package utilitaire;

public class Utilitaire {
	public static <T> String concatListe(T[] liste,String separateur,String quote){
		String response="";
		boolean isFirst=true;
		for(T t:liste){
			if(!isFirst)
				response+=separateur;
			response+=quote+t+quote;
			isFirst=false;
		}
		return response;
	}
}

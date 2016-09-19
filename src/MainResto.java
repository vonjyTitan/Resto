import utilitaire.UtilCrypto;

public class MainResto {

	public static void main(String[] args) {
		try {
			String[] list=("test").split(";");
			for(String s:list){
				System.out.println(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

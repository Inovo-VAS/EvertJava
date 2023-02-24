
public class Main {

	public static void main(String[] args) {
		DBSMSGateway dbsmsGateway=new DBSMSGateway();
		new Thread(dbsmsGateway).start();
	}

	public static String defaultLocalPath(String suggestedlocalpath){
		suggestedlocalpath="D:/projects/clients/inovo/java/";
		return suggestedlocalpath;
	}
	
	public static String defaultServletContextName() {
		return "AvonDBSMSGateway";
	}
}

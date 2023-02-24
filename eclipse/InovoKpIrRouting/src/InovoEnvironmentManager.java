import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;

import presence.Administrator;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		//suggestedlocalpath="D:/projects/clients/inovo/java/";
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		try {
			inovo.db.Database.executeDBRequest(null, "KPROUTING", "SELECT GETDATE() AS TESTDATE", null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void disposeServletContext(ServletContext sc) {
		
		super.disposeServletContext(sc);
	}
}

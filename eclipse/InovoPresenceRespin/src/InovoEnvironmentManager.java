import javax.servlet.ServletContext;

import presence.Administrator;

import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		suggestedlocalpath="D:/projects/clients/inovo/java/";
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		Administrator.administrator(this._roottemppath, this.configProperty("PRESENCESERVERIP"));
	}
		
	@Override
	public void disposeServletContext(ServletContext sc) {
		super.disposeServletContext(sc);
	}
}

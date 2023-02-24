import inovo.servlet.InovoCoreEnvironmentManager;

import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager{
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
	}
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void disposeServletContext(ServletContext sc) {
		super.disposeServletContext(sc);
	}
}

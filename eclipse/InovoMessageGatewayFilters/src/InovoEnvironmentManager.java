import javax.servlet.ServletContext;

import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	public static String _currentLocalpath="";
	private boolean _enableMessagingQueue=false;
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return (_currentLocalpath=super.defaultLocalPath(suggestedlocalpath));
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		
	}
	
	@Override
	public void loadServletConfig(String defaultconfigfilename,
			boolean updateProperties) throws Exception {
		super.loadServletConfig(defaultconfigfilename, updateProperties);
	}

	@Override
	public void disposeServletContext(ServletContext sc) {
		super.disposeServletContext(sc);
	}
}

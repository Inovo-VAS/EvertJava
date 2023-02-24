import inovo.servlet.InovoCoreEnvironmentManager;
import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
  
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext servletContext)
	{
	    super.initializeServletContext(servletContext);
	}
}
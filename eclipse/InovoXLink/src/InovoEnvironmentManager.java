import inovo.servlet.InovoCoreEnvironmentManager;

import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
	
	
  public void initializeServletContext(ServletContext sc)
  {
    super.initializeServletContext(sc);
        
  }
    

  public String defaultLocalPath(String suggestedlocalpah)
  {
	  //suggestedlocalpah="D:/projects/clients/inovo/java/";
	  return super.defaultLocalPath(suggestedlocalpah);
  }

  public void disposeServletContext(ServletContext sc)
  {
	super.disposeServletContext(sc);
  }
}

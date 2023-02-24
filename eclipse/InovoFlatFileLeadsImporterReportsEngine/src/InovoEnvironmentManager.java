import inovo.db.Database;
import inovo.flat.file.leads.importer.reports.StatusReportQueue;
import inovo.servlet.InovoCoreEnvironmentManager;

import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
  public void initializeServletContext(ServletContext sc)
  {
    super.initializeServletContext(sc);
    StatusReportQueue.statusReportQueue();
  }

  public String defaultLocalPath(String suggestedlocalpath)
  {
	  //suggestedlocalpath="C:/projects/clients/inovo/java/";
	  return super.defaultLocalPath(suggestedlocalpath);
  }

  public void disposeServletContext(ServletContext sc)
  {
	 StatusReportQueue.statusReportQueue().shutdownQueue(); 
	 super.disposeServletContext(sc);
  }
}

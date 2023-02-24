import inovo.db.Database;
import inovo.flat.file.leads.importer.FlatFileImportProcessManager;
import inovo.flat.file.leads.importer.FlatFileLeadsImportQueueManager;
import inovo.servlet.InovoCoreEnvironmentManager;

import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
  public void initializeServletContext(ServletContext sc)
  {
    super.initializeServletContext(sc);   
    FlatFileImportProcessManager.flatFileImportProcessManager();
    this.log_debug("FLATFILELEADSIMPORTER STARTED");
  }

  public String defaultLocalPath(String suggestedlocalpath)
  {
	  //suggestedlocalpath="C:/projects/java/";
	  return super.defaultLocalPath(suggestedlocalpath);
  }

  public void disposeServletContext(ServletContext sc)
  {
	 this.log_debug("FLATFILELEADSIMPORTER STOPPING QUEUE");
	 //FlatFileLeadsImportQueueManager.flatFileLeadsImportQueueManager().shutdownImportQueue();
	 FlatFileImportProcessManager.shutdownFlatFileImportProcessManager();
	 this.log_debug("FLATFILELEADSIMPORTER QUEUE STOPPED");
     super.disposeServletContext(sc);
  }
}

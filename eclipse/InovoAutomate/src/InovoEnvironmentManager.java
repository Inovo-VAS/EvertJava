import inovo.automation.AutomatedTasksQueue;
import inovo.servlet.InovoCoreEnvironmentManager;
import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
  public void initializeServletContext(ServletContext sc)
  {
    super.initializeServletContext(sc);
    AutomatedTasksQueue.initAutomatedTasksQueue();
  }

  public String defaultLocalPath(String suggestedlocalpah)
  {
    return super.defaultLocalPath("C:/projects/java/");
  }

  public void disposeServletContext(ServletContext sc)
  {
	super.disposeServletContext(sc);
  }
}

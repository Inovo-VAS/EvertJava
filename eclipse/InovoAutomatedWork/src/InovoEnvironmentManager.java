import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import inovo.automation.AutomationControlQueue;
import inovo.automation.AutomationControlRequest;
import inovo.automation.db.Queue;
import inovo.automation.db.Request;
import inovo.db.Database;
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
    return super.defaultLocalPath(suggestedlocalpah);
  }

  public void disposeServletContext(ServletContext sc)
  {
	super.disposeServletContext(sc);
  }
}

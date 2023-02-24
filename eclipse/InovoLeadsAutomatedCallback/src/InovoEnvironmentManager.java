import javax.servlet.ServletContext;

import inovo.automation.scheduledsqljobs.ScheduledSqlJobsQueue;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		//suggestedlocalpath="C:/projects/java/";
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		ScheduledSqlJobsQueue.scheduledSqlJobsQueue();
	}

	@Override
	public void disposeServletContext(ServletContext sc) {
		ScheduledSqlJobsQueue.scheduledSqlJobsQueue().shutdownSqlJobsQueue();
		super.disposeServletContext(sc);
	}
}

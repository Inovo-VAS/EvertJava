import javax.servlet.ServletContext;

import inovo.presence.reports.AgentServiceStagingMonitorQueue;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void loadServletConfig(String defaultconfigfilename,
			boolean updateProperties) throws Exception {
		// TODO Auto-generated method stub
		super.loadServletConfig(defaultconfigfilename, updateProperties);
		if(this.configProperty("PRESENCESERVERIP").equals("")){
			this.setConfigProperty("PRESENCESERVERIP", "127.0.0.1:6800");
		}
		if(this.configProperty("PMCONSOLSTATSINTERVAL").equals("")){
			this.setConfigProperty("PMCONSOLSTATSINTERVAL", "20");
		}
		super.loadServletConfig(defaultconfigfilename, true);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		AgentServiceStagingMonitorQueue.agentServiceStagingMonitor(this.configProperty("PRESENCESERVERIP"),Integer.parseInt(this.configProperty("PMCONSOLSTATSINTERVAL")));
	}

	@Override
	public void disposeServletContext(ServletContext sc) {		
		super.disposeServletContext(sc);
	}
}

import javax.servlet.ServletContext;

import presence.Administrator;
import inovo.presence.agent.gateway.PresenceAgentStatusQueue;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	private PresenceAgentStatusQueue _presenceAgentStatusQueue=null;
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		_presenceAgentStatusQueue=PresenceAgentStatusQueue.activatePresenceAgentStatusQueue();
	}
		
	@Override
	public void disposeServletContext(ServletContext sc) {
		_presenceAgentStatusQueue.shutdownQueue();
		super.disposeServletContext(sc);
	}
}

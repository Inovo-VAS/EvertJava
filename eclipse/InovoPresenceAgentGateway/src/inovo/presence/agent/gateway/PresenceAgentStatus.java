package inovo.presence.agent.gateway;

import java.io.InputStream;
import java.util.HashMap;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class PresenceAgentStatus extends InovoHTMLPageWidget {

	public PresenceAgentStatus(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	public void publishAgentStatus() throws Exception{
		HashMap<String,String> presenceAgentStatusInfo=new HashMap<String,String>();
		this.importRequestParametersIntoMap(presenceAgentStatusInfo, null);
		if(presenceAgentStatusInfo.containsKey("COMMAND")) presenceAgentStatusInfo.remove("COMMAND");
		//PresenceAgentStatusQueue.publishPresenceAgentStatus(presenceAgentStatusInfo);
		this.respondString("SUCCESS");
	}
}

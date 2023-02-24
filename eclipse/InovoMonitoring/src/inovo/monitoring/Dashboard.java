package inovo.monitoring;

import java.io.InputStream;
import java.util.HashMap;

import inovo.monitoring.dashboards.MonitorDashboard;
import inovo.monitoring.monitors.Monitor;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Dashboard extends InovoHTMLPageWidget {

	public Dashboard(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		HashMap<String,HashMap<Integer,HashMap<String,Object>>> monitorsStatusInfo=new HashMap<String, HashMap<Integer,HashMap<String,Object>>>();
		Monitor.refreshMonitorsStatusInfo(monitorsStatusInfo,null);
		
		if(!monitorsStatusInfo.isEmpty()){
			for(String monitorStatusInfoKey:monitorsStatusInfo.keySet()){
				new MonitorDashboard(this, null,monitorStatusInfoKey,monitorsStatusInfo.get(monitorStatusInfoKey)).executeContentWidget();
			}
		}
	}
}

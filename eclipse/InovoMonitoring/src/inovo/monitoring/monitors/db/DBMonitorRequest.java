package inovo.monitoring.monitors.db;

import inovo.monitoring.monitors.MonitorRequest;

public class DBMonitorRequest extends MonitorRequest {
	public DBMonitor dbmonitor(){
		return (DBMonitor)this.monitor();
	}
	
	public void listApplicationConnections(String dballias){
		;
	}
}

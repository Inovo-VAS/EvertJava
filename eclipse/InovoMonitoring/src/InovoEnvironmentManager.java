import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import inovo.monitoring.monitors.Monitor;
import inovo.monitoring.monitors.MonitorRequest;
import inovo.servlet.InovoCoreEnvironmentManager;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return super.defaultLocalPath("c:/projects/java/");//suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		try {
			Monitor.initiateMonitor(Monitor.class,"MONITOR 1").initiateMonitorRequest(new MonitorRequest(){
				@Override
				public void attachMonitorRequests(
						MonitorRequest[] monitorRequests) {
					super.attachMonitorRequests(new MonitorRequest[]{new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST1");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST1:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					}});
				}
			});
			
			Monitor.initiateMonitor(Monitor.class,"MONITOR 2").initiateMonitorRequest(new MonitorRequest(){
				@Override
				public void attachMonitorRequests(
						MonitorRequest[] monitorRequests) {
					super.attachMonitorRequests(new MonitorRequest[]{new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.1");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.1:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.2");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.2:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},
					new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.3");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.1:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.3");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.2:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},
					new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.4");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.3:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.5");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.4:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},
					new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.6");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.5:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST2.7");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST2.6:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					}
					});
				}
			});
			
			Monitor.initiateMonitor(Monitor.class,"MONITOR 3").initiateMonitorRequest(new MonitorRequest(){
				@Override
				public void attachMonitorRequests(
						MonitorRequest[] monitorRequests) {
					super.attachMonitorRequests(new MonitorRequest[]{new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST3.1");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST3.1:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST3.2");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST3.2:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},
					new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST3.3");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST3.3:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST3.4");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST3.4:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},
					new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST3.5");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST3.5:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					},new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST3.6");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST3.6:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					}});
				}
			});
			Monitor.initiateMonitor(Monitor.class,"MONITOR 4").initiateMonitorRequest(new MonitorRequest(){
				@Override
				public void attachMonitorRequests(
						MonitorRequest[] monitorRequests) {
					super.attachMonitorRequests(new MonitorRequest[]{new MonitorRequest(this){
						public boolean executeMonitor(MonitorRequest parentMonitorRequest) {
							this.setStatus("NAME", "TEST4");
							this.setStatus("DATETIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
							//System.out.println("TEST4:"+ parentMonitorRequest.toString()+":"+this.toString());
							return true;
						};
					}});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void disposeServletContext(ServletContext sc) {
		Monitor.killAllMonitors();
		super.disposeServletContext(sc);
	}
}

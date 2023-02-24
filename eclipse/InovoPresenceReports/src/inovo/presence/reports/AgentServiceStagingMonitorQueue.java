package inovo.presence.reports;

import inovo.presence.pmconsol.PMConsolClient;

public class AgentServiceStagingMonitorQueue implements Runnable{

	private static AgentServiceStagingMonitorQueue _agentServiceMonotoringQueue=null;
	
	private String _presenceserverip="";
	private PMConsolClient _pmconsol=null;
	private int polingInterval=20;
	private AgentServiceStagingMonitorQueue(String presenceServerIp,int polingstatsinterval){
		presenceServerIp=(presenceServerIp.indexOf(":")==-1?presenceServerIp+":"+6800:presenceServerIp);
		this._presenceserverip=presenceServerIp;
		this._pmconsol=new PMConsolClient(_presenceserverip);
		this.polingInterval=polingstatsinterval;
		if(polingInterval<15) polingInterval=15;
	}
	private boolean _shutdownMonitor=false;
	@Override
	public void run() {
		while(!_shutdownMonitor){
			try{
				if(this._pmconsol.startStatsCollection(polingInterval)){
					
				}
				else{
					
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(polingInterval*1024);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static AgentServiceStagingMonitorQueue agentServiceStagingMonitor(String presenceserverip,int polingstatsinterval){
		if(_agentServiceMonotoringQueue==null){
			new Thread(_agentServiceMonotoringQueue=new AgentServiceStagingMonitorQueue(presenceserverip,polingstatsinterval)).start();
		}
		return _agentServiceMonotoringQueue;
	}
}

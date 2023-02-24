package inovo.monitoring.monitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Monitor implements Runnable{
	
	private static ExecutorService _monitorsServices=null;
	
	private static ExecutorService _monitorRequestServices=null;
	
	private static ArrayList<Monitor> _monitors=new ArrayList<Monitor>();
	private Monitor _monitorRef=null;
	
	private ArrayList<MonitorRequest> _monitorRequests=new ArrayList<MonitorRequest>();
	
	private HashMap<MonitorRequest,Integer> _monitorRequestStatusInfoIndexMap=new HashMap<MonitorRequest, Integer>();
	private HashMap<Integer,HashMap<String,Object>> _monitorRequestStatusInfo=new HashMap<Integer, HashMap<String,Object>>();
	
	public Monitor(){
		this._monitorRef=this;
	}
	
	private String _monitorLabel="";
	private void setMonitorLabel(String monitorLabel) {
		this._monitorLabel=monitorLabel;
	}
	public String monitorLabel(){
		return this._monitorLabel;
	}
	
	public void refreshMonitorRequestsStatusInfo(
			HashMap<Integer, HashMap<String, Object>> monitorRequestStatusInfo) {
		int monReqIndex=_monitorRequests.size();
		
		while(monReqIndex>0){
			monReqIndex--;
			monitorRequestStatusInfo.put(monReqIndex, _monitorRequests.get((Integer)monReqIndex).monitorRequestInfoExtract());
		}
	}
	
	public void requestCompleted(MonitorRequest monitorRequest) {
		if(monitorRequest.stillBusyExecuting()){
			_monitorRequestServices.execute(monitorRequest);
		}
		else{
			System.out.println("COMPLETED:"+monitorRequest.toString());
		}
	}
	
	public static Monitor initiateMonitor(Class<?> monitorClass,String monitorLabel) throws Exception{
		Monitor monitorInit=null;
		initiateMonitor(monitorInit=(Monitor)monitorClass.newInstance(),monitorLabel);
		return monitorInit;
	}
	
	public static void initiateMonitor(Monitor monitor,String monitorLabel){
		if(_monitorsServices==null){
			_monitorsServices=Executors.newCachedThreadPool();
		}
		synchronized (_monitors) {
			_monitors.add(monitor);
		}
		monitor.setMonitorLabel(monitorLabel);
		_monitorsServices.execute(monitor);
	}

	public static void killAllMonitors(){
		while(!_monitors.isEmpty()){
			synchronized (_monitors) {
				for(Monitor mon:_monitors){
					if(!mon.shuttingdownMonitor()){
						mon.shutdownMonitor();
					}
				}
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		_monitorRequestServices.shutdown();
		_monitorsServices.shutdown();
	}

	private boolean _shutdownMonitor=false;
	
	@Override
	public void run() {
		if(!this._shutdownMonitor){
			synchronized (_monitorRef) {
				try {
					this._monitorRef.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		this.shutdownRequests();
		System.out.println("MONITOR["+this.getClass().toString()+"] stopped");
		synchronized (_monitors) {
			_monitors.remove(this._monitorRef);
		}
	}
	
	public void shutdownRequests() {
		while(!_monitorRequests.isEmpty()){
			synchronized (_monitorRequests) {
				for(MonitorRequest monReq:_monitorRequests){
					if(!monReq.shuttingdownRequest()){
						monReq.shutdownRequest();
					}
				}
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean shuttingdownMonitor(){
		return this._shutdownMonitor;
	}
	
	public void shutdownMonitor(){
		this._shutdownMonitor=true;
		synchronized (_monitorRef) {
			_monitorRef.notifyAll();
		}
	}
	
	public MonitorRequest initiateMonitorRequest(MonitorRequest monitorRequest){
		
		MonitorRequest monRequest=null;
		synchronized (_monitorRef) {
			monitorRequest.attachToMonitor(_monitorRef);
		}
		monitorRequest.attachMonitorRequests((MonitorRequest[])null);
		if(_monitorRequestServices==null){
			_monitorRequestServices=Executors.newCachedThreadPool();
		}
		synchronized (_monitorRequests) {
			this._monitorRequests.add(monitorRequest);
		}
		_monitorRequestServices.execute(monRequest=monitorRequest);
		return monRequest;
	}

	public void changeMonitorRequestStatus(MonitorRequest monitorRequest,
			String field, Object value) {
		int monitorRequestInfoMapIndex=-1;
		boolean addMonitor=false;
		synchronized (this._monitorRequestStatusInfoIndexMap) {
			Object monitorRequestInfoMapIndexRef=this._monitorRequestStatusInfoIndexMap.get(monitorRequest);
			if(monitorRequestInfoMapIndexRef==null){
				addMonitor=true;
				this._monitorRequestStatusInfoIndexMap.put(monitorRequest,(Integer)(monitorRequestInfoMapIndex=this._monitorRequestStatusInfoIndexMap.size()));
			}
			else{
				monitorRequestInfoMapIndex=(Integer)monitorRequestInfoMapIndexRef;
			}
		}
		
		HashMap<String,Object> monitorRequestStatusInfo=null;
		synchronized (_monitorRequestStatusInfo) {
			if(addMonitor){
				_monitorRequestStatusInfo.put((Integer)monitorRequestInfoMapIndex, monitorRequestStatusInfo=new HashMap<String,Object>());
			}
			else{
				monitorRequestStatusInfo=_monitorRequestStatusInfo.get((Integer)monitorRequestInfoMapIndex);
			}
			monitorRequestStatusInfo.put(field, value);
			System.out.println(monitorRequestStatusInfo.toString());
		}
	}

	public static void refreshMonitorsStatusInfo(
			HashMap<String, HashMap<Integer, HashMap<String, Object>>> monitorsStatusInfo,ArrayList<String> unAllocatedMonitorLabels) {
		int monitorsIndex=_monitors.size();
		
		unAllocatedMonitorLabels=(unAllocatedMonitorLabels==null?new ArrayList<String>():unAllocatedMonitorLabels);
		
		unAllocatedMonitorLabels.clear();
		
		if(!monitorsStatusInfo.isEmpty()){
			for(String monLabel:monitorsStatusInfo.keySet()){
				unAllocatedMonitorLabels.add(monLabel);
			}
		}
		
		while(monitorsIndex>0){
			monitorsIndex--;
			String monitorLabel="";
			
			HashMap<Integer,HashMap<String,Object>> monitorRequestStatusInfo=new HashMap<Integer, HashMap<String,Object>>();
			
			synchronized (_monitors) {
					Monitor mon=_monitors.get(monitorsIndex);
					if(mon!=null){
						monitorLabel=mon.monitorLabel();
					}
					
					mon.refreshMonitorRequestsStatusInfo(monitorRequestStatusInfo);
					
			}
			
			if(monitorLabel.equals("")){
				monitorRequestStatusInfo=null;
			}
			else{
				if(unAllocatedMonitorLabels.indexOf(monitorLabel)>-1){
					unAllocatedMonitorLabels.remove(unAllocatedMonitorLabels.indexOf(monitorLabel));
					monitorsStatusInfo.get(monitorLabel).clear();
					monitorsStatusInfo.get(monitorLabel).putAll(monitorRequestStatusInfo);
				}
				else{
					monitorsStatusInfo.put(monitorLabel, monitorRequestStatusInfo);
				}
			}
		}		
		
		while(!unAllocatedMonitorLabels.isEmpty()){
			
		}
	}
}

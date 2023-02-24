package inovo.monitoring.monitors;

import java.util.ArrayList;
import java.util.HashMap;

public class MonitorRequest implements Runnable{

	private Exception _em=null;
	private boolean _busyExecuting=false; 
	
	private Monitor _monitor=null;
	private ArrayList<MonitorRequest> _monitorRequests=new ArrayList<MonitorRequest>();
	private MonitorRequest _parentMonitorRequest=null;
	private MonitorRequest _monitorRequestRef=null;
	
	private HashMap<String,Object> _monitorRequestStatusInfo=new HashMap<String,Object>();
	
	public MonitorRequest(){
		this(null);
	}
	
	public void setStatus(String field,Object value){
		if(this._monitorRequestRef!=null){
			synchronized (_monitorRequestStatusInfo) {
				this._monitorRequestStatusInfo.put(field, value);
			}
			this._monitor.changeMonitorRequestStatus(this,field,value);
		}
		else{
			this._parentMonitorRequest.setStatus(field, value);
		}
	}
	
	public HashMap<String,Object> monitorRequestInfoExtract(){
		HashMap<String,Object> monitorRequestInfoExtract=new HashMap<String, Object>();
		synchronized (_monitorRequestRef) {
			monitorRequestInfoExtract.putAll(_monitorRequestStatusInfo);
		}
		return monitorRequestInfoExtract;
	}
	
	public Monitor monitor(){
		return this._monitor;
	}
	
	public MonitorRequest(MonitorRequest parentMonitorRequest){
		if((this._parentMonitorRequest=parentMonitorRequest)!=null){
			this.attachToMonitor(_parentMonitorRequest.monitor());
		}
		else{
			this._monitorRequestRef=this;
		}
		this._busyExecuting=true;
	}
	
	public void attachToMonitor(Monitor monitor){
		this._monitor=monitor;
	}
	
	public void attachMonitorRequests(MonitorRequest[] monitorRequests){
		if(monitorRequests==null) return;
		for(MonitorRequest monreq:monitorRequests){
			this._monitorRequests.add(monreq);
		}
		monitorRequests=null;
	}
	
	public void attachMonitorRequests(ArrayList<MonitorRequest> monitorRequest){
		if(monitorRequest==null) return;
		if(!monitorRequest.isEmpty()){
			synchronized (_monitorRequests) {
				_monitorRequests.addAll(monitorRequest);
			}
		}
	}
	
	@Override
	public void run() {
		this._em=null;
		try{
			if(!this._monitorRequests.isEmpty()){
				synchronized (_monitorRequests) {
					int monitorRequestsCount=_monitorRequests.size();
					while(monitorRequestsCount>0){
						monitorRequestsCount--;
						if(!this._shutdownRequest){
							if(!_monitorRequests.get(monitorRequestsCount).executeMonitor(this)){
								_monitorRequests.remove(monitorRequestsCount);
							}
						}
						else{
							_monitorRequests.remove(monitorRequestsCount);
						}
					}
					this._busyExecuting=!this._monitorRequests.isEmpty();
				}
			}
			else{
				if(!_shutdownRequest){
					this._busyExecuting=this.executeMonitor(null);
				}
				else{
					this._busyExecuting=this._shutdownRequest;
				}
			}
		}
		catch(Exception e){
			this._em=e;
		}
		if(this._busyExecuting){
			if(this._monitorRequestRef!=null){
				synchronized (_monitorRequestRef) {
					try {
						_monitorRequestRef.wait(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.completedRequest(this);
	}
	
	public boolean stillBusyExecuting(){
		return this._busyExecuting;
	}
	
	private void completedRequest(MonitorRequest monitorRequest) {
		if(this._monitor!=null){
			this._monitor.requestCompleted(this);
		}
	}

	public boolean executeMonitor(MonitorRequest parentMonitorRequest){
		return true;
	}

	public boolean shuttingdownRequest() {
		return this._shutdownRequest;
	}

	private boolean _shutdownRequest=false;
	public void shutdownRequest() {
		this._shutdownRequest=true;
	}
}

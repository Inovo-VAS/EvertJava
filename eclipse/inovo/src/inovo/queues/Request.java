package inovo.queues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Request implements Runnable {
	private boolean _completed=false;
	private Queue _queue=null;
	private Object _requestLock=new Object();
	private Exception _reqException=null;
	
	private HashMap<String,String> _requestProperties=new HashMap<String,String>();
	private ArrayList<String> _requetsActions=new ArrayList<String>();
	
	public Request(HashMap<String,Object> requestProperties){
		if(requestProperties!=null){
			for(String propName:requestProperties.keySet()){
				this.setProperty(propName, requestProperties.get(propName).toString());
			}
		}
	}
	
	public void clearRequestActions(){
		synchronized (_requetsActions) {
			this._requetsActions.clear();
		}
	}
	
	public void setRequestActions(ArrayList<String> requestActions){
		if(requestActions!=null){
			synchronized (_requetsActions) {
				this._requetsActions.addAll(requestActions);
			}
		}
	}
	
	private int _totalRequestActions=0;
	private int _totalRequestActionsRemaining=0;
	private int _totalRequestActionsCompleted=0;
	
	public void executeActions(){
		ArrayList<String> requestActionsToExecute=new ArrayList<String>();
		synchronized (_requetsActions) {
			if(this._requetsActions!=null){
				requestActionsToExecute.addAll(_requetsActions);
			}
		}
		
		this._totalRequestActions=requestActionsToExecute.size();
		this._totalRequestActionsCompleted=0;
		this._totalRequestActionsRemaining=this._totalRequestActions;
		
		if(!requestActionsToExecute.isEmpty()){
			while(!requestActionsToExecute.isEmpty()&&!this.killingRequest()){
				this._totalRequestActionsRemaining=(this._totalRequestActions- this._totalRequestActionsCompleted++);
				this.executeAction(requestActionsToExecute.remove(0));
			}
		}
	}
	
	public int totalRequestActions(){
		return this._totalRequestActions;
	}
	
	public int totalRequestsRemaining(){
		return this._totalRequestActionsRemaining;
	}
	
	public int totalRequestActionsCompleted(){
		return this._totalRequestActionsCompleted;
	}
	
	public void executeAction(String requestAction){
	}
	
	@Override
	public void run() {
		while(!_queue._killAllQueues&&!_queue._killRequests&&!_killRequest){
			if(this.canExecute()){
				try{
					this._reqException=null;
					this.executeRequest();
				}catch(Exception re){
					this._reqException=re;
					if(this.onExecuteRequestError(re)){
						break;
					}
				}
			}
			try {
				synchronized (_requestLock) {
					_requestLock.wait(this.requestDelay());
				}
			} catch (InterruptedException e) {
				break;
			}
			if(!this.canContinue()){
				this._killRequest=true;
			}
		}
		synchronized (this) {
			this.notify();
		}
		this._completed=true;
		this._queue.detachRequest(this);
	}
	
	public boolean onExecuteRequestError(Exception exception) {
		return false;
	}

	public Queue queue(){
		return this._queue;
	}
	
	public void setProperty(String propName,String propValue){
		this._requestProperties.put(propName=propName.toUpperCase(), (propValue==null?"":propValue));
	}
	
	public String property(String propName){
		if(this._requestProperties.containsKey(propName=propName.toUpperCase())){
			return this._requestProperties.get(propName);
		}
		return "";
	}
	
	public Set<String> propertyNames(){
		return this._requestProperties.keySet();
	}
	
	private boolean _killRequest=false;
	
	public boolean killingRequest(){
		return _queue._killAllQueues||_queue._killRequests||_killRequest;
	}
	
	public void kill(){
		this._killRequest=true;
		this.notifyRequestLock();
	}
	
	public void notifyRequestLock(){
		synchronized (_requestLock) {
			this._requestLock.notify();
		}
	}
	
	public boolean canContinue() {
		return true;
	}

	protected void attachQueue(Queue queue){
		this._queue=queue;
	}
	
	public boolean completed(){
		return this._completed;
	}
	
	public void executeRequest() throws Exception{
	}
	
	public boolean canExecute(){
		return true;
	}

	private long _requestDelay=2;
	
	public void setRequestDelay(long requestDelay){
		_requestDelay=(requestDelay<2?2:requestDelay);
	}
	
	public long requestDelay(){
		return _requestDelay;
	}

	private String _requestAllias="";
	
	public String requestAllias(){
		return this._requestAllias;
	}
	
	public void setRequestAllias(String requestAllias) {
		this._requestAllias=requestAllias;
	}

}

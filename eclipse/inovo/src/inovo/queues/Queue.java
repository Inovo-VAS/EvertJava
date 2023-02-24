package inovo.queues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Queue {
	private static ArrayList<Queue> _queues=new ArrayList<Queue>();
	private static HashMap<String,Queue> _alliasedQueues=new HashMap<String,Queue>();
	protected static boolean _killAllQueues=false;
	
	private ArrayList<Request> _requests=new ArrayList<Request>();
	private HashMap<String,Request> _requestAlliasses=new HashMap<String,Request>();
	private String _queueAllias="";
	
	private HashMap<String,String> _queueProperties=new HashMap<String,String>();
	
	public Queue(String queueAllias) throws Exception{
		this._queueAllias=queueAllias.toUpperCase();
		this.initiate();
		synchronized (_queues) {
			_queues.add(this);
			if(!this._queueAllias.equals("")){
				_alliasedQueues.put(_queueAllias, this);
			}
		}
	}
	
	public String queueAllias(){
		return this._queueAllias;
	}
	
	public void setProperty(String propName,String propValue){
		this._queueProperties.put(propName=propName.toUpperCase(), (propValue==null?"":propValue));
	}
	
	public String property(String propName){
		if(this._queueProperties.containsKey(propName=propName.toUpperCase())){
			return this._queueProperties.get(propName);
		}
		return "";
	}
	
	public Set<String> propertyNames(){
		return this._queueProperties.keySet();
	}

	public void initiate() throws Exception{
	}
	
	public Request queueRequest(Request requestToQueue,boolean queueModal) throws Exception{
		return queueRequest("", requestToQueue, queueModal);
	}
	
	public boolean requestAlliasExist(String requestAllias){
		boolean requestAlliasExist=false;
		synchronized (_requestAlliasses) {
			requestAlliasExist=this._requestAlliasses.containsKey(requestAllias.toUpperCase());
		}
		return requestAlliasExist;
	}
	
	public Request request(String requestAllias){
		Request request=null;
		synchronized (_requestAlliasses) {
			if(this._requestAlliasses.containsKey(requestAllias=requestAllias.toUpperCase())){
				request=this._requestAlliasses.get(requestAllias);
			}
		}
		return request;
	}
	
	public Request newRequest(HashMap<String,Object> requestProperties,String requestTypeName,String requestClassPath,String requestPackagePath){
		return null;
	}

	public Request queueRequest(String requestAllias,Request requestToQueue,boolean queueModal) throws Exception{
		synchronized (_requests) {
			if((requestAllias=(requestAllias==null?"":requestAllias.trim())).toUpperCase().equals("")){
				_requests.add(requestToQueue);
				requestToQueue.attachQueue(this);
			}
			else{
				if(!this._requestAlliasses.containsKey(requestAllias)){
					requestToQueue.setRequestAllias(requestAllias);
					this._requestAlliasses.put(requestAllias, requestToQueue);
					_requests.add(requestToQueue);
					requestToQueue.attachQueue(this);
				}
				else{
					requestToQueue=null;
				}
			}
		}
		if(requestToQueue!=null){
			new Thread(requestToQueue).start();
			if(queueModal){
				while(!requestToQueue.completed()){
					synchronized (requestToQueue) {
						requestToQueue.wait(2);
					}
				}
			}
		}
		return requestToQueue;
	}
	
	protected void detachRequest(Request request){
		synchronized (_requests) {
			_requests.remove(request);
			if(!request.requestAllias().equals("")){
				this._requestAlliasses.remove(request.requestAllias());
			}
		}
	}
	
	public void killRequest(String requestAllias){
		Request requestToKill=this.request(requestAllias);
		if(requestToKill!=null){
			requestToKill.kill();
		}
	}
	
	protected boolean _killRequests=false;
	public void kill() {
		this._killRequests=true;
		synchronized (_requests) {
			for(Request request:this._requests){
				request.kill();
			}
		}
		while(!_requests.isEmpty()){
			synchronized (this) {
				try {
					this.wait(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		synchronized (_queues) {
			_queues.remove(this);
			if(!this._queueAllias.equals("")){
				_alliasedQueues.remove(this.queueAllias());
			}
		}
	}
	
	public static Queue queue(String queueAllias){
		Queue queue=null;
		synchronized (_alliasedQueues) {
			if(_alliasedQueues.containsKey(queueAllias=queueAllias.toUpperCase())){
				queue=_alliasedQueues.get(queueAllias.toUpperCase());
			}
		}
		return queue;
	}
	
	public static void killQueue(String queueAllias){
		Queue queueToKill=queue(queueAllias);
		if(queueToKill!=null){
			queueToKill.kill();
		}
	}
	
	public static void killAllQueues(){
		for(final Queue queue:_queues){
			new Thread(new Runnable() {
				public void run() {
					queue.kill();
				}
			}).start();
		}
		while(!_queues.isEmpty()){
			try {
				synchronized (_queues) {
					_queues.wait(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//_unQueueQueueService.shutdown();
	}
}

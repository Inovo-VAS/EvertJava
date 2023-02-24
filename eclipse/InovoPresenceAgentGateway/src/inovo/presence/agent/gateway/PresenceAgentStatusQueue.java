package inovo.presence.agent.gateway;

import java.util.ArrayList;
import java.util.HashMap;

public class PresenceAgentStatusQueue implements Runnable{
	
	
	private static PresenceAgentStatusQueue _presenceAgentStatusQueue=null;
	
	
	private ArrayList<HashMap<String,String>> _queuedPresenceAgentStatusses=new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> _presenceAgentStatussesToActivate=new ArrayList<HashMap<String,String>>();
	
	private HashMap<String,HashMap<String,String>> _activePresenceAgentStatusses=new HashMap<String,HashMap<String,String>>();
	
	private Thread _presenceAgentStatusQueueThread=null;
	public PresenceAgentStatusQueue(){
		_presenceAgentStatusQueueThread=new Thread(this);
		_presenceAgentStatusQueueThread.start();
	}

	private boolean _shutdownQueue=false;
	
	public void shutdownQueue(){
		this._shutdownQueue=true;
		_presenceAgentStatusQueue.notifyAll();
		while(!_queuedPresenceAgentStatusses.isEmpty()){
			synchronized (_presenceAgentStatusQueue) {
				try {
					_presenceAgentStatusQueue.wait(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	
	@Override
	public void run() {
		while(!_shutdownQueue){
			if(!_queuedPresenceAgentStatusses.isEmpty()){
				synchronized (_queuedPresenceAgentStatusses) {
					while(!_queuedPresenceAgentStatusses.isEmpty()){
					_presenceAgentStatussesToActivate.add(_queuedPresenceAgentStatusses.remove(0));
					}
					_queuedPresenceAgentStatusses.clear();
				}
			}
			this.movePresenceAgentStatussesToActive();
			synchronized (_presenceAgentStatusQueue) {
				try {
					_presenceAgentStatusQueue.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	private void movePresenceAgentStatussesToActive() {
		synchronized (_activePresenceAgentStatusses) {
			while(!this._presenceAgentStatussesToActivate.isEmpty()){
				HashMap<String,String> presenceAgentStatusInfo=this._presenceAgentStatussesToActivate.remove(0);
				if(presenceAgentStatusInfo.containsKey("RESET")){
					if(presenceAgentStatusInfo.get("RESET").equals("TRUE")){
						_activePresenceAgentStatusses.remove(presenceAgentStatusInfo.get("LOGIN")).clear();
						presenceAgentStatusInfo.remove("LOGIN");
						_activePresenceAgentStatusses.put(presenceAgentStatusInfo.get("LOGIN"), presenceAgentStatusInfo);
					}
					else{
						String login=presenceAgentStatusInfo.get("LOGIN");
						HashMap<String,String> activePresenceAgentStatusInfo=this._activePresenceAgentStatusses.get(login);
						if(activePresenceAgentStatusInfo!=null){
							for(String presenceAgentStatusInfoKey:presenceAgentStatusInfo.keySet()){
								if(presenceAgentStatusInfoKey.toUpperCase().equals("LOGIN")) continue;
								activePresenceAgentStatusInfo.put(presenceAgentStatusInfoKey.toUpperCase(), presenceAgentStatusInfo.get(presenceAgentStatusInfoKey));
							}
						}
					}
				}
			}
		}
		this._presenceAgentStatussesToActivate.clear();
	}


	public static PresenceAgentStatusQueue activatePresenceAgentStatusQueue(){
		if(_presenceAgentStatusQueue==null) _presenceAgentStatusQueue=new PresenceAgentStatusQueue();
		return _presenceAgentStatusQueue;
	}

	public static void publishPresenceAgentStatus(
			HashMap<String, String> presenceAgentStatusInfo) {
		_presenceAgentStatusQueue.queuePresenceAgentStatus(presenceAgentStatusInfo);
	}

	private void queuePresenceAgentStatus(
			HashMap<String, String> presenceAgentStatusInfo) {
		synchronized (_presenceAgentStatusQueue._queuedPresenceAgentStatusses) {
			_queuedPresenceAgentStatusses.add(presenceAgentStatusInfo);
		}
	}
}

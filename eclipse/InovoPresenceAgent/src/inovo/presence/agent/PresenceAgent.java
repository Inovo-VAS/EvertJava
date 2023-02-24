package inovo.presence.agent;

import com4j.Com4jObject;
import com4j.EventCookie;
import com4j.Holder;
import presence.pcoagent.ClassFactory;
import presence.pcoagent.events.IPresenceInterfaceXEvents;

public class PresenceAgent implements Runnable,IPresenceAgent{
	
	private static PresenceAgent _parentAgent=new PresenceAgent();
	
	private static EventCookie _evHandle=null;
	public static void main(String[] args) {
		presence.pcoagent.IPresenceInterfaceX presenceAgentX=null;
		 
		presenceAgentX=ClassFactory.createPresenceInterfaceX();
		_evHandle=((Com4jObject)presenceAgentX).advise(presence.pcoagent.events.IPresenceInterfaceXEvents.class,new IPresenceInterfaceXEvents() {
			@Override
			public void closeEvent() {
				_parentAgent.event_closeEvent();
			}
		});
		if(presenceAgentX.active()){
		
			_parentAgent.activeEvent();
		}
		else{
			_parentAgent._activated=false;
		}
		_parentAgent.run();
		_evHandle.close();
	}
	
	public void activeEvent(){
		
		this._activated=true;
	}

	private static Object _agentShutdownLock=new Object();
	private boolean _activated=true;
	@Override
	public void run() {
		while(_activated){
			synchronized (_agentShutdownLock) {
				try {
					_agentShutdownLock.wait();
				} catch (InterruptedException e) {
					
				}
			}
		}
		_evHandle.close();
	}

	@Override
	public void event_startEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_stopEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_newEndCodeEvent(int endCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_outboundCallEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_inboundCallEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_endContactEvent(Holder<Boolean> endContact) {
		
	}

	@Override
	public void event_recordStartedEvent(int recordId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_closeEvent() {
		this._activated=false;
		_agentShutdownLock.notifyAll();
	}

	@Override
	public void event_loginEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_logoutEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_queuedContactsEvent(int serviceId, int queuedContacts,
			String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_afterCallWorkEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_alertCallEvent(int serviceId, String phone, int vdn,
			int skill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_endAlertCallEvent(int serviceId, String phone, int vdn,
			int skill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_unexpectedLogoutEvent() {
		this._activated=false;
	}

	@Override
	public void event_lineCountEvent(int lineCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_beforeEndContactEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_endContact2Event(Holder<Boolean> endContact,
			boolean forceEndContact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_alertCall2Event() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_endAlertCall2Event() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_customButtonClickEvent(String customButtonId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallOriginatedEvent(int index, int callId,
			String ani) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallDeliveredEvent(int index, int callId,
			String ani) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallEstablishedEvent(int index, int callId,
			String ani) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallClearedEvent(int index, int callId,
			String ani) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallHeldEvent(int index, int callId, String ani) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallRetrievedEvent(int index, int callId,
			String ani) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_recordStarted2Event(int recordId, int recordType,
			int serviceId, int index, int callId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_recordPausedEvent(int recordId, int recordType,
			int serviceId, int index, int callId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_recordResumedEvent(int recordId, int recordType,
			int serviceId, int index, int callId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_recordStoppedEvent(int recordId, int recordType,
			int serviceId, int index, int callId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallOriginated2Event(int index, int callId,
			String ani, String ucid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallDelivered2Event(int index, int callId,
			String ani, String ucid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallEstablished2Event(int index, int callId,
			String ani, String ucid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallCleared2Event(int index, int callId,
			String ani, String ucid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallHeld2Event(int index, int callId,
			String ani, String ucid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void event_softphoneCallRetrieved2Event(int index, int callId,
			String ani, String ucid) {
		// TODO Auto-generated method stub
		
	};

}

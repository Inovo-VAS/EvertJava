package inovo.presence.agent;

import com4j.DISPID;
import com4j.Holder;

public interface IPresenceAgent {
	  public void  event_startEvent();


	  /**
	   */

	  public void  event_stopEvent(); 


	  /**
	   * @param endCode Mandatory int parameter.
	   */

	  public void  event_newEndCodeEvent(
	    int endCode) ;


	  /**
	   */

	  public void  event_outboundCallEvent();

	  /**
	   */

	  public void  event_inboundCallEvent();


	  /**
	   * @param endContact Mandatory Holder<Boolean> parameter.
	   */

	  public void  event_endContactEvent(
	    Holder<Boolean> endContact);

	  /**
	   * @param recordId Mandatory int parameter.
	   */

	  public void  event_recordStartedEvent(
	    int recordId);


	  /**
	   */

	  public void  event_closeEvent();


	  /**
	   */

	  public void  event_loginEvent();


	  /**
	   */

	  public void  event_logoutEvent();


	  /**
	   * @param serviceId Mandatory int parameter.
	   * @param queuedContacts Mandatory int parameter.
	   * @param serviceName Mandatory java.lang.String parameter.
	   */

	  public void  event_queuedContactsEvent(
	    int serviceId,
	    int queuedContacts,
	    java.lang.String serviceName);


	  /**
	   */

	  public void  event_afterCallWorkEvent();


	  /**
	   * @param serviceId Mandatory int parameter.
	   * @param phone Mandatory java.lang.String parameter.
	   * @param vdn Mandatory int parameter.
	   * @param skill Mandatory int parameter.
	   */

	  public void  event_alertCallEvent(
	    int serviceId,
	    java.lang.String phone,
	    int vdn,
	    int skill);


	  /**
	   * @param serviceId Mandatory int parameter.
	   * @param phone Mandatory java.lang.String parameter.
	   * @param vdn Mandatory int parameter.
	   * @param skill Mandatory int parameter.
	   */

	  public void  event_endAlertCallEvent(
	    int serviceId,
	    java.lang.String phone,
	    int vdn,
	    int skill);


	  /**
	   */

	  public void  event_unexpectedLogoutEvent();


	  /**
	   * @param lineCount Mandatory int parameter.
	   */

	  public void  event_lineCountEvent(
	    int lineCount);


	  /**
	   */

	  public void  event_beforeEndContactEvent();


	  /**
	   * @param endContact Mandatory Holder<Boolean> parameter.
	   * @param forceEndContact Mandatory boolean parameter.
	   */

	  public void  event_endContact2Event(
	    Holder<Boolean> endContact,
	    boolean forceEndContact);


	  /**
	   */

	  public void  event_alertCall2Event();


	  /**
	   */

	  public void  event_endAlertCall2Event();


	  /**
	   * @param customButtonId Mandatory java.lang.String parameter.
	   */

	  public void  event_customButtonClickEvent(
	    java.lang.String customButtonId);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallOriginatedEvent(
	    int index,
	    int callId,
	    java.lang.String ani);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallDeliveredEvent(
	    int index,
	    int callId,
	    java.lang.String ani);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallEstablishedEvent(
	    int index,
	    int callId,
	    java.lang.String ani);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallClearedEvent(
	    int index,
	    int callId,
	    java.lang.String ani);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallHeldEvent(
	    int index,
	    int callId,
	    java.lang.String ani);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallRetrievedEvent(
	    int index,
	    int callId,
	    java.lang.String ani);


	  /**
	   * @param recordId Mandatory int parameter.
	   * @param recordType Mandatory int parameter.
	   * @param serviceId Mandatory int parameter.
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   */

	  public void  event_recordStarted2Event(
	    int recordId,
	    int recordType,
	    int serviceId,
	    int index,
	    int callId);


	  /**
	   * @param recordId Mandatory int parameter.
	   * @param recordType Mandatory int parameter.
	   * @param serviceId Mandatory int parameter.
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   */

	  public void  event_recordPausedEvent(
	    int recordId,
	    int recordType,
	    int serviceId,
	    int index,
	    int callId);


	  /**
	   * @param recordId Mandatory int parameter.
	   * @param recordType Mandatory int parameter.
	   * @param serviceId Mandatory int parameter.
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   */

	  public void  event_recordResumedEvent(
	    int recordId,
	    int recordType,
	    int serviceId,
	    int index,
	    int callId);


	  /**
	   * @param recordId Mandatory int parameter.
	   * @param recordType Mandatory int parameter.
	   * @param serviceId Mandatory int parameter.
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   */

	  public void  event_recordStoppedEvent(
	    int recordId,
	    int recordType,
	    int serviceId,
	    int index,
	    int callId);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   * @param ucid Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallOriginated2Event(
	    int index,
	    int callId,
	    java.lang.String ani,
	    java.lang.String ucid);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   * @param ucid Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallDelivered2Event(
	    int index,
	    int callId,
	    java.lang.String ani,
	    java.lang.String ucid) ;


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   * @param ucid Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallEstablished2Event(
	    int index,
	    int callId,
	    java.lang.String ani,
	    java.lang.String ucid);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   * @param ucid Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallCleared2Event(
	    int index,
	    int callId,
	    java.lang.String ani,
	    java.lang.String ucid);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   * @param ucid Mandatory java.lang.String parameter.
	   */

	  public void  event_softphoneCallHeld2Event(
	    int index,
	    int callId,
	    java.lang.String ani,
	    java.lang.String ucid);


	  /**
	   * @param index Mandatory int parameter.
	   * @param callId Mandatory int parameter.
	   * @param ani Mandatory java.lang.String parameter.
	   * @param ucid Mandatory java.lang.String parameter.
	   */

	  public void event_softphoneCallRetrieved2Event(
	    int index,
	    int callId,
	    java.lang.String ani,
	    java.lang.String ucid);
}

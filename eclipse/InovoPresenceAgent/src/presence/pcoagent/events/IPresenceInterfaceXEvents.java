package presence.pcoagent.events;

import com4j.*;

/**
 * Events interface for PresenceInterfaceX Control
 */
@IID("{45A0E20F-D21B-11D5-B730-00B0D039C0EF}")
public abstract class IPresenceInterfaceXEvents {
  // Methods:
  /**
   */

  @DISPID(1)
  public void startEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(2)
  public void stopEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param endCode Mandatory int parameter.
   */

  @DISPID(3)
  public void newEndCodeEvent(
    int endCode) {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(4)
  public void outboundCallEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(5)
  public void inboundCallEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param endContact Mandatory Holder<Boolean> parameter.
   */

  @DISPID(6)
  public void endContactEvent(
    Holder<Boolean> endContact) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param recordId Mandatory int parameter.
   */

  @DISPID(7)
  public void recordStartedEvent(
    int recordId) {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(8)
  public void closeEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(9)
  public void loginEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(11)
  public void logoutEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param serviceId Mandatory int parameter.
   * @param queuedContacts Mandatory int parameter.
   * @param serviceName Mandatory java.lang.String parameter.
   */

  @DISPID(10)
  public void queuedContactsEvent(
    int serviceId,
    int queuedContacts,
    java.lang.String serviceName) {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(12)
  public void afterCallWorkEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param serviceId Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param vdn Mandatory int parameter.
   * @param skill Mandatory int parameter.
   */

  @DISPID(13)
  public void alertCallEvent(
    int serviceId,
    java.lang.String phone,
    int vdn,
    int skill) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param serviceId Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param vdn Mandatory int parameter.
   * @param skill Mandatory int parameter.
   */

  @DISPID(14)
  public void endAlertCallEvent(
    int serviceId,
    java.lang.String phone,
    int vdn,
    int skill) {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(16)
  public void unexpectedLogoutEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param lineCount Mandatory int parameter.
   */

  @DISPID(15)
  public void lineCountEvent(
    int lineCount) {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(201)
  public void beforeEndContactEvent() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param endContact Mandatory Holder<Boolean> parameter.
   * @param forceEndContact Mandatory boolean parameter.
   */

  @DISPID(202)
  public void endContact2Event(
    Holder<Boolean> endContact,
    boolean forceEndContact) {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(224)
  public void alertCall2Event() {
        throw new UnsupportedOperationException();
  }


  /**
   */

  @DISPID(225)
  public void endAlertCall2Event() {
        throw new UnsupportedOperationException();
  }


  /**
   * @param customButtonId Mandatory java.lang.String parameter.
   */

  @DISPID(203)
  public void customButtonClickEvent(
    java.lang.String customButtonId) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   */

  @DISPID(230)
  public void softphoneCallOriginatedEvent(
    int index,
    int callId,
    java.lang.String ani) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   */

  @DISPID(231)
  public void softphoneCallDeliveredEvent(
    int index,
    int callId,
    java.lang.String ani) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   */

  @DISPID(232)
  public void softphoneCallEstablishedEvent(
    int index,
    int callId,
    java.lang.String ani) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   */

  @DISPID(233)
  public void softphoneCallClearedEvent(
    int index,
    int callId,
    java.lang.String ani) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   */

  @DISPID(234)
  public void softphoneCallHeldEvent(
    int index,
    int callId,
    java.lang.String ani) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   */

  @DISPID(235)
  public void softphoneCallRetrievedEvent(
    int index,
    int callId,
    java.lang.String ani) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param recordId Mandatory int parameter.
   * @param recordType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   */

  @DISPID(204)
  public void recordStarted2Event(
    int recordId,
    int recordType,
    int serviceId,
    int index,
    int callId) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param recordId Mandatory int parameter.
   * @param recordType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   */

  @DISPID(205)
  public void recordPausedEvent(
    int recordId,
    int recordType,
    int serviceId,
    int index,
    int callId) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param recordId Mandatory int parameter.
   * @param recordType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   */

  @DISPID(206)
  public void recordResumedEvent(
    int recordId,
    int recordType,
    int serviceId,
    int index,
    int callId) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param recordId Mandatory int parameter.
   * @param recordType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   */

  @DISPID(207)
  public void recordStoppedEvent(
    int recordId,
    int recordType,
    int serviceId,
    int index,
    int callId) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(208)
  public void softphoneCallOriginated2Event(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(209)
  public void softphoneCallDelivered2Event(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(210)
  public void softphoneCallEstablished2Event(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(211)
  public void softphoneCallCleared2Event(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(212)
  public void softphoneCallHeld2Event(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid) {
        throw new UnsupportedOperationException();
  }


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(213)
  public void softphoneCallRetrieved2Event(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid) {
        throw new UnsupportedOperationException();
  }


  // Properties:
}

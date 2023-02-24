package presence.pcoagent  ;

import com4j.*;

/**
 * Dispatch interface for PresenceInterfaceX Control
 */
@IID("{45A0E20D-D21B-11D5-B730-00B0D039C0EF}")
public interface IPresenceInterfaceX extends Com4jObject {
  // Methods:
  /**
   * @return  Returns a value of type boolean
   */

  @DISPID(1) //= 0x1. The runtime will prefer the VTID if present
  @VTID(7)
  boolean active();


  /**
   */

  @DISPID(2) //= 0x2. The runtime will prefer the VTID if present
  @VTID(8)
  void close();


  /**
   * @return  Returns a value of type int
   */

  @DISPID(3) //= 0x3. The runtime will prefer the VTID if present
  @VTID(9)
  int generateNewPredictiveCall();


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @param clientId Mandatory int parameter.
   * @param clientName Mandatory java.lang.String parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param status Mandatory int parameter.
   * @param scheduledTime Mandatory java.util.Date parameter.
   * @param captureAgent Mandatory int parameter.
   * @param priority Mandatory int parameter.
   * @param obs Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(4) //= 0x4. The runtime will prefer the VTID if present
  @VTID(10)
  int insertNewOutboundContact(
    int serviceId,
    int loadId,
    int clientId,
    java.lang.String clientName,
    java.lang.String phone,
    int status,
    java.util.Date scheduledTime,
    int captureAgent,
    int priority,
    java.lang.String obs);


  /**
   */

  @DISPID(5) //= 0x5. The runtime will prefer the VTID if present
  @VTID(11)
  void start();


  /**
   */

  @DISPID(6) //= 0x6. The runtime will prefer the VTID if present
  @VTID(12)
  void stop();


  /**
   * @param serviceId Mandatory int parameter.
   * @param endCode Mandatory int parameter.
   */

  @DISPID(7) //= 0x7. The runtime will prefer the VTID if present
  @VTID(13)
  void newEndCode(
    int serviceId,
    int endCode);


  /**
   * @param serviceId Mandatory int parameter.
   */

  @DISPID(8) //= 0x8. The runtime will prefer the VTID if present
  @VTID(14)
  void outboundCall(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   */

  @DISPID(9) //= 0x9. The runtime will prefer the VTID if present
  @VTID(15)
  void inboundCall(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   * @param endContact Mandatory Holder<Boolean> parameter.
   * @param forceEndContact Mandatory boolean parameter.
   */

  @DISPID(10) //= 0xa. The runtime will prefer the VTID if present
  @VTID(16)
  void endContact(
    int serviceId,
    Holder<Boolean> endContact,
    boolean forceEndContact);


  /**
   * @param bs Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(14) //= 0xe. The runtime will prefer the VTID if present
  @VTID(17)
  int setBusinessService(
    java.lang.String bs);


  /**
   */

  @DISPID(15) //= 0xf. The runtime will prefer the VTID if present
  @VTID(18)
  void clearCalls();


  /**
   */

  @DISPID(16) //= 0x10. The runtime will prefer the VTID if present
  @VTID(19)
  void answerCall();


  /**
   */

  @DISPID(17) //= 0x11. The runtime will prefer the VTID if present
  @VTID(20)
  void holdCall();


  /**
   */

  @DISPID(18) //= 0x12. The runtime will prefer the VTID if present
  @VTID(21)
  void transferCall();


  /**
   */

  @DISPID(19) //= 0x13. The runtime will prefer the VTID if present
  @VTID(22)
  void conferenceCall();


  /**
   * @param phone Mandatory java.lang.String parameter.
   */

  @DISPID(20) //= 0x14. The runtime will prefer the VTID if present
  @VTID(23)
  void makeCall(
    java.lang.String phone);


  /**
   * <p>
   * Getter method for the COM property "LineCount"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(21) //= 0x15. The runtime will prefer the VTID if present
  @VTID(24)
  int lineCount();


  /**
   * <p>
   * Getter method for the COM property "LineActive"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(22) //= 0x16. The runtime will prefer the VTID if present
  @VTID(25)
  int lineActive();


  /**
   * <p>
   * Setter method for the COM property "LineActive"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(22) //= 0x16. The runtime will prefer the VTID if present
  @VTID(26)
  void lineActive(
    int value);


  /**
   */

  @DISPID(23) //= 0x17. The runtime will prefer the VTID if present
  @VTID(27)
  void closeContact();


  /**
   * <p>
   * Getter method for the COM property "ContactCode"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(24) //= 0x18. The runtime will prefer the VTID if present
  @VTID(28)
  int contactCode();


  /**
   * <p>
   * Setter method for the COM property "ContactCode"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(24) //= 0x18. The runtime will prefer the VTID if present
  @VTID(29)
  void contactCode(
    int value);


  /**
   * <p>
   * Getter method for the COM property "CollectDigits"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(25) //= 0x19. The runtime will prefer the VTID if present
  @VTID(30)
  java.lang.String collectDigits();


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @param clientId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(26) //= 0x1a. The runtime will prefer the VTID if present
  @VTID(31)
  int deleteOutboundContact(
    int serviceId,
    int loadId,
    int clientId);


  /**
   */

  @DISPID(27) //= 0x1b. The runtime will prefer the VTID if present
  @VTID(32)
  void startSession();


  /**
   * @param endCode Mandatory int parameter.
   */

  @DISPID(28) //= 0x1c. The runtime will prefer the VTID if present
  @VTID(33)
  void stopSession(
    int endCode);


  /**
   */

  @DISPID(29) //= 0x1d. The runtime will prefer the VTID if present
  @VTID(34)
  void startRecording();


  /**
   */

  @DISPID(30) //= 0x1e. The runtime will prefer the VTID if present
  @VTID(35)
  void stopRecording();


  /**
   * <p>
   * Getter method for the COM property "Phone2"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(31) //= 0x1f. The runtime will prefer the VTID if present
  @VTID(36)
  java.lang.String phone2();


  /**
   * <p>
   * Setter method for the COM property "Phone2"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(31) //= 0x1f. The runtime will prefer the VTID if present
  @VTID(37)
  void phone2(
    java.lang.String value);


  /**
   * <p>
   * Getter method for the COM property "ScheduledDate"
   * </p>
   * @return  Returns a value of type java.util.Date
   */

  @DISPID(32) //= 0x20. The runtime will prefer the VTID if present
  @VTID(38)
  java.util.Date scheduledDate();


  /**
   * <p>
   * Setter method for the COM property "ScheduledDate"
   * </p>
   * @param value Mandatory java.util.Date parameter.
   */

  @DISPID(32) //= 0x20. The runtime will prefer the VTID if present
  @VTID(39)
  void scheduledDate(
    java.util.Date value);


  /**
   * <p>
   * Getter method for the COM property "Comments"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(33) //= 0x21. The runtime will prefer the VTID if present
  @VTID(40)
  java.lang.String comments();


  /**
   * <p>
   * Setter method for the COM property "Comments"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(33) //= 0x21. The runtime will prefer the VTID if present
  @VTID(41)
  void comments(
    java.lang.String value);


  /**
   * <p>
   * Getter method for the COM property "ContactName"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(34) //= 0x22. The runtime will prefer the VTID if present
  @VTID(42)
  java.lang.String contactName();


  /**
   * <p>
   * Setter method for the COM property "ContactName"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(34) //= 0x22. The runtime will prefer the VTID if present
  @VTID(43)
  void contactName(
    java.lang.String value);


  /**
   * <p>
   * Getter method for the COM property "CaptureCall"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(35) //= 0x23. The runtime will prefer the VTID if present
  @VTID(44)
  int captureCall();


  /**
   * <p>
   * Setter method for the COM property "CaptureCall"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(35) //= 0x23. The runtime will prefer the VTID if present
  @VTID(45)
  void captureCall(
    int value);


  /**
   * <p>
   * Getter method for the COM property "CaptureCallDateLimit"
   * </p>
   * @return  Returns a value of type java.util.Date
   */

  @DISPID(36) //= 0x24. The runtime will prefer the VTID if present
  @VTID(46)
  java.util.Date captureCallDateLimit();


  /**
   * <p>
   * Setter method for the COM property "CaptureCallDateLimit"
   * </p>
   * @param value Mandatory java.util.Date parameter.
   */

  @DISPID(36) //= 0x24. The runtime will prefer the VTID if present
  @VTID(47)
  void captureCallDateLimit(
    java.util.Date value);


  /**
   * @param variable Mandatory java.lang.String parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(37) //= 0x25. The runtime will prefer the VTID if present
  @VTID(48)
  java.lang.String internetVariable(
    java.lang.String variable);


  /**
   * <p>
   * Getter method for the COM property "EMailInFrom"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(38) //= 0x26. The runtime will prefer the VTID if present
  @VTID(49)
  java.lang.String eMailInFrom();


  /**
   * <p>
   * Getter method for the COM property "EMailInTo"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(39) //= 0x27. The runtime will prefer the VTID if present
  @VTID(50)
  java.lang.String eMailInTo();


  /**
   * <p>
   * Getter method for the COM property "EMailInSubject"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(40) //= 0x28. The runtime will prefer the VTID if present
  @VTID(51)
  java.lang.String eMailInSubject();


  /**
   * <p>
   * Getter method for the COM property "EMailInMessage"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(41) //= 0x29. The runtime will prefer the VTID if present
  @VTID(52)
  java.lang.String eMailInMessage();


  /**
   * @return  Returns a value of type int
   */

  @DISPID(42) //= 0x2a. The runtime will prefer the VTID if present
  @VTID(53)
  int eMailInAttachmentCount();


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(43) //= 0x2b. The runtime will prefer the VTID if present
  @VTID(54)
  java.lang.String eMailInAttachmentName(
    int index);


  /**
   * @param index Mandatory int parameter.
   * @param path Mandatory java.lang.String parameter.
   */

  @DISPID(44) //= 0x2c. The runtime will prefer the VTID if present
  @VTID(55)
  void eMailInAttachmentSave(
    int index,
    java.lang.String path);


  /**
   * @param index Mandatory int parameter.
   */

  @DISPID(45) //= 0x2d. The runtime will prefer the VTID if present
  @VTID(56)
  void eMailInAttachmentOpen(
    int index);


  /**
   * <p>
   * Getter method for the COM property "EMailOutFrom"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(46) //= 0x2e. The runtime will prefer the VTID if present
  @VTID(57)
  java.lang.String eMailOutFrom();


  /**
   * <p>
   * Setter method for the COM property "EMailOutFrom"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(46) //= 0x2e. The runtime will prefer the VTID if present
  @VTID(58)
  void eMailOutFrom(
    java.lang.String value);


  /**
   * <p>
   * Getter method for the COM property "EMailOutTo"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(47) //= 0x2f. The runtime will prefer the VTID if present
  @VTID(59)
  java.lang.String eMailOutTo();


  /**
   * <p>
   * Setter method for the COM property "EMailOutTo"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(47) //= 0x2f. The runtime will prefer the VTID if present
  @VTID(60)
  void eMailOutTo(
    java.lang.String value);


  /**
   * <p>
   * Getter method for the COM property "EMailOutSubject"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(48) //= 0x30. The runtime will prefer the VTID if present
  @VTID(61)
  java.lang.String eMailOutSubject();


  /**
   * <p>
   * Setter method for the COM property "EMailOutSubject"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(48) //= 0x30. The runtime will prefer the VTID if present
  @VTID(62)
  void eMailOutSubject(
    java.lang.String value);


  /**
   * <p>
   * Getter method for the COM property "EMailOutMessage"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(49) //= 0x31. The runtime will prefer the VTID if present
  @VTID(63)
  java.lang.String eMailOutMessage();


  /**
   * <p>
   * Setter method for the COM property "EMailOutMessage"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(49) //= 0x31. The runtime will prefer the VTID if present
  @VTID(64)
  void eMailOutMessage(
    java.lang.String value);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(50) //= 0x32. The runtime will prefer the VTID if present
  @VTID(65)
  int eMailOutAttachmentCount();


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(51) //= 0x33. The runtime will prefer the VTID if present
  @VTID(66)
  java.lang.String eMailOutAttachmentName(
    int index);


  /**
   * @param fileName Mandatory java.lang.String parameter.
   */

  @DISPID(52) //= 0x34. The runtime will prefer the VTID if present
  @VTID(67)
  void eMailOutAttachmentInsert(
    java.lang.String fileName);


  /**
   * @param index Mandatory int parameter.
   */

  @DISPID(53) //= 0x35. The runtime will prefer the VTID if present
  @VTID(68)
  void eMailOutAttachmentDelete(
    int index);


  /**
   */

  @DISPID(54) //= 0x36. The runtime will prefer the VTID if present
  @VTID(69)
  void eMailOutSend();


  /**
   * @param login Mandatory java.lang.String parameter.
   * @param password Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(55) //= 0x37. The runtime will prefer the VTID if present
  @VTID(70)
  int loginAgent(
    java.lang.String login,
    java.lang.String password);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(56) //= 0x38. The runtime will prefer the VTID if present
  @VTID(71)
  int logoutAgent();


  /**
   * <p>
   * Getter method for the COM property "ServiceId"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(57) //= 0x39. The runtime will prefer the VTID if present
  @VTID(72)
  int serviceId();


  /**
   * <p>
   * Getter method for the COM property "CallType"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(58) //= 0x3a. The runtime will prefer the VTID if present
  @VTID(73)
  int callType();


  /**
   * <p>
   * Getter method for the COM property "Phone"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(59) //= 0x3b. The runtime will prefer the VTID if present
  @VTID(74)
  java.lang.String phone();


  /**
   * <p>
   * Getter method for the COM property "OutboundMode"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(60) //= 0x3c. The runtime will prefer the VTID if present
  @VTID(75)
  int outboundMode();


  /**
   * <p>
   * Getter method for the COM property "PredictiveMode"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(61) //= 0x3d. The runtime will prefer the VTID if present
  @VTID(76)
  int predictiveMode();


  /**
   * <p>
   * Getter method for the COM property "PredictiveLevel"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(62) //= 0x3e. The runtime will prefer the VTID if present
  @VTID(77)
  int predictiveLevel();


  /**
   * <p>
   * Getter method for the COM property "SkillExtension"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(63) //= 0x3f. The runtime will prefer the VTID if present
  @VTID(78)
  int skillExtension();


  /**
   * <p>
   * Getter method for the COM property "VDN"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(64) //= 0x40. The runtime will prefer the VTID if present
  @VTID(79)
  int vdn();


  /**
   * <p>
   * Getter method for the COM property "AgentStation"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(65) //= 0x41. The runtime will prefer the VTID if present
  @VTID(80)
  int agentStation();


  /**
   * <p>
   * Getter method for the COM property "AgentId"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(66) //= 0x42. The runtime will prefer the VTID if present
  @VTID(81)
  int agentId();


  /**
   * <p>
   * Getter method for the COM property "ContactId"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(67) //= 0x43. The runtime will prefer the VTID if present
  @VTID(82)
  int contactId();


  /**
   * <p>
   * Getter method for the COM property "CollectVDN"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(70) //= 0x46. The runtime will prefer the VTID if present
  @VTID(83)
  int collectVDN();


  /**
   * <p>
   * Getter method for the COM property "DoubleBuffered"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(71) //= 0x47. The runtime will prefer the VTID if present
  @VTID(84)
  boolean doubleBuffered();


  /**
   * <p>
   * Setter method for the COM property "DoubleBuffered"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(71) //= 0x47. The runtime will prefer the VTID if present
  @VTID(85)
  void doubleBuffered(
    boolean value);


  /**
   * <p>
   * Getter method for the COM property "AlignDisabled"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(72) //= 0x48. The runtime will prefer the VTID if present
  @VTID(86)
  boolean alignDisabled();


  /**
   * <p>
   * Getter method for the COM property "VisibleDockClientCount"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(73) //= 0x49. The runtime will prefer the VTID if present
  @VTID(87)
  int visibleDockClientCount();


  /**
   * @return  Returns a value of type int
   */

  @DISPID(75) //= 0x4b. The runtime will prefer the VTID if present
  @VTID(88)
  int drawTextBiDiModeFlagsReadingOnly();


  /**
   * <p>
   * Getter method for the COM property "Enabled"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(-514) //= 0xfffffdfe. The runtime will prefer the VTID if present
  @VTID(89)
  boolean enabled();


  /**
   * <p>
   * Setter method for the COM property "Enabled"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(-514) //= 0xfffffdfe. The runtime will prefer the VTID if present
  @VTID(90)
  void enabled(
    boolean value);


  /**
   */

  @DISPID(76) //= 0x4c. The runtime will prefer the VTID if present
  @VTID(91)
  void initiateAction();


  /**
   * @return  Returns a value of type boolean
   */

  @DISPID(77) //= 0x4d. The runtime will prefer the VTID if present
  @VTID(92)
  boolean isRightToLeft();


  /**
   * @return  Returns a value of type boolean
   */

  @DISPID(80) //= 0x50. The runtime will prefer the VTID if present
  @VTID(93)
  boolean useRightToLeftReading();


  /**
   * @return  Returns a value of type boolean
   */

  @DISPID(81) //= 0x51. The runtime will prefer the VTID if present
  @VTID(94)
  boolean useRightToLeftScrollBar();


  /**
   * <p>
   * Getter method for the COM property "Visible"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(82) //= 0x52. The runtime will prefer the VTID if present
  @VTID(95)
  boolean visible();


  /**
   * <p>
   * Setter method for the COM property "Visible"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(82) //= 0x52. The runtime will prefer the VTID if present
  @VTID(96)
  void visible(
    boolean value);


  /**
   * <p>
   * Getter method for the COM property "Cursor"
   * </p>
   * @return  Returns a value of type short
   */

  @DISPID(83) //= 0x53. The runtime will prefer the VTID if present
  @VTID(97)
  short cursor();


  /**
   * <p>
   * Setter method for the COM property "Cursor"
   * </p>
   * @param value Mandatory short parameter.
   */

  @DISPID(83) //= 0x53. The runtime will prefer the VTID if present
  @VTID(98)
  void cursor(
    short value);


  /**
   * @param isSubComponent Mandatory boolean parameter.
   */

  @DISPID(87) //= 0x57. The runtime will prefer the VTID if present
  @VTID(99)
  void setSubComponent(
    boolean isSubComponent);


  /**
   * @param serviceId Mandatory int parameter.
   */

  @DISPID(12) //= 0xc. The runtime will prefer the VTID if present
  @VTID(100)
  void connectToService(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   */

  @DISPID(13) //= 0xd. The runtime will prefer the VTID if present
  @VTID(101)
  void disconnectFromService(
    int serviceId);


  /**
   */

  @DISPID(74) //= 0x4a. The runtime will prefer the VTID if present
  @VTID(102)
  void clearActiveCall();


  /**
   * <p>
   * Getter method for the COM property "ClientInfo"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(69) //= 0x45. The runtime will prefer the VTID if present
  @VTID(103)
  java.lang.String clientInfo();


  /**
   * <p>
   * Setter method for the COM property "ClientInfo"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(69) //= 0x45. The runtime will prefer the VTID if present
  @VTID(104)
  void clientInfo(
    java.lang.String value);


  /**
   * @param variable Mandatory java.lang.String parameter.
   * @param data Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(78) //= 0x4e. The runtime will prefer the VTID if present
  @VTID(105)
  int addCallData(
    java.lang.String variable,
    java.lang.String data);


  /**
   * @param variable Mandatory java.lang.String parameter.
   * @param data Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type int
   */

  @DISPID(79) //= 0x4f. The runtime will prefer the VTID if present
  @VTID(106)
  int getCallData(
    java.lang.String variable,
    Holder<java.lang.String> data);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(84) //= 0x54. The runtime will prefer the VTID if present
  @VTID(107)
  int transferCall2();


  /**
   */

  @DISPID(85) //= 0x55. The runtime will prefer the VTID if present
  @VTID(108)
  void login();


  /**
   */

  @DISPID(86) //= 0x56. The runtime will prefer the VTID if present
  @VTID(109)
  void logout();


  /**
   */

  @DISPID(88) //= 0x58. The runtime will prefer the VTID if present
  @VTID(110)
  void raiseCloseEvent();


  /**
   * <p>
   * Getter method for the COM property "DNIS"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(89) //= 0x59. The runtime will prefer the VTID if present
  @VTID(111)
  java.lang.String dnis();


  /**
   * @param phone Mandatory java.lang.String parameter.
   * @param conference Mandatory boolean parameter.
   * @return  Returns a value of type int
   */

  @DISPID(90) //= 0x5a. The runtime will prefer the VTID if present
  @VTID(112)
  int makeConsultationCall(
    java.lang.String phone,
    boolean conference);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(91) //= 0x5b. The runtime will prefer the VTID if present
  @VTID(113)
  int cancelConsultationCall();


  /**
   * <p>
   * Getter method for the COM property "AllowEndContact"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(92) //= 0x5c. The runtime will prefer the VTID if present
  @VTID(114)
  boolean allowEndContact();


  /**
   * <p>
   * Setter method for the COM property "AllowEndContact"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(92) //= 0x5c. The runtime will prefer the VTID if present
  @VTID(115)
  void allowEndContact(
    boolean value);


  /**
   * @param serviceId Mandatory int parameter.
   * @param queuedContacts Mandatory int parameter.
   * @param serviceName Mandatory java.lang.String parameter.
   */

  @DISPID(93) //= 0x5d. The runtime will prefer the VTID if present
  @VTID(116)
  void queuedContacts(
    int serviceId,
    int queuedContacts,
    java.lang.String serviceName);


  /**
   * <p>
   * Getter method for the COM property "QueuedContactsEventTimer"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(94) //= 0x5e. The runtime will prefer the VTID if present
  @VTID(117)
  int queuedContactsEventTimer();


  /**
   * <p>
   * Setter method for the COM property "QueuedContactsEventTimer"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(94) //= 0x5e. The runtime will prefer the VTID if present
  @VTID(118)
  void queuedContactsEventTimer(
    int value);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(95) //= 0x5f. The runtime will prefer the VTID if present
  @VTID(119)
  boolean requestServiceAgents(
    int serviceId);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(96) //= 0x60. The runtime will prefer the VTID if present
  @VTID(120)
  int serviceAgentsCount();


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(97) //= 0x61. The runtime will prefer the VTID if present
  @VTID(121)
  int serviceAgentsLogin(
    int index);


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(98) //= 0x62. The runtime will prefer the VTID if present
  @VTID(122)
  java.lang.String serviceAgentsName(
    int index);


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(99) //= 0x63. The runtime will prefer the VTID if present
  @VTID(123)
  int serviceAgentsStatus(
    int index);


  /**
   */

  @DISPID(100) //= 0x64. The runtime will prefer the VTID if present
  @VTID(124)
  void afterCallWork();


  /**
   * @return  Returns a value of type int
   */

  @DISPID(101) //= 0x65. The runtime will prefer the VTID if present
  @VTID(125)
  int conferenceCall2();


  /**
   * @param serviceId Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param vdn Mandatory java.lang.String parameter.
   * @param skill Mandatory java.lang.String parameter.
   */

  @DISPID(102) //= 0x66. The runtime will prefer the VTID if present
  @VTID(126)
  void alertCall(
    int serviceId,
    java.lang.String phone,
    java.lang.String vdn,
    java.lang.String skill);


  /**
   * @param serviceId Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param vdn Mandatory java.lang.String parameter.
   * @param skill Mandatory java.lang.String parameter.
   */

  @DISPID(103) //= 0x67. The runtime will prefer the VTID if present
  @VTID(127)
  void endAlertCall(
    int serviceId,
    java.lang.String phone,
    java.lang.String vdn,
    java.lang.String skill);


  /**
   */

  @DISPID(105) //= 0x69. The runtime will prefer the VTID if present
  @VTID(128)
  void unexpectedLogout();


  /**
   * @param lineCount Mandatory int parameter.
   */

  @DISPID(104) //= 0x68. The runtime will prefer the VTID if present
  @VTID(129)
  void raiseLineCountEvent(
    int lineCount);


  /**
   * @param variable Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(106) //= 0x6a. The runtime will prefer the VTID if present
  @VTID(130)
  int deleteCallData(
    java.lang.String variable);


  /**
   * <p>
   * Getter method for the COM property "ClientId"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(68) //= 0x44. The runtime will prefer the VTID if present
  @VTID(131)
  int clientId();


  /**
   * <p>
   * Setter method for the COM property "ClientId"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(68) //= 0x44. The runtime will prefer the VTID if present
  @VTID(132)
  void clientId(
    int value);


  /**
   * @param serviceId Mandatory int parameter.
   */

  @DISPID(201) //= 0xc9. The runtime will prefer the VTID if present
  @VTID(133)
  void beforeEndContact(
    int serviceId);


  /**
   */

  @DISPID(202) //= 0xca. The runtime will prefer the VTID if present
  @VTID(134)
  void connectToAllServices();


  /**
   * @param appData Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(203) //= 0xcb. The runtime will prefer the VTID if present
  @VTID(135)
  int startRecording2(
    java.lang.String appData);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(204) //= 0xcc. The runtime will prefer the VTID if present
  @VTID(136)
  int stopRecording2();


  /**
   * @param phone Mandatory java.lang.String parameter.
   * @param appData Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(205) //= 0xcd. The runtime will prefer the VTID if present
  @VTID(137)
  int makeCall2(
    java.lang.String phone,
    java.lang.String appData);


  /**
   * @param phone Mandatory java.lang.String parameter.
   * @param conference Mandatory boolean parameter.
   * @param appData Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(206) //= 0xce. The runtime will prefer the VTID if present
  @VTID(138)
  int makeConsultationCall2(
    java.lang.String phone,
    boolean conference,
    java.lang.String appData);


  /**
   * <p>
   * Getter method for the COM property "UCID"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(207) //= 0xcf. The runtime will prefer the VTID if present
  @VTID(139)
  java.lang.String ucid();


  /**
   * <p>
   * Getter method for the COM property "EmailInId"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(208) //= 0xd0. The runtime will prefer the VTID if present
  @VTID(140)
  int emailInId();


  /**
   * @param stationFrom Mandatory java.lang.String parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param appData Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(209) //= 0xd1. The runtime will prefer the VTID if present
  @VTID(141)
  int makeCallFrom(
    java.lang.String stationFrom,
    java.lang.String phone,
    java.lang.String appData);


  /**
   * @param tones Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(210) //= 0xd2. The runtime will prefer the VTID if present
  @VTID(142)
  int sendDTMFTones(
    java.lang.String tones);


  /**
   * <p>
   * Getter method for the COM property "SkillDescription"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(211) //= 0xd3. The runtime will prefer the VTID if present
  @VTID(143)
  java.lang.String skillDescription();


  /**
   * <p>
   * Getter method for the COM property "VDNDescription"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(212) //= 0xd4. The runtime will prefer the VTID if present
  @VTID(144)
  java.lang.String vdnDescription();


  /**
   * @param serviceId Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param name Mandatory java.lang.String parameter.
   * @param scheduleDate Mandatory java.util.Date parameter.
   * @param comments Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(213) //= 0xd5. The runtime will prefer the VTID if present
  @VTID(145)
  int requestOutboundACDCall(
    int serviceId,
    java.lang.String phone,
    java.lang.String name,
    java.util.Date scheduleDate,
    java.lang.String comments);


  /**
   * <p>
   * Getter method for the COM property "AgentName"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(214) //= 0xd6. The runtime will prefer the VTID if present
  @VTID(146)
  java.lang.String agentName();


  /**
   * <p>
   * Getter method for the COM property "ServiceName"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(215) //= 0xd7. The runtime will prefer the VTID if present
  @VTID(147)
  java.lang.String serviceName();


  /**
   * @param qCode Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(216) //= 0xd8. The runtime will prefer the VTID if present
  @VTID(148)
  boolean isValidQCode(
    int qCode);


  /**
   * <p>
   * Getter method for the COM property "AgentId2"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(217) //= 0xd9. The runtime will prefer the VTID if present
  @VTID(149)
  java.lang.String agentId2();


  /**
   * <p>
   * Getter method for the COM property "AgentStation2"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(218) //= 0xda. The runtime will prefer the VTID if present
  @VTID(150)
  java.lang.String agentStation2();


  /**
   * <p>
   * Getter method for the COM property "CollectVDN2"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(219) //= 0xdb. The runtime will prefer the VTID if present
  @VTID(151)
  java.lang.String collectVDN2();


  /**
   * <p>
   * Getter method for the COM property "SkillExtension2"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(220) //= 0xdc. The runtime will prefer the VTID if present
  @VTID(152)
  java.lang.String skillExtension2();


  /**
   * <p>
   * Getter method for the COM property "VDN2"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(221) //= 0xdd. The runtime will prefer the VTID if present
  @VTID(153)
  java.lang.String vdN2();


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @param clientId Mandatory int parameter.
   * @param clientName Mandatory java.lang.String parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param status Mandatory int parameter.
   * @param scheduledTime Mandatory java.util.Date parameter.
   * @param captureAgent Mandatory java.lang.String parameter.
   * @param priority Mandatory int parameter.
   * @param obs Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(222) //= 0xde. The runtime will prefer the VTID if present
  @VTID(154)
  int insertNewOutboundContact2(
    int serviceId,
    int loadId,
    int clientId,
    java.lang.String clientName,
    java.lang.String phone,
    int status,
    java.util.Date scheduledTime,
    java.lang.String captureAgent,
    int priority,
    java.lang.String obs);


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(223) //= 0xdf. The runtime will prefer the VTID if present
  @VTID(155)
  java.lang.String serviceAgentsLogin2(
    int index);


  /**
   * <p>
   * Getter method for the COM property "QCode"
   * </p>
   * @return  Returns a value of type int
   */

  @DISPID(224) //= 0xe0. The runtime will prefer the VTID if present
  @VTID(156)
  int qCode();


  /**
   * <p>
   * Setter method for the COM property "QCode"
   * </p>
   * @param value Mandatory int parameter.
   */

  @DISPID(224) //= 0xe0. The runtime will prefer the VTID if present
  @VTID(157)
  void qCode(
    int value);


  /**
   * @param customButtonId Mandatory java.lang.String parameter.
   */

  @DISPID(225) //= 0xe1. The runtime will prefer the VTID if present
  @VTID(158)
  void customButtonClick(
    java.lang.String customButtonId);


  /**
   */

  @DISPID(226) //= 0xe2. The runtime will prefer the VTID if present
  @VTID(159)
  void takeCall();


  /**
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   * @param ani Mandatory java.lang.String parameter.
   * @param ucid Mandatory java.lang.String parameter.
   */

  @DISPID(227) //= 0xe3. The runtime will prefer the VTID if present
  @VTID(160)
  void softphoneCallOriginated(
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

  @DISPID(228) //= 0xe4. The runtime will prefer the VTID if present
  @VTID(161)
  void softphoneCallDelivered(
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

  @DISPID(229) //= 0xe5. The runtime will prefer the VTID if present
  @VTID(162)
  void softphoneCallEstablished(
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

  @DISPID(230) //= 0xe6. The runtime will prefer the VTID if present
  @VTID(163)
  void softphoneCallCleared(
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

  @DISPID(231) //= 0xe7. The runtime will prefer the VTID if present
  @VTID(164)
  void softphoneCallHeld(
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

  @DISPID(232) //= 0xe8. The runtime will prefer the VTID if present
  @VTID(165)
  void softphoneCallRetrieved(
    int index,
    int callId,
    java.lang.String ani,
    java.lang.String ucid);


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(233) //= 0xe9. The runtime will prefer the VTID if present
  @VTID(166)
  int getCallId(
    int index);


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(234) //= 0xea. The runtime will prefer the VTID if present
  @VTID(167)
  java.lang.String getANI(
    int index);


  /**
   * <p>
   * Getter method for the COM property "SaveDraftMail"
   * </p>
   * @return  Returns a value of type boolean
   */

  @DISPID(235) //= 0xeb. The runtime will prefer the VTID if present
  @VTID(168)
  boolean saveDraftMail();


  /**
   * <p>
   * Setter method for the COM property "SaveDraftMail"
   * </p>
   * @param value Mandatory boolean parameter.
   */

  @DISPID(235) //= 0xeb. The runtime will prefer the VTID if present
  @VTID(169)
  void saveDraftMail(
    boolean value);


  /**
   */

  @DISPID(236) //= 0xec. The runtime will prefer the VTID if present
  @VTID(170)
  void mailOutCompose();


  /**
   * <p>
   * Getter method for the COM property "EMailOutBody"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(239) //= 0xef. The runtime will prefer the VTID if present
  @VTID(171)
  java.lang.String eMailOutBody();


  /**
   * <p>
   * Setter method for the COM property "EMailOutBody"
   * </p>
   * @param value Mandatory java.lang.String parameter.
   */

  @DISPID(239) //= 0xef. The runtime will prefer the VTID if present
  @VTID(172)
  void eMailOutBody(
    java.lang.String value);


  /**
   * @param mailboxType Mandatory int parameter.
   * @param mailId Mandatory int parameter.
   */

  @DISPID(237) //= 0xed. The runtime will prefer the VTID if present
  @VTID(173)
  void findMailById(
    int mailboxType,
    int mailId);


  /**
   * @param mailboxType Mandatory int parameter.
   * @param sender Mandatory java.lang.String parameter.
   * @param status Mandatory int parameter.
   * @param days Mandatory int parameter.
   */

  @DISPID(238) //= 0xee. The runtime will prefer the VTID if present
  @VTID(174)
  void findMailBySender(
    int mailboxType,
    java.lang.String sender,
    int status,
    int days);


  /**
   * <p>
   * Getter method for the COM property "EMailInBody"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(240) //= 0xf0. The runtime will prefer the VTID if present
  @VTID(175)
  java.lang.String eMailInBody();


  /**
   * @param mailId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(241) //= 0xf1. The runtime will prefer the VTID if present
  @VTID(176)
  int retrieveMailSuspended(
    int mailId);


  /**
   * @param recordId Mandatory int parameter.
   */

  @DISPID(242) //= 0xf2. The runtime will prefer the VTID if present
  @VTID(177)
  void recordStarted(
    int recordId);


  /**
   * @param recordId Mandatory int parameter.
   * @param recordType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param index Mandatory int parameter.
   * @param callId Mandatory int parameter.
   */

  @DISPID(247) //= 0xf7. The runtime will prefer the VTID if present
  @VTID(178)
  void recordStarted2(
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

  @DISPID(243) //= 0xf3. The runtime will prefer the VTID if present
  @VTID(179)
  void recordPaused(
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

  @DISPID(244) //= 0xf4. The runtime will prefer the VTID if present
  @VTID(180)
  void recordResumed(
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

  @DISPID(245) //= 0xf5. The runtime will prefer the VTID if present
  @VTID(181)
  void recordStopped(
    int recordId,
    int recordType,
    int serviceId,
    int index,
    int callId);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(246) //= 0xf6. The runtime will prefer the VTID if present
  @VTID(182)
  int pauseRecording();


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @param sourceId Mandatory int parameter.
   * @param name Mandatory java.lang.String parameter.
   * @param timeZone Mandatory java.lang.String parameter.
   * @param status Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param phoneTimeZone Mandatory java.lang.String parameter.
   * @param alternativePhones Mandatory java.lang.String parameter.
   * @param alternativePhoneDescriptions Mandatory java.lang.String parameter.
   * @param alternativePhoneTimeZones Mandatory java.lang.String parameter.
   * @param scheduleDate Mandatory java.util.Date parameter.
   * @param capturingAgent Mandatory java.lang.String parameter.
   * @param priority Mandatory int parameter.
   * @param comments Mandatory java.lang.String parameter.
   * @param automaticTimeZoneDetection Mandatory boolean parameter.
   * @return  Returns a value of type int
   */

  @DISPID(248) //= 0xf8. The runtime will prefer the VTID if present
  @VTID(183)
  int insertOutboundRecord(
    int serviceId,
    int loadId,
    int sourceId,
    java.lang.String name,
    java.lang.String timeZone,
    int status,
    java.lang.String phone,
    java.lang.String phoneTimeZone,
    java.lang.String alternativePhones,
    java.lang.String alternativePhoneDescriptions,
    java.lang.String alternativePhoneTimeZones,
    java.util.Date scheduleDate,
    java.lang.String capturingAgent,
    int priority,
    java.lang.String comments,
    boolean automaticTimeZoneDetection);


  /**
   * @param serviceId Mandatory int parameter.
   * @param sourceId Mandatory int parameter.
   * @param phone Mandatory java.lang.String parameter.
   * @param name Mandatory java.lang.String parameter.
   * @param scheduleDate Mandatory java.util.Date parameter.
   * @param comments Mandatory java.lang.String parameter.
   * @return  Returns a value of type int
   */

  @DISPID(249) //= 0xf9. The runtime will prefer the VTID if present
  @VTID(184)
  int requestOutboundACDCall2(
    int serviceId,
    int sourceId,
    java.lang.String phone,
    java.lang.String name,
    java.util.Date scheduleDate,
    java.lang.String comments);


  /**
   * @param index Mandatory int parameter.
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(250) //= 0xfa. The runtime will prefer the VTID if present
  @VTID(185)
  java.lang.String getUCID(
    int index);


  /**
   * @param mailboxType Mandatory int parameter.
   * @param sender Mandatory java.lang.String parameter.
   * @param status Mandatory int parameter.
   * @param startDate Mandatory java.util.Date parameter.
   * @param endDate Mandatory java.util.Date parameter.
   */

  @DISPID(251) //= 0xfb. The runtime will prefer the VTID if present
  @VTID(186)
  void findMailBySender2(
    int mailboxType,
    java.lang.String sender,
    int status,
    java.util.Date startDate,
    java.util.Date endDate);


  /**
   * @return  Returns a value of type int
   */

  @DISPID(252) //= 0xfc. The runtime will prefer the VTID if present
  @VTID(187)
  int dropCall();


  /**
   * <p>
   * Getter method for the COM property "LastGetCallDataValue"
   * </p>
   * @return  Returns a value of type java.lang.String
   */

  @DISPID(253) //= 0xfd. The runtime will prefer the VTID if present
  @VTID(188)
  java.lang.String lastGetCallDataValue();


  /**
   * @param windowCode Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(254) //= 0xfe. The runtime will prefer the VTID if present
  @VTID(189)
  int openWindow(
    int windowCode);


  // Properties:
}

package presence.v10.pcoadmin  ;

import com4j.*;

@IID("{6E11BFB6-A515-457B-A88C-C9E387E5C0BB}")
public interface IAdministratorAO extends Com4jObject {
  // Methods:
  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(201) //= 0xc9. The runtime will prefer the VTID if present
  @VTID(7)
  boolean enableLoad(
    int serviceId,
    int loadId);


  /**
   * @param serverName Mandatory java.lang.String parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(202) //= 0xca. The runtime will prefer the VTID if present
  @VTID(8)
  boolean connect(
    java.lang.String serverName);


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(203) //= 0xcb. The runtime will prefer the VTID if present
  @VTID(9)
  boolean disableLoad(
    int serviceId,
    int loadId);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceIdField Mandatory java.lang.String parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priority Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneField Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param phone2Field Mandatory java.lang.String parameter.
   * @param phone3Field Mandatory java.lang.String parameter.
   * @param phoneDescCode Mandatory int parameter.
   * @param phone2DescCode Mandatory int parameter.
   * @param phone3DescCode Mandatory int parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(204) //= 0xcc. The runtime will prefer the VTID if present
  @VTID(10)
  boolean multiServiceLoad(
    int dbAccessType,
    java.lang.String serviceIdField,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    java.lang.String priority,
    java.lang.String nameField,
    java.lang.String phoneField,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String phone2Field,
    java.lang.String phone3Field,
    int phoneDescCode,
    int phone2DescCode,
    int phone3DescCode,
    java.lang.String filter,
    Holder<java.lang.String> errors);


  /**
   * @param serversList Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(205) //= 0xcd. The runtime will prefer the VTID if present
  @VTID(11)
  boolean getServers(
    Holder<java.lang.String> serversList);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(206) //= 0xce. The runtime will prefer the VTID if present
  @VTID(12)
  boolean pauseService(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(207) //= 0xcf. The runtime will prefer the VTID if present
  @VTID(13)
  boolean resumeService(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(208) //= 0xd0. The runtime will prefer the VTID if present
  @VTID(14)
  boolean reloadOutboundQueues(
    int serviceId);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param serviceId Mandatory int parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param priority Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneField Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param phone2Field Mandatory java.lang.String parameter.
   * @param phone3Field Mandatory java.lang.String parameter.
   * @param phoneDescCode Mandatory int parameter.
   * @param phone2DescCode Mandatory int parameter.
   * @param phone3DescCode Mandatory int parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param add Mandatory boolean parameter.
   * @param delete Mandatory boolean parameter.
   * @param modify Mandatory boolean parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(209) //= 0xd1. The runtime will prefer the VTID if present
  @VTID(15)
  boolean updateLoad(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    int serviceId,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String priority,
    java.lang.String nameField,
    java.lang.String phoneField,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String phone2Field,
    java.lang.String phone3Field,
    int phoneDescCode,
    int phone2DescCode,
    int phone3DescCode,
    java.lang.String filter,
    boolean add,
    boolean delete,
    boolean modify,
    Holder<java.lang.String> errors);


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(210) //= 0xd2. The runtime will prefer the VTID if present
  @VTID(16)
  boolean isLoadEnabled(
    int serviceId,
    int loadId);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceIdField Mandatory java.lang.String parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priority Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneField Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param phone2Field Mandatory java.lang.String parameter.
   * @param phone3Field Mandatory java.lang.String parameter.
   * @param phoneDescCode Mandatory int parameter.
   * @param phone2DescCode Mandatory int parameter.
   * @param phone3DescCode Mandatory int parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(211) //= 0xd3. The runtime will prefer the VTID if present
  @VTID(17)
  boolean multiServiceLoad2(
    int dbAccessType,
    java.lang.String serviceIdField,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    java.lang.String priority,
    java.lang.String nameField,
    java.lang.String phoneField,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String phone2Field,
    java.lang.String phone3Field,
    int phoneDescCode,
    int phone2DescCode,
    int phone3DescCode,
    java.lang.String filter,
    java.lang.String commentsField,
    Holder<java.lang.String> errors);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param serviceId Mandatory int parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param priority Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneField Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param phone2Field Mandatory java.lang.String parameter.
   * @param phone3Field Mandatory java.lang.String parameter.
   * @param phoneDescCode Mandatory int parameter.
   * @param phone2DescCode Mandatory int parameter.
   * @param phone3DescCode Mandatory int parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param add Mandatory boolean parameter.
   * @param delete Mandatory boolean parameter.
   * @param modify Mandatory boolean parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(212) //= 0xd4. The runtime will prefer the VTID if present
  @VTID(18)
  boolean updateLoad2(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    int serviceId,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String priority,
    java.lang.String nameField,
    java.lang.String phoneField,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String phone2Field,
    java.lang.String phone3Field,
    int phoneDescCode,
    int phone2DescCode,
    int phone3DescCode,
    java.lang.String filter,
    boolean add,
    boolean delete,
    boolean modify,
    java.lang.String commentsField,
    Holder<java.lang.String> errors);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceIdField Mandatory java.lang.String parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priority Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(213) //= 0xd5. The runtime will prefer the VTID if present
  @VTID(19)
  boolean multiServiceLoad3(
    int dbAccessType,
    java.lang.String serviceIdField,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    java.lang.String priority,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    java.lang.String commentsField,
    Holder<java.lang.String> errors);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param serviceId Mandatory int parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param priority Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param add Mandatory boolean parameter.
   * @param delete Mandatory boolean parameter.
   * @param modify Mandatory boolean parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(214) //= 0xd6. The runtime will prefer the VTID if present
  @VTID(20)
  boolean updateLoad3(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    int serviceId,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String priority,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    boolean add,
    boolean delete,
    boolean modify,
    java.lang.String commentsField,
    Holder<java.lang.String> errors);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param description Mandatory java.lang.String parameter.
   * @param asDefault Mandatory boolean parameter.
   * @param phoneField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param doNotCallListId Mandatory Holder<Integer> parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(215) //= 0xd7. The runtime will prefer the VTID if present
  @VTID(21)
  boolean newDoNotCallList(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String description,
    boolean asDefault,
    java.lang.String phoneField,
    java.lang.String filter,
    Holder<Integer> doNotCallListId,
    Holder<java.lang.String> errors);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param doNotCallListId Mandatory int parameter.
   * @param phoneField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param add Mandatory boolean parameter.
   * @param delete Mandatory boolean parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(216) //= 0xd8. The runtime will prefer the VTID if present
  @VTID(22)
  boolean updateDoNotCallList(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    int doNotCallListId,
    java.lang.String phoneField,
    java.lang.String filter,
    boolean add,
    boolean delete,
    Holder<java.lang.String> errors);


  /**
   * @param doNotCallListId Mandatory int parameter.
   * @param onlyEnabledServices Mandatory boolean parameter.
   * @param onlyEnabledLoads Mandatory boolean parameter.
   * @param updatedPhones Mandatory Holder<Integer> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(217) //= 0xd9. The runtime will prefer the VTID if present
  @VTID(23)
  boolean verifyDoNotCallList(
    int doNotCallListId,
    boolean onlyEnabledServices,
    boolean onlyEnabledLoads,
    Holder<Integer> updatedPhones);


  /**
   * @param serviceId Mandatory int parameter.
   * @param updatedPhones Mandatory Holder<Integer> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(218) //= 0xda. The runtime will prefer the VTID if present
  @VTID(24)
  boolean verifyServiceDoNotCallPhones(
    int serviceId,
    Holder<Integer> updatedPhones);


  /**
   * @param doNotCallListId Mandatory int parameter.
   * @param phoneNumber Mandatory java.lang.String parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(219) //= 0xdb. The runtime will prefer the VTID if present
  @VTID(25)
  boolean insertDoNotCallPhone(
    int doNotCallListId,
    java.lang.String phoneNumber);


  /**
   * @param doNotCallListId Mandatory int parameter.
   * @param phoneNumber Mandatory java.lang.String parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(220) //= 0xdc. The runtime will prefer the VTID if present
  @VTID(26)
  boolean removeDoNotCallPhone(
    int doNotCallListId,
    java.lang.String phoneNumber);


  /**
   * @param doNotCallListId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(221) //= 0xdd. The runtime will prefer the VTID if present
  @VTID(27)
  boolean enableDoNotCallList(
    int doNotCallListId);


  /**
   * @param doNotCallListId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(222) //= 0xde. The runtime will prefer the VTID if present
  @VTID(28)
  boolean disableDoNotCallList(
    int doNotCallListId);


  /**
   * @param doNotCallListId Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(223) //= 0xdf. The runtime will prefer the VTID if present
  @VTID(29)
  boolean addDoNotCallListToService(
    int doNotCallListId,
    int serviceId);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceIdField Mandatory java.lang.String parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priorityType Mandatory int parameter.
   * @param priorityValue Mandatory java.lang.String parameter.
   * @param priorityField Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @param warnings Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(224) //= 0xe0. The runtime will prefer the VTID if present
  @VTID(30)
  boolean multiServiceLoad4(
    int dbAccessType,
    java.lang.String serviceIdField,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    int priorityType,
    java.lang.String priorityValue,
    java.lang.String priorityField,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    java.lang.String commentsField,
    Holder<java.lang.String> errors,
    Holder<java.lang.String> warnings);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param serviceId Mandatory int parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param priorityType Mandatory int parameter.
   * @param priorityValue Mandatory java.lang.String parameter.
   * @param priorityField Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param add Mandatory boolean parameter.
   * @param delete Mandatory boolean parameter.
   * @param modify Mandatory boolean parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(225) //= 0xe1. The runtime will prefer the VTID if present
  @VTID(31)
  boolean updateLoad4(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    int serviceId,
    java.lang.String sourceIdField,
    int loadId,
    int priorityType,
    java.lang.String priorityValue,
    java.lang.String priorityField,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    boolean add,
    boolean delete,
    boolean modify,
    java.lang.String commentsField,
    Holder<java.lang.String> errors);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(226) //= 0xe2. The runtime will prefer the VTID if present
  @VTID(32)
  int disableService(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   * @param loadId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(227) //= 0xe3. The runtime will prefer the VTID if present
  @VTID(33)
  int deleteLoad(
    int serviceId,
    int loadId);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priorityType Mandatory int parameter.
   * @param priorityValue Mandatory java.lang.String parameter.
   * @param priorityField Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @param warnings Mandatory Holder<java.lang.String> parameter.
   * @param records Mandatory Holder<Integer> parameter.
   * @return  Returns a value of type int
   */

  @DISPID(228) //= 0xe4. The runtime will prefer the VTID if present
  @VTID(34)
  int createLoad(
    int dbAccessType,
    int serviceId,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    int priorityType,
    java.lang.String priorityValue,
    java.lang.String priorityField,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    java.lang.String commentsField,
    Holder<java.lang.String> errors,
    Holder<java.lang.String> warnings,
    Holder<Integer> records);


  /**
   * @param mailboxId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(229) //= 0xe5. The runtime will prefer the VTID if present
  @VTID(35)
  int enableMailbox(
    int mailboxId);


  /**
   * @param mailboxId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(230) //= 0xe6. The runtime will prefer the VTID if present
  @VTID(36)
  int disableMailbox(
    int mailboxId);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(231) //= 0xe7. The runtime will prefer the VTID if present
  @VTID(37)
  int enableInternetService(
    int serviceId);


  /**
   * @param serviceId Mandatory int parameter.
   * @return  Returns a value of type int
   */

  @DISPID(232) //= 0xe8. The runtime will prefer the VTID if present
  @VTID(38)
  int disableInternetService(
    int serviceId);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceIdField Mandatory java.lang.String parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priorityType Mandatory int parameter.
   * @param priorityValue Mandatory java.lang.String parameter.
   * @param priorityField Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param scheduleDateType Mandatory int parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param customDataFields Mandatory java.lang.String parameter.
   * @param callerId Mandatory java.lang.String parameter.
   * @param callerName Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @param warnings Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(233) //= 0xe9. The runtime will prefer the VTID if present
  @VTID(39)
  boolean multiServiceLoad5(
    int dbAccessType,
    java.lang.String serviceIdField,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    int priorityType,
    java.lang.String priorityValue,
    java.lang.String priorityField,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    int scheduleDateType,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    java.lang.String commentsField,
    java.lang.String customDataFields,
    java.lang.String callerId,
    java.lang.String callerName,
    Holder<java.lang.String> errors,
    Holder<java.lang.String> warnings);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param serviceId Mandatory int parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param priorityType Mandatory int parameter.
   * @param priorityValue Mandatory java.lang.String parameter.
   * @param priorityField Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param scheduleDateType Mandatory int parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param add Mandatory boolean parameter.
   * @param delete Mandatory boolean parameter.
   * @param modify Mandatory boolean parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param customDataFields Mandatory java.lang.String parameter.
   * @param callerId Mandatory java.lang.String parameter.
   * @param callerName Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @return  Returns a value of type boolean
   */

  @DISPID(234) //= 0xea. The runtime will prefer the VTID if present
  @VTID(40)
  boolean updateLoad5(
    int dbAccessType,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    int serviceId,
    java.lang.String sourceIdField,
    int loadId,
    int priorityType,
    java.lang.String priorityValue,
    java.lang.String priorityField,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    int scheduleDateType,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    boolean add,
    boolean delete,
    boolean modify,
    java.lang.String commentsField,
    java.lang.String customDataFields,
    java.lang.String callerId,
    java.lang.String callerName,
    Holder<java.lang.String> errors);


  /**
   * @param dbAccessType Mandatory int parameter.
   * @param serviceId Mandatory int parameter.
   * @param dbConnection Mandatory java.lang.String parameter.
   * @param dbUsername Mandatory java.lang.String parameter.
   * @param dbPassword Mandatory java.lang.String parameter.
   * @param dbTable Mandatory java.lang.String parameter.
   * @param sourceIdField Mandatory java.lang.String parameter.
   * @param loadId Mandatory int parameter.
   * @param loadDescription Mandatory java.lang.String parameter.
   * @param priorityType Mandatory int parameter.
   * @param priorityValue Mandatory java.lang.String parameter.
   * @param priorityField Mandatory java.lang.String parameter.
   * @param nameField Mandatory java.lang.String parameter.
   * @param phoneFields Mandatory java.lang.String parameter.
   * @param phoneDescCodes Mandatory java.lang.String parameter.
   * @param timeZoneType Mandatory int parameter.
   * @param defaultTimeZone Mandatory java.lang.String parameter.
   * @param timeZones Mandatory java.lang.String parameter.
   * @param scheduleDateField Mandatory java.lang.String parameter.
   * @param scheduleDateType Mandatory int parameter.
   * @param capturingAgentField Mandatory java.lang.String parameter.
   * @param filter Mandatory java.lang.String parameter.
   * @param commentsField Mandatory java.lang.String parameter.
   * @param customDataFields Mandatory java.lang.String parameter.
   * @param callerId Mandatory java.lang.String parameter.
   * @param callerName Mandatory java.lang.String parameter.
   * @param errors Mandatory Holder<java.lang.String> parameter.
   * @param warnings Mandatory Holder<java.lang.String> parameter.
   * @param records Mandatory Holder<Integer> parameter.
   * @return  Returns a value of type int
   */

  @DISPID(235) //= 0xeb. The runtime will prefer the VTID if present
  @VTID(41)
  int createLoad2(
    int dbAccessType,
    int serviceId,
    java.lang.String dbConnection,
    java.lang.String dbUsername,
    java.lang.String dbPassword,
    java.lang.String dbTable,
    java.lang.String sourceIdField,
    int loadId,
    java.lang.String loadDescription,
    int priorityType,
    java.lang.String priorityValue,
    java.lang.String priorityField,
    java.lang.String nameField,
    java.lang.String phoneFields,
    java.lang.String phoneDescCodes,
    int timeZoneType,
    java.lang.String defaultTimeZone,
    java.lang.String timeZones,
    java.lang.String scheduleDateField,
    int scheduleDateType,
    java.lang.String capturingAgentField,
    java.lang.String filter,
    java.lang.String commentsField,
    java.lang.String customDataFields,
    java.lang.String callerId,
    java.lang.String callerName,
    Holder<java.lang.String> errors,
    Holder<java.lang.String> warnings,
    Holder<Integer> records);


  // Properties:
}

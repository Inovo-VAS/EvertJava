package presence.v10_1.pcoadmin  ;

import com4j.*;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
  private ClassFactory() {} // instanciation is not allowed


  /**
   * AOAdministracionEsmarto Object
   */
  public static presence.v10_1.pcoadmin.IAOAdministracionEsmarto createAOAdministracionEsmarto() {
    return COM4J.createInstance( presence.v10_1.pcoadmin.IAOAdministracionEsmarto.class, "{14783D74-50C3-11D4-BD18-000024C8DB7D}" );
  }

  public static presence.v10_1.pcoadmin.IAdministratorAO createAdministratorAO() {
    return COM4J.createInstance( presence.v10_1.pcoadmin.IAdministratorAO.class, "{6CC6275F-DE80-42ED-A22A-BAA92B4A55D7}" );
  }
}

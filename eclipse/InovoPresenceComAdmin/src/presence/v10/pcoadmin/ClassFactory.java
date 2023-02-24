package presence.v10.pcoadmin  ;

import com4j.*;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
  private ClassFactory() {} // instanciation is not allowed


  /**
   * AOAdministracionEsmarto Object
   */
  public static presence.v10.pcoadmin.IAOAdministracionEsmarto createAOAdministracionEsmarto() {
    return COM4J.createInstance( presence.v10.pcoadmin.IAOAdministracionEsmarto.class, "{14783D74-50C3-11D4-BD18-000024C8DB7D}",CLSCTX.LOCAL_SERVER);
  }

  public static presence.v10.pcoadmin.IAdministratorAO createAdministratorAO() {
    return COM4J.createInstance( presence.v10.pcoadmin.IAdministratorAO.class, "{6CC6275F-DE80-42ED-A22A-BAA92B4A55D7}",CLSCTX.LOCAL_SERVER );
  }
}

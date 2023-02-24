package presence.pcoagent  ;

import com4j.*;

/**
 * Defines methods to create COM objects
 */
public abstract class ClassFactory {
  private ClassFactory() {} // instanciation is not allowed


  /**
   * PresenceInterfaceX Control
   */
  public static presence.pcoagent.IPresenceInterfaceX createPresenceInterfaceX() {
    return COM4J.createInstance( presence.pcoagent.IPresenceInterfaceX.class, "{45A0E211-D21B-11D5-B730-00B0D039C0EF}" );
  }
}

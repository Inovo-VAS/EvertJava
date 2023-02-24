package inovo.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InovoServletContextListener
  implements ServletContextListener
{
  private static IInovoServletListener _inovoservletlisten;

  public void contextInitialized(ServletContextEvent event)
  {
    ServletContext sc = event.getServletContext();
    _inovoservletlisten = null;
    try {
      _inovoservletlisten = (IInovoServletListener)Class.forName("InovoEnvironmentManager").newInstance();
    } catch (Exception e) {
      _inovoservletlisten = new InovoCoreEnvironmentManager();
    }
    _inovoservletlisten.initializeServletContext(sc);
  }

  public void contextDestroyed(ServletContextEvent event)
  {
    ServletContext sc = event.getServletContext();
    if (_inovoservletlisten != null) {
      _inovoservletlisten.disposeServletContext(sc);
      inovo.queues.Queue.killAllQueues();
      _inovoservletlisten = null;
    }
  }

  public static IInovoServletListener inovoServletListener() {
    return _inovoservletlisten;
  }
}

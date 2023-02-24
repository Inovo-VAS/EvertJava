package inovo.servlet;

import java.util.List;

import inovo.db.Database;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public abstract interface IInovoServletListener
{
  public abstract void initializeServletContext(ServletContext paramServletContext);

  public abstract void disposeServletContext(ServletContext paramServletContext);

  public abstract void doRequest(String defaultContextPath,HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);

  public abstract Database dbAllias(String paramString);
  
  public abstract Logger logger();
  public abstract void logDebug(String message);
  public abstract String configProperty(String propName);
  
  public List<String> configProperties();
  
  public List<String> configProperties(String propMatchName,boolean startsWith,boolean endsWith,boolean contains);

  public abstract void setConfigProperty(String propName, String propValue);
  
  public String version(String productlabel,String version);
}

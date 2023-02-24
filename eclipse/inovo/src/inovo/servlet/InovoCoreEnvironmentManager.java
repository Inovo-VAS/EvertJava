package inovo.servlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import inovo.automation.AutomationControlQueue;
import inovo.automation.AutomationControlRequest;
import inovo.automation.db.Queue;
import inovo.db.Database;
import inovo.db.IDatabase;
import inovo.web.InovoWebWidget;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InovoCoreEnvironmentManager
  implements IInovoServletListener,IDatabase,Callable<InovoCoreEnvironmentManager>,Runnable
{
  protected String _roottemppath = "";
  protected String _defaultdballiasesfilepath = "";
  private String _servletContextName = "";
  private Logger _logger=null;
  //private Properties _properties=new Properties();
  private HashMap<String,String> _properties=new HashMap<String,String>();
  
  private AutomationControlQueue _automationControlQueue=null;
  
  @Override
  public String version(String productlabel,String version){
	  return "[VERSION:"+productlabel + " - "+version+"]";
  }
  
  public void initializeServletContext(ServletContext sc)
  {
	  Database.attachIDabase(this);
	  if (sc==null){
		  this._servletContextName=this.defaultServletContextName();
		  rootPath();
		  this._roottemppath = defaultLocalPath(this._roottemppath);
	  } else {
		  this._servletContextName = sc.getServletContextName();
		  this._roottemppath = defaultLocalPath(sc.getRealPath("\\").replace(this._servletContextName, "ROOT"));
	  }
	  rootPath();
	  File flog = new File(loggingPath());
	  if (!flog.exists()) {
		  flog.mkdir();
	  }
	  try{
		  loadLoggingConfig("");
	  }	catch (Exception e) {
		  e.printStackTrace();
	  }
    
	  File fconfig = new File(configsPath());
	  if (!fconfig.exists()){
		  fconfig.mkdir();
	  }
	  try{
		  this.log_debug(this.version(this._servletContextName, "1.0.0"));
	  }
	  finally{}

	  try{
		  this.log_debug("LOAD DATABASE CONNECTION(S)");
		  loadDBAlliases("");
		  this.log_debug("LOADED DATABASE CONNECTION(S)");
	  }
	  catch (Exception e) {
		  e.printStackTrace();
		  this.log_debug("FAILED LOADING DATABASE CONNECTION(S) - "+e.getMessage());
	  }
    
    try
    {
    	this.log_debug("LOAD SERVLET CONFIG");
      loadServletConfig("",false);
      this.log_debug("LOADED SERVLET CONFIG");
    }
    catch (Exception e) {
      e.printStackTrace();
      this.log_debug("FAILED LOADING SERVLET CONFIG - "+e.getMessage());
    }
    
    if (this.enableAutomationQueues()){
	    try{
	    	_automationControlQueue=new AutomationControlQueue();
	    	Queue.queue("AUTOMATIONCONTROLQUEUE").queueRequest(new AutomationControlRequest(), false);
	    	this.log_debug("LOADED AUTOMATION CONTROL QUEUE");
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    	this.log_debug("FAILED LOADING AUTOMATION CONTROL QUEUE");
	    }
    }
  }
  
  public String defaultServletContextName() {
	return InovoCoreEnvironmentManager.class.getSimpleName();
}
  
public boolean enableAutomationQueues(){
	return true;
}

@Override
  public String configProperty(String propName){
	  if(this._properties.containsKey(propName=propName.toUpperCase())){
		  return this._properties.get(propName);
	  }
	  return "";
  }
  
  @Override
  public void setConfigProperty(String propName,String propValue){
	  _properties.put(propName.toUpperCase(), propValue);
  }

  public void loadServletConfig(String defaultconfigfilename,boolean updateProperties) throws Exception {
	  if (defaultconfigfilename.equals("")) defaultconfigfilename = this._servletContextName;
	  String configfilepath=this.configsPath()+defaultconfigfilename.toUpperCase()+".cfg";
	  File configPath=new File(configfilepath);
	  if(!configPath.exists()){
		  configPath.createNewFile();
	  }
	  
	  if(updateProperties){
		  if(!this._properties.isEmpty()){
			  FileOutputStream focfg=new FileOutputStream(configPath);
			  for(String propName:this._properties.keySet()){
				  byte[] propertyValue=(propName+"="+this._properties.get(propName)+"\r\n").getBytes();
				  focfg.write(propertyValue);
			  }
			  focfg.flush();  
			  //_properties.store(focfg, defaultconfigfilename);
			  focfg.close();
		  }
	  }
	  
	  FileInputStream ficfg=new FileInputStream(configPath);
	  
	  String propName="";
	  String propValue="";
	  
	  byte[]bytesConfOut=new byte[8192];
	  int bytesConfOutCount=0;
	  boolean assignFound=false;
	  while((bytesConfOutCount=ficfg.read(bytesConfOut))>-1){
		  if(bytesConfOutCount>0){
			  int bytesConfIndex=0;
			  while(bytesConfIndex<bytesConfOutCount){
				  char cfc=(char)bytesConfOut[bytesConfIndex++];
				  switch(cfc){
				  case '=':
					  assignFound=true;
					  break;
				  case '\r':
					  continue;
				  case '\n':
					  if(assignFound){
						  this._properties.put(propName.toUpperCase(), propValue);
					  }
					  propName="";
					  propValue="";
					  assignFound=false;
					  break;
				  default:
					  if(assignFound){
						  propValue+=cfc;
					  }
					  else{
						  propName+=cfc;
					  }
					  break;
				  }
			  }
		  }
	  }
	  if(assignFound){
		  this._properties.put(propName.toUpperCase(), propValue);
		  assignFound=false;
	  }
	  
	  //_properties.load(ficfg);
	  ficfg.close();
	  ficfg=null;
  }

public void loadLoggingConfig(String defaultlogfilename) throws Exception{
	  if (defaultlogfilename.equals("")) defaultlogfilename = this._servletContextName;
	  //defaultlogfilename+=this.defaultLogFileNamePostFix();
	  String log4jpath=this.configsPath()+defaultlogfilename.toUpperCase()+".logcfg";
	  File log4jFile = new File(log4jpath);
	  
      if (log4jFile.exists()) {
    	  //Properties logprops=new Properties();
    	  //FileInputStream filog=new FileInputStream(log4jpath);
    	  //logprops.load(filog);
    	  //filog.close();
    	  //filog=null;
    	  
            PropertyConfigurator.configure(log4jpath);
      } else {
    	  log4jFile.createNewFile();
    	  FileOutputStream foutlog=new FileOutputStream(log4jpath);
    	  String logpathToUse=this.loggingPath();
    	  while(logpathToUse.indexOf('\\')>-1){
    		  logpathToUse=logpathToUse.replace('\\', '/');
    	  }
    	  inovo.adhoc.AdhocUtils.inputStreamToOutputStream(new ByteArrayInputStream(
    			  ("#Define the root logger with appender file\r\n"+
    			  "log = "+logpathToUse+"\r\n"+
    			  "log4j.logger."+this._servletContextName+" = DEBUG, FILE\r\n"+

					"\r\n#Define the Console appender\r\n"+
	    			  "log4j.appender.Console=org.apache.log4j.ConsoleAppender\r\n"+
	    			  "log4j.appender.Console.layout=org.apache.log4j.PatternLayout\r\n"+
	    			  "log4j.appender.Console.layout.conversionPattern=%d{yyyy-MM-dd HH:mm:ss} %p %c - %m%n\r\n"+

    			  "\r\n#Define the file appender\r\n"+
    			  "log4j.appender.FILE.MaxBackupIndex=5\r\n"+
    			  "log4j.appender.FILE.MaxFileSize=2m\r\n"+
    			  "log4j.appender.FILE=org.apache.log4j.DailyRollingFileAppender\r\n"+
    			  "log4j.appender.FILE.File=${log}"+defaultlogfilename.toUpperCase()+".log\r\n"+
    			  "log4j.appender.FILE.layout=org.apache.log4j.PatternLayout\r\n"+
    			  "log4j.appender.FILE.layout.conversionPattern=%d{yyyy-MM-dd HH:mm:ss} %p %c - %m%n").getBytes()), foutlog, 0);
    	  foutlog.flush();
    	  foutlog.close();
    	  
    	  PropertyConfigurator.configure(log4jpath);
      }
      _logger=Logger.getLogger(defaultlogfilename);
  }

  public String defaultLogFileNamePostFix() {
	return "";
}

public String defaultLocalPath(String suggestedlocalpath) {
    return suggestedlocalpath;
  }

  public void loadDBAlliases(String defaultdballiasesfilename) throws Exception {
    if (defaultdballiasesfilename.equals("")) defaultdballiasesfilename = this._servletContextName;
    this._defaultdballiasesfilepath = (configsPath() + "DB" + defaultdballiasesfilename.toUpperCase() + ".cfg");
    File fdb = new File(this._defaultdballiasesfilepath);
    if (!fdb.exists()) {
      fdb.createNewFile();
    }
    else
      Database.registerDBAlliasesFromConfigFile(this._defaultdballiasesfilepath);
  }

  public Database dbAllias(String dballias)
  {
    return Database.dballias(dballias);
  }

  public void disposeServletContext(ServletContext sc)
  {
	  if(_automationControlQueue!=null){
		  _automationControlQueue.kill();
	  }
	  inovo.queues.Queue.killAllQueues();
	  inovo.db.Database.killAllDatabases();
	  this.log_debug("SHUTDOWN-ALL");
  }

  public String rootPath()
  {
    if (this._roottemppath.equals("")) {
      File f = null;
      f = new File("");
      this._roottemppath = (f.getAbsolutePath().replaceAll("[\\\\]", "/") + "/");
    }
    return this._roottemppath;
  }

  public String systemPath() {
    return rootPath() + "inovo" + "/";
  }

  public String configsPath() {
    return systemPath() + "conf" + "/";
  }

  public String loggingPath() {
    return rootPath() + "log" + "/";
  }
  
  @Override
  public Logger logger(){
	  return _logger;
  }

  public String systemName() {
	return "";
}

private String formatContextPath(String contextPathToFormat) {
    String contextPath = "";
    char prevc = 0;
    for (char cc : contextPathToFormat.toCharArray()) {
      if (cc != '/') {
        if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(cc) > -1) {
          if (prevc != '/') {
            contextPath = contextPath + '.';
          }
          contextPath = contextPath + (char)(cc + ' ');
        }
        else {
          contextPath = contextPath + cc;
        }
      }
      prevc = cc;
    }
    return contextPath;
  }

  public void doRequest(String defaultContextPath,HttpServletRequest request, HttpServletResponse response)
  {
    InovoWebWidget inovowidget = null;
    try {
      String path = request.getRequestURL().toString();
      path=(defaultContextPath.equals("")?path:((path.endsWith("/")&&!path.contains(defaultContextPath)?(path.substring(0,path.length()-1)+defaultContextPath):path)));
      String possiblewidgetcontext = formatContextPath(request.getContextPath()) + path.substring(path.indexOf(request.getContextPath()) + request.getContextPath().length()).replaceAll("/", ".");
      try
      {
        inovowidget = path.equals("") ? new InovoWebWidget(request.getInputStream()) : (InovoWebWidget)Class.forName(possiblewidgetcontext).getConstructor(new Class[] { InovoWebWidget.class, InputStream.class }).newInstance(new Object[] { null, request.getInputStream() });
      }
      catch (Exception e) {
    	try{
    		possiblewidgetcontext=(defaultContextPath.indexOf("/")==0?defaultContextPath.substring(1):defaultContextPath).replaceAll("[/]", ".");
    		inovowidget=  (InovoWebWidget)Class.forName(possiblewidgetcontext).getConstructor(new Class[] { InovoWebWidget.class, InputStream.class }).newInstance(new Object[] { null, request.getInputStream() });
    	}
    	catch(Exception ew){
    		inovowidget = new InovoWebWidget(request.getInputStream());
    	}
      }
      String URLREQUEST = path.substring(path.indexOf(request.getContextPath()));
      inovowidget.setRequestHeader("COMMAND", request.getMethod());
      inovowidget.setRequestHeader("URLREQUEST", path.substring(path.indexOf(request.getContextPath())));
      inovowidget.setRequestHeader("LOCAL-HOST", request.getLocalName());
      inovowidget.setRequestHeader("REMOTE-HOST", request.getRemoteHost());
      inovowidget.setRequestHeader("PROTOCOL", request.getProtocol());
      String querystring = request.getQueryString();
      if (querystring != null) inovowidget.parseUrlEncodedParams(querystring.toCharArray());
      Enumeration reqHeaderEnum = request.getHeaderNames();

      while (reqHeaderEnum.hasMoreElements()) {
        String headerName = (String)reqHeaderEnum.nextElement();
        inovowidget.setRequestHeader(headerName, request.getHeader(headerName));
      }

      inovowidget.setResponseHeader("CONTENT-TYPE", "text/html");

      inovowidget.setResponseHeader("CONTENT-LENGTH", "0");
      inovowidget.setResponseHeader("CONNECTION", "Close");

      inovowidget.executeWidget(false);

      inovowidget.setResponseHeader("CONTENT-LENGTH", String.valueOf(inovowidget.responseBytesRead()));

      response.setStatus(inovowidget.responseStatus());

      while (!inovowidget.responseHeaders().isEmpty()) {
        String headerName = (String)inovowidget.responseHeaders().keySet().toArray()[0];
        String headerValue = (String)inovowidget.responseHeaders().remove(headerName);
        inovowidget.responseHeaders().keySet().remove(headerName);
        response.setHeader(headerName, headerValue);
      }

      OutputStream outresponse = response.getOutputStream();
      inovowidget.outputResponseContent(outresponse);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if (inovowidget != null) inovowidget.cleanUpWidget();
  }

	@Override
	public void log_debug(String debugmessage) {
		if(_logger!=null){
			_logger.debug(debugmessage);
		}
	}

	@Override
	public void logDebug(String message) {
		this.log_debug(message);		
	}

	@Override
	public List<String> configProperties() {
		return this.configProperties("",true,false,false);
	}

	@Override
	public List<String> configProperties(String propMatchName,boolean startsWith,boolean endsWith,boolean contains) {
		List<String> configProperties=new ArrayList<String>();
		if(this._properties!=null&&!this._properties.isEmpty()){
			for(String propName:this._properties.keySet()){
				propName=propName.toLowerCase();
				if((propMatchName==null?"":(propMatchName=propMatchName.trim().toUpperCase())).equals("")){
					configProperties.add(propName);
				}
				else if(startsWith&&propName.indexOf(propMatchName)==0){
					configProperties.add(propName);
				}
				else if(endsWith&&(propName.indexOf(propMatchName)+propMatchName.length())==propName.length()){
					configProperties.add(propName);
				}
				else if(contains&&propName.indexOf(propMatchName)>-1){
					configProperties.add(propName);
				}
			}
		}
		return configProperties;
	}

	@Override
	public void run() {
		this.initializeServletContext(null);
		try {
			this.processCoreEnvironment(this);
		} catch (Exception e) {
			this.logDebug(e.getMessage());
		}
		this.disposeServletContext(null);
	}

	public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager) throws Exception{
	}

	@Override
	public InovoCoreEnvironmentManager call() throws Exception {
		return this;
	}
}

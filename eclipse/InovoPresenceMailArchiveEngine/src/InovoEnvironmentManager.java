import inovo.presence.mail.archiver.MailExportQueue;
import inovo.servlet.InovoCoreEnvironmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletContext;

import za.co.woolworths.Indibano.WS_WFS_Multimedia.WS_WFS_MultimediaSOAPStub;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{  
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		//suggestedlocalpath="D:/projects/clients/inovo/java/";
		return super.defaultLocalPath(suggestedlocalpath);
	}
		
	public void initializeServletContext(ServletContext servletContext)
	{
	    super.initializeServletContext(servletContext);
	    String sslDomain=this.configProperty("SSLDOMAIN");
	    String sslPort=this.configProperty("SSLPORT");
	    if(!this.configProperty("SOAPREQUESTTIMEOUT").equals("")){
	    	WS_WFS_MultimediaSOAPStub._requestTimeoutMilliseconds=Integer.parseInt(this.configProperty("SOAPREQUESTTIMEOUT"))*1000;
	    }
	    if(sslDomain.equals("")){
	    	this.log_debug("START MAILEXORTQUEUE");
		    MailExportQueue.mailEportQueue().initiateQueue();
	    	//MailExportQueue.initiateMailExportQueue(60*1024);
		    this.log_debug("STARTED MAILEXORTQUEUE");
	    }
	    else{
		    try{
			    SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(sslDomain, Integer.parseInt(sslPort.equals("")?"443":sslPort));
				
				InputStream inp = sslsocket.getInputStream();
				OutputStream outp = sslsocket.getOutputStream();
				
				// Write a test byte to get a reaction :)
				outp.write(1);
				ByteArrayOutputStream outback=new ByteArrayOutputStream();
				int inputCount=0;
				byte [] inputbuffer=new byte[8192];
				while ((inputCount=inp.read(inputbuffer))>-1) {
					if(inputCount>0){
						byte [] byteread=new byte[inputCount];
						System.arraycopy(inputbuffer, 0, byteread, 0, inputCount);
						
						outback.write(byteread);
						
						byteread=null;
					}
				}
				this.logDebug("SSL:Successfully connected");
				this.logDebug("SSL RESPONSE:"+outback.toString());
				
				this.log_debug("START MAILEXORTQUEUE");
			    MailExportQueue.mailEportQueue().initiateQueue();
				//MailExportQueue.initiateMailExportQueue(60*1024);
			    this.log_debug("STARTED MAILEXORTQUEUE");
		    } catch (Exception exception) {
		    	this.logDebug("SSL:Failure:"+exception.getMessage());
			}
	    }
	}
	  
  @Override
  public void log_debug(String debugmessage) {
	super.log_debug(debugmessage);
  }
  
  @Override
	public void loadServletConfig(String defaultconfigfilename,
			boolean updateProperties) throws Exception {
	  super.loadServletConfig(defaultconfigfilename, false);
		if(this.configProperty("WSMAILSERVICEURL").equals("")){
			this.setConfigProperty("WSMAILSERVICEURL", "https://165.4.12.13:443/WFSMailExportService");
		}
		super.loadServletConfig(defaultconfigfilename, true);
	}
  
  @Override
	public void disposeServletContext(ServletContext sc) {
	    this.log_debug("STOPPING MAILEXPORTQUEUE");
		MailExportQueue.mailEportQueue().shutdownQueue();
	    //MailExportQueue.killMainExportQueueScheduler();
		this.log_debug("STOPPED MAILEXPORTQUEUE");
		super.disposeServletContext(sc);
	}
}
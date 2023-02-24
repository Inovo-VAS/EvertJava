import javax.servlet.ServletContext;

import inovo.message.gateway.MessageFailureLoggingQueue;
import inovo.message.gateway.MessagingQueue;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	public static String _currentLocalpath="";
	private boolean _enableMessagingQueue=false;
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		//suggestedlocalpath="C:/projects/java/";
		return (_currentLocalpath=super.defaultLocalPath(suggestedlocalpath));
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		if(_enableMessagingQueue){
			if(!this.configProperty("PARAMFIELDCOUNT").equals("")){
				MessagingQueue._paramFieldCount=Integer.parseInt(this.configProperty("PARAMFIELDCOUNT"));
			}
			MessagingQueue.messagingQueue();
			MessageFailureLoggingQueue.initMessageFailureLoggingQueue(_currentLocalpath);
		}
	}
	
	@Override
	public void loadServletConfig(String defaultconfigfilename,
			boolean updateProperties) throws Exception {
		super.loadServletConfig(defaultconfigfilename, updateProperties);
		if(this.configProperty("ENABLEMESSAGEQUEUE").toUpperCase().equals("TRUE")){
			_enableMessagingQueue=true;
		}
	}

	@Override
	public void disposeServletContext(ServletContext sc) {
		if(_enableMessagingQueue){
			try {
				MessagingQueue.messagingQueue().shutdownQueue();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				MessageFailureLoggingQueue.messageFailureLoggingQueue().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.disposeServletContext(sc);
	}
	
	
}

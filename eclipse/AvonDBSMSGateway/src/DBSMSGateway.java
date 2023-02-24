import javax.servlet.ServletContext;

import dbssmsgateway.SMSQueue;
import inovo.db.Database;
import inovo.servlet.InovoCoreEnvironmentManager;

public class DBSMSGateway extends InovoCoreEnvironmentManager {

	@Override
	public boolean enableAutomationQueues() {
		return false;
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
	}
	
	@Override
	public void disposeServletContext(ServletContext sc) {
		super.disposeServletContext(sc);
	}
	
	private boolean running=true;
	
	private SMSQueue smsQueue=null;
	
	@Override
	public void processCoreEnvironment(InovoCoreEnvironmentManager inovoCoreEnvironmentManager) throws Exception {
		smsQueue=new SMSQueue(this.dbAllias("SMSGateway"),"avonomni","1234");
		if (smsQueue.start()){
			while(running&&smsQueue.enabled()){
				Thread.currentThread().sleep(10);
			}
		}
	}
	
	@Override
	public String defaultServletContextName() {
		return Main.defaultServletContextName();
	}
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		suggestedlocalpath=Main.defaultLocalPath(suggestedlocalpath);
		System.out.println("suggestedlocalpath:->"+suggestedlocalpath);
		return super.defaultLocalPath(suggestedlocalpath);
	}
}

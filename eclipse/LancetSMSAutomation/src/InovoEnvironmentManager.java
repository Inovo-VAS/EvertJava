import javax.servlet.ServletContext;

import lancet.leads.automation.SMSExportQueue;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	private SMSExportQueue _smsExportQueue=null;
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		return (super.defaultLocalPath(suggestedlocalpath));
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		this.log_debug("LANCET SMS EXPORT STARTED");
	    try {
	    	_smsExportQueue=SMSExportQueue.smsExportQueue(this.configProperty("SMSFILEEXPORTPICKUPPATH"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadServletConfig(String defaultconfigfilename,
			boolean updateProperties) throws Exception {
		super.loadServletConfig(defaultconfigfilename, updateProperties);
	}

	@Override
	public void disposeServletContext(ServletContext sc) {
		if(_smsExportQueue!=null){
			 _smsExportQueue.shutdown();
			 this.log_debug("LANCET LEADS SMS AUTOMATION STOPPED");
		 }
		super.disposeServletContext(sc);
	}
	
	
}

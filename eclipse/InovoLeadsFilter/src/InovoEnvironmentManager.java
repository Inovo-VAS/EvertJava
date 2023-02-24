import javax.servlet.ServletContext;

import inovo.leads.filter.DataSource;
import inovo.servlet.InovoCoreEnvironmentManager;


public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {

	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		//suggestedlocalpath="D:/projects/clients/inovo/java/";
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		try {
			DataSource.loadEnabledDatasources();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(this.configProperty("ACTIVATEAUTOMATION").equals("Y")){
			inovo.leads.filter.LeadsFilter.startupLeadsPrioritisation();
		}
	}
		
	@Override
	public void disposeServletContext(ServletContext sc) {
		if(this.configProperty("ACTIVATEAUTOMATION").equals("Y")){
			inovo.leads.filter.LeadsFilter.shutdownLeadsPrioritisation();
		}
		super.disposeServletContext(sc);
	}
}

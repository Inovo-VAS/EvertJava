import inovo.ftp.file.downloader.FTPDownloader;
import inovo.servlet.InovoCoreEnvironmentManager;
import java.io.IOException;
import javax.servlet.ServletContext;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager
{
  
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		// TODO Auto-generated method stub
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
    public void initializeServletContext(ServletContext servletContext)
    {
    	super.initializeServletContext(servletContext);
    
    	FTPDownloader.ftpdownloader().initiateFTP(this.configProperty("INOVOFTP"));
    }
  
	@Override
	public void log_debug(String debugmessage) {
		super.log_debug(debugmessage);
	}
  
  @Override
	public void loadServletConfig(String defaultconfigfilename,
			boolean updateProperties) throws Exception {
	  	super.loadServletConfig(defaultconfigfilename, false);
	  	
		if(this.configProperty("INOVOFTP").equals("")){
			this.setConfigProperty("INOVOFTP", "username=bb_call_centre;password=b3tt3rb0nd;host=journeysolutions.co.za;localfolder=C:/Software/Inovo/downloadfolder;processedfolder=/processed/;");
		}
		
		super.loadServletConfig(defaultconfigfilename, true);
	}
  
  @Override
	public void disposeServletContext(ServletContext sc) {
		super.disposeServletContext(sc);
	}
}
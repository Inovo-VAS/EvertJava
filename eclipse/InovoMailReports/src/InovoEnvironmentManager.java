import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;

import presence.Administrator;
import inovo.mail.reports.VolvoReportsQueue;
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
			//VolvoReportsQueue.sendMail("DAILY REPORTING", "shelley.loxley@volvo.com", "ejoubert@inovo.co.za", "DAILY REPORT:\r\n AGENT_SERVICELEVEL_BREAKDOWN", "smtp.gmail.com", "587","ejoubert@inovo.co.za","Nan6inga","AGENT_SERVICELEVEL_BREAKDOWN");
			String dailySchedule=this.configProperty("DAILYSCHEDULE");
			String toRecievers=this.configProperty("TORECIEVERS");
			String smtpServer=this.configProperty("SMTP_SERVER");
			String smtpServerPort=smtpServer.lastIndexOf(":")==1?"25":(smtpServer.substring(smtpServer.lastIndexOf(":")+1,smtpServer.length()));
			if (smtpServer.lastIndexOf(":")>-1){
				smtpServer=smtpServer.substring(0,smtpServer.lastIndexOf(":"));
			}
			String smtpAccount=this.configProperty("SMTP_ACCOUNT");
			String smtpAccountPW=this.configProperty("SMTP_PW");
			//String toRecievers="ejoubert@inovo.co.za;faith.mbatha@volvo.com;natasha.rensburg@volvo.com;estelle.roberts@volvo.com;lesinda.dekker@volvo.com;dudu.mosetlhe@udtrucks.co.za;shelley.loxley@volvo.com;Info.hrsc.communication.za@volvo.com";
			VolvoReportsQueue.scheduleReport("DAILY REPORTING",smtpAccount.equals("")?"shelley.loxley@volvo.com":smtpAccount, toRecievers.equals("")?"ejoubert@inovo.co.za;faith.mbatha@volvo.com;natasha.rensburg@volvo.com;estelle.roberts@volvo.com;lesinda.dekker@volvo.com;dudu.mosetlhe@udtrucks.co.za;shelley.loxley@volvo.com;Info.hrsc.communication.za@volvo.com":toRecievers, "DAILY REPORT:\r\n AGENT_SERVICELEVEL_BREAKDOWN","AGENT_SERVICELEVEL_BREAKDOWN","",smtpServer/*"smtp.gmail.com"*/, /*"587"*/smtpServerPort,smtpAccount/*"ejoubert@inovo.co.za"*/,smtpAccountPW/*"Nan6inga"*/,"DAILY",(dailySchedule.equals("")?"06:00":dailySchedule).split("[|]"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void disposeServletContext(ServletContext sc) {
		VolvoReportsQueue.shutdownScheduledReports();
		super.disposeServletContext(sc);
	}
}

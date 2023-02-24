package inovo.mail.reports;

import java.io.InputStream;

import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Monitors extends InovoHTMLPageWidget {

	public Monitors(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
		// TODO Auto-generated constructor stub
	}

	public void generatedReport() throws Exception{
		String suggestedDate=this.requestParameter("suggestedDate");
		String toRecievers=inovo.servlet.InovoServletContextListener.inovoServletListener().configProperty("TORECIEVERSDIRECT");
		String smtpServer=inovo.servlet.InovoServletContextListener.inovoServletListener().configProperty("SMTP_SERVER");
		String smtpServerPort=smtpServer.lastIndexOf(":")==1?"25":(smtpServer.substring(smtpServer.lastIndexOf(":")+1,smtpServer.length()));
		if (smtpServer.lastIndexOf(":")>-1){
			smtpServer=smtpServer.substring(0,smtpServer.lastIndexOf(":"));
		}
		String smtpAccount=inovo.servlet.InovoServletContextListener.inovoServletListener().configProperty("SMTP_ACCOUNT");
		String smtpAccountPW=inovo.servlet.InovoServletContextListener.inovoServletListener().configProperty("SMTP_PW");
		try{
			VolvoReportsQueue.sendMail("DAILY REPORTING"+(suggestedDate.equals("")?"":("["+suggestedDate+"]")),smtpAccount.equals("")?"shelley.loxley@volvo.com":smtpAccount,toRecievers.equals("")?"ejoubert@inovo.co.za;shelley.loxley@volvo.com":toRecievers, "DAILY REPORT:\r\n AGENT_SERVICELEVEL_BREAKDOWN",smtpServer/*"smtp.gmail.com"*/,smtpServerPort/*"587"*/,smtpAccount/*"ejoubert@inovo.co.za"*/,smtpAccountPW/*"Nan6inga"*/,"AGENT_SERVICELEVEL_BREAKDOWN", suggestedDate);
		}
		catch(Exception e){
			inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("err:"+e.getMessage());
		}
	}
}

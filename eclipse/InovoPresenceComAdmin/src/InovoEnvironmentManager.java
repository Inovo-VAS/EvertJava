import java.util.ArrayList;

import javax.servlet.ServletContext;

import inovo.db.Database;
import inovo.servlet.InovoCoreEnvironmentManager;

public class InovoEnvironmentManager extends InovoCoreEnvironmentManager {
	
	private InovoEnvironmentManager inovoEnvironmentManager=null;
	private static presence.pcoadmin.InternalAdminAO IAdminAO=null;
	
	public static presence.pcoadmin.InternalAdminAO IAdminAO(){
		return IAdminAO;
	}
	
	@Override
	public String defaultLocalPath(String suggestedlocalpath) {
		suggestedlocalpath="D:/projects/clients/inovo/java/";
		return super.defaultLocalPath(suggestedlocalpath);
	}
	
	private boolean shutdown=false;
	private Object delayLock=new Object();
	@Override
	public void initializeServletContext(ServletContext sc) {
		super.initializeServletContext(sc);
		this.inovoEnvironmentManager=this;
		
		if(inovoEnvironmentManager.configProperty("PRESENCE_LABEL").equals("")){
			inovoEnvironmentManager.setConfigProperty("PRESENCE_LABEL", "PRESENCE_SERVER");
		}
		
		
		new Thread(){
			
			public void run() {
				logDebug("initiating IAdminAO");
				try{
					IAdminAO=presence.v10.pcoadmin.InternalAdminAO.adminAO();
					logDebug("initiated IAdminAO");
				}
				catch(Exception e){
					logDebug("failed: initiating IAdminAO");
					log_debug(e.getMessage());
				}
				synchronized (delayLock) {
					try {
						delayLock.wait(10000);
					} catch (InterruptedException e1) {
						shutdown=true;
					}
				}
				if(!shutdown){
					IAdminAO.connectToPresence(inovoEnvironmentManager.configProperty("PRESENCE_LABEL"));
				}
				while(!shutdown){
					synchronized (delayLock) {
						try {
							delayLock.wait(10000);
						} catch (InterruptedException e1) {
							shutdown=true;
						}
					}
					if(!shutdown){
						if((delayCount++)%10000==0&&delayCount>0){
							delayCount=0;
							try {
								Database.executeDBRequest(null, Database.dballias("ADMINAO"), "SELECT MAILID,CURRENTSTATUS,REQUIREDSTATUS,CANCHANGE FROM <DBUSER>.ACTIVE_MAILBOXES()", null, inovoEnvironmentManager,"enableDisableMailBoxesData");
							} catch (Exception e) {
							}
						}
					}
				}
				inovoEnvironmentManager.setIAdmonAONULL();
			};
		}.start();
	}
	
	private ArrayList<String> mailboxidsHandled=new ArrayList<String>();
	private int delayCount=0;
	
	public void enableDisableMailBoxesData(int rowIndex,ArrayList<String> data,ArrayList<String> columns){
		if(rowIndex>0){
			if(!mailboxidsHandled.contains(data.get(0))&&data.get(3).equals("Y")){
				if(!data.get(1).equals(data.get(2))){
					if(data.get(2).equals("E")){
						IAdminAO().enableMailBox(data.get(0));
						this.logDebug("Enabled mailbox["+data.get(0)+"]");
					}
					else if(data.get(2).equals("D")){
						IAdminAO().disableMailBox(data.get(0));
						this.logDebug("Disabled mailbox["+data.get(0)+"]");
					}
				}
			}
		}
	}
	
	public void setIAdmonAONULL(){
		IAdminAO=null;
	}
		
	@Override
	public void disposeServletContext(ServletContext sc) {
		shutdown=true;
		synchronized (delayLock) {
			delayLock.notify();
		}
		while(IAdminAO!=null){
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
			}
		}
		presence.v10.pcoadmin.InternalAdminAO.disposeAdminAO();
		
		super.disposeServletContext(sc);
	}
}

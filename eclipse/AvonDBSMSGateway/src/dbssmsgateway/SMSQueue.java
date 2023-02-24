package dbssmsgateway;

import java.util.ArrayList;
import java.util.HashMap;

import inovo.db.Database;

public class SMSQueue implements Runnable{
	Database smsdb=null;
	private String smsgwayuser="";
	private String smsgwaypw="";
	
	public SMSQueue(Database smsdb,String smsgwayuser,String smsgwaypw){
		this.smsdb=smsdb;
		this.smsgwayuser=smsgwayuser;
		this.smsgwaypw=smsgwaypw;
	}

	private boolean enabled=true;
	
	public boolean enabled() {
		return enabled;
	}

	public boolean start() {
		new Thread((Runnable) this).start();
		return true;
	}
	
	private long sleepTimeout=10;
	@Override
	public void run() {
		while (this.enabled){
			try {
				if (!smsSendLogParams.isEmpty()){
					smsSendLogParams.clear();
				}
				
				smsSendLogParams.put("RECSMSCOUNT", "0");
				
				this.smsdb.executeDBRequest(null, "SELECT COUNT(*) AS RECSMSCOUNT FROM SMS_SEND WHERE ENABLED='Y'", smsSendLogParams, null,null);
				
				if (!smsSendLogParams.get("RECSMSCOUNT").toString().equals("")&&!smsSendLogParams.get("RECSMSCOUNT").toString().equals("0")){
					smsSendLogParams.clear();
					this.smsdb.executeDBRequest(null, "SELECT * FROM SMS_SEND WHERE ENABLED='Y'", null, this, "sendRequestMassageData");
					if (sleepTimeout>10) sleepTimeout=10;
				} else {
					if (sleepTimeout>100) sleepTimeout=100;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.currentThread().sleep(sleepTimeout);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private String smssendstatus="";
	
	private HashMap<String,Object> smsSendLogParams=new HashMap<String, Object>();
	
	public void sendRequestMassageData(long rowindex,ArrayList<Object> data,ArrayList<String> columns){
		if (rowindex>0){
			
			smsSendLogParams.clear();
			smsSendLogParams.put("SMS_SEND_ID", data.get(0));
			smssendstatus=stouf.sms.SMS.sendSMS(this.smsgwayuser, this.smsgwaypw, data.get(1).toString(), data.get(3).toString());
			
			smsSendLogParams.put("FAILUREREASON", smssendstatus.equals("success")?"":smssendstatus);
			
			try {
				this.smsdb.executeDBRequest(null, "UPDATE SMS_SEND SET ENABLED='N',SENT='"+(smssendstatus.equals("success")?"Y":"F")+"',SENDSTAMP=GETDATE() WHERE ID=:SMS_SEND_ID; INSERT INTO SMS_SEND_LOG (SMS_SEND_ID,MESSAGE,NAME,MOBILE,SENDSTAMP,FOREIGNREFNR,FAILUREREASON) SELECT SMS_SEND.ID,MESSAGE,NAME,MOBILE,SENDSTAMP,FOREIGNREFNR,:FAILUREREASON FROM SMS_SEND WHERE ID=:SMS_SEND_ID;", smsSendLogParams,null,(String)null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}

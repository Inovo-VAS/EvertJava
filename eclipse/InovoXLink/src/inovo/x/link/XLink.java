package inovo.x.link;

import java.io.InputStream;
import java.util.HashMap;

import org.datacontract.schemas._2004._07.TelephonyApi.SerialNoDetails;
import org.tempuri.BasicHttpBinding_ITelephonyServiceStub;
import org.tempuri.ITelephonyService;
import org.tempuri.TelephonyService2Locator;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class XLink extends InovoHTMLPageWidget {

	public XLink(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		
	}
	
	public void xlink_connect() throws Exception{
		
		String serial_no=this.requestParameter("serial_no");
		String ucid=this.requestParameter("UCID");
		
		HashMap<String,String> mappedInfo=new HashMap<String,String>();
		
		if(serial_no.equals("")){
			mappedInfo.put("account-name", "");
			mappedInfo.put("account-no", "");
			mappedInfo.put("contact-firstname", "");
			mappedInfo.put("contact-lastname", "");
			mappedInfo.put("serial-no", serial_no);
			mappedInfo.put("description", "");
		}
		else{
			try{
				String prodServiceUrl="http://172.25.2.250:12345/TelephonyApiLive/TelephonyService.svc";
				String uatServiceUrl="http://172.25.2.250:12345/TelephonyApiUAT/TelephonyService.svc";
				String serviceUrl="";
				if(this.requestParameter("ENV").toLowerCase().equals("uat")){
					serviceUrl=uatServiceUrl;
				}
				else{
					serviceUrl=prodServiceUrl;
				}
				TelephonyService2Locator telephonyService2Locator=new TelephonyService2Locator(serviceUrl);
				
				ITelephonyService iTelephonyService=telephonyService2Locator.getBasicHttpBinding_ITelephonyService();
				
				SerialNoDetails serialNoDetails=iTelephonyService.findServiceItem(this.requestParameter("serial_no"));
				inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("serialNoDetails:xlink_connect():"+serialNoDetails);
				
				if(serialNoDetails==null){
					inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("NULL SerialNoDetails");
					mappedInfo.put("account-name", "");
					mappedInfo.put("account-no", "");
					mappedInfo.put("contact-firstname", "");
					mappedInfo.put("contact-lastname", "");
					mappedInfo.put("serial-no", this.requestParameter("serial_no"));
					mappedInfo.put("description", "");
				}
				else{
					inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("READ SerialNoDetails");
					mappedInfo.put("account-name", serialNoDetails.getAccountName());
					mappedInfo.put("account-no", serialNoDetails.getAccountNo());
					mappedInfo.put("contact-firstname", serialNoDetails.getContactFirstName());
					mappedInfo.put("contact-lastname", serialNoDetails.getContactLastName());
					mappedInfo.put("serial-no", serialNoDetails.getSerialNo());
					mappedInfo.put("description", serialNoDetails.getDescription());
					inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("SerialNoDetails:"+mappedInfo.toString());
				}
			}
			catch(Exception e){
				inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR:"+e.getMessage());
				respond.print("ERROR="+e.getMessage());
				return;
			}
		}
		if(ucid.equals("")){
			respond.print("ERROR=NO UCID");
		}
		else{
			mappedInfo.put("ucid", ucid);
			try{
				this.respond.print("destination_vdn="+this.logInfoAndObtainServiceVDN(mappedInfo));
			}
			catch(Exception e){
				inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("ERROR:"+e.getMessage());
				respond.print("ERROR="+e.getMessage());
			}
		}
	}

	private String logInfoAndObtainServiceVDN(HashMap<String, String> mappedInfo) throws Exception{
		HashMap<String, Object> params=new HashMap<String,Object>();
		params.put("XLINK_PROD_NAME", mappedInfo.get("description"));
		params.put("UAT_CAMPAIGN_VDN", "0");
		params.put("PROD_CAMPAIGN_VDN", "0");
		params.put("UCID", mappedInfo.get("ucid"));
		Database.executeDBRequest(null, "XLINKREF", "SELECT * FROM PTOOLS.XLINK_PROD_CAMPAIGN_LOOKUP WHERE UPPER(XLINK_PROD_NAME)=:XLINK_PROD_NAME", params, null);
		Database.executeDBRequest(null, "XLINKREF", "DELETE FROM PTOOLS.XLIINK_PROD_CAMPAIGN_LOOKUP_UCID_LOG WHERE UCID=:UCID",params,null);
		
		inovo.servlet.InovoServletContextListener.inovoServletListener().logDebug("SerialNoDetails:logInfoAndObtainServiceID():"+mappedInfo.toString());
		for(String mapkey:mappedInfo.keySet()){
			String mapValue=mappedInfo.get(mapkey);
			if((mapkey=mapkey.trim().toUpperCase()).equals("UCID")) continue;
			params.put("PARAMNAME",mapkey);
			params.put("PARAMVALUE", mapValue);
			Database.executeDBRequest(null, "XLINKREF", "INSERT INTO PTOOLS.XLIINK_PROD_CAMPAIGN_LOOKUP_UCID_LOG (UCID,PARAMNAME,PARAMVALUE) SELECT UCID=:UCID, PARAMNAME=:PARAMNAME,PARAMVALUE=:PARAMVALUE", params, null);
		}
		
		return this.requestParameter("ENV").toLowerCase().equals("uat")?params.get("UAT_CAMPAIGN_VDN").toString():params.get("PROD_CAMPAIGN_VDN").toString();
	}
}

package inovo.avon.cim;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

//CIM COLLECTIONS PAGE
public class Collections extends InovoHTMLPageWidget {

	public Collections(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	private HashMap<String, Object> params=new HashMap<String, Object>();

	@Override
	public void pageContent() throws Exception {
		this.startTable();
			this.startRow();
				this.startColumn();
					this.respondString("CUSTOMER INFO");
				this.endColumn();
			this.endRow();
			this.startRow();
				this.startCell();
					this.importRequestParametersIntoMap(params,"");
					params.put("INFOLOAYOUT", "");//DAT:100118518 AMANDA JONATHAN
					if (params.containsKey("SOURCEID")){
						if (!params.containsKey("CIMLEVEL")) {
							params.put("CIMLEVEL", "");
							if (params.containsKey("SERVICEID") && params.containsKey("LOADID")){
								Database.dballias("COLLECTIONS").executeDBRequest(null,"SELECT COLLECTIONS.fnCIMLEVELBYSERVICELOADID(:SERVICEID,:LOADID) AS CIMLEVEL",params,null,(String)null);
							}
						}
						if (params.containsKey("CIMLEVEL") && !params.get("CIMLEVEL").equals("")){
							params.put("DATASET", "");
							params.put("ACCOUNTNUMBER", "");
							Database.dballias("COLLECTIONS").executeDBRequest(null, "SELECT DATASET,ACCOUNTNUMBER FROM COLLECTIONS.fnAVONJUSBYSOURCEID(:SOURCEID,:CIMLEVEL)", params, null,(String)null);
						}
					}
					if (params.containsKey("NAME")&&params.get("NAME").toString().indexOf(":")>0){
						String[] datavals=params.get("NAME").toString().substring(0,params.get("NAME").toString().indexOf(" ")>params.get("NAME").toString().indexOf(":")?params.get("NAME").toString().indexOf(" "):params.get("NAME").toString().length()).split("[:]");
						params.put("DATASET", datavals[0]);
						params.put("ACCOUNTNUMBER", datavals[1]);
						
					}
					if (params.containsKey("DATASET")&&params.containsKey("ACCOUNTNUMBER")){
						this.fieldHidden("DATASET", params.get("DATASET").toString());
						this.fieldHidden("ACCOUNTNUMBER", params.get("ACCOUNTNUMBER").toString());
						this.fieldHidden("UCID", params.containsKey("UCID")?params.get("UCID").toString():"");
						this.fieldHidden("LOGIN", params.containsKey("LOGIN")?params.get("LOGIN").toString():"0");
						this.fieldHidden("SERVICEID", params.containsKey("SERVICEID")?params.get("SERVICEID").toString():"0");
						this.fieldHidden("CONTACTID", params.containsKey("CONTACTID")?params.get("CONTACTID").toString():"0");
						this.startTable();
							
						Database.dballias("COLLECTIONS").executeDBRequest(null, "SELECT FIELD,VALUE,HIDDEN FROM COLLECTIONS.fnLANDINGINFO(:DATASET,:ACCOUNTNUMBER)", params, this, "customerInfoData");
						
						this.endTable();
					}
				this.endCell();
			this.endRow();
			if (!params.get("INFOLOAYOUT").equals("")){
				
				if (!(params.containsKey("UCID")?params.get("UCID").toString():"").equals("")&&!(params.containsKey("SERVICEID")?params.get("SERVICEID").toString():"").equals("")){
					params.put("VALID_SERVICEID", "N");	
					Database.dballias("COLLECTIONS").executeDBRequest(null, "SELECT 'Y' AS VALID_SERVICEID FROM COLLECTIONS.POPUPSETTINGS WHERE SETTING='CAPTURE-PTP' AND UPPER(','+VALUE+',') LIKE '%,'+:SERVICEID+',%'", params, null,(String)null);
				}
				
				if (!(params.containsKey("UCID")?params.get("UCID").toString():"").equals("")&&!(params.containsKey("SERVICEID")?params.get("SERVICEID").toString():"").equals("")&&(params.containsKey("VALID_SERVICEID")?params.get("VALID_SERVICEID").toString():"").equals("Y")){
					this.captureptpui();
				}
				this.startRow();
					this.startCell("class=ui-widget|id=cellACCOUNTHISTORY|command=fnACCOUNTHISTORY|enableblockui=false");
						this.startScript();
							this.respond.print("inovo_action($('#cellACCOUNTHISTORY'));");
						this.endScript();
					///Database.dballias("COLLECTIONS").executeDBRequest(null, "SELECT CONVERT(VARCHAR(10),ENTRYDATE,121) AS ENTRYDATE,ACTIVITY,ADDITIONAL_INFO FROM [COLLECTIONS].fnACCOUNTHISTORY(:DATASET,:ACCOUNTNUMBER) fn ORDER BY fn.ENTRYDATE", params, this, "customerActivityData");
					this.endCell();
				this.endRow();
			}
		this.endTable();
	}
	
	public void fnACCOUNTHISTORY() throws Exception{
		this.importRequestParametersIntoMap(params,"");
		if (params.containsKey("DATASET")&&params.containsKey("ACCOUNTNUMBER")){
			this.replaceComponentContent("cellACCOUNTHISTORY");
			this.importRequestParametersIntoMap(params,"");
			Database.dballias("COLLECTIONS").executeDBRequest(null, "SELECT CONVERT(VARCHAR(10),ENTRYDATE,121) AS ENTRYDATE,ACTIVITY,ADDITIONAL_INFO FROM [COLLECTIONS].fnACCOUNTHISTORY(:DATASET,:ACCOUNTNUMBER) fn ORDER BY fn.ENTRYDATE", params, this, "customerActivityData");
			this.endReplaceComponentContent();	
		}
	}
	
	private void captureptpui() throws Exception{
		if(params.containsKey("ACCOUNTID")){
			this.fieldHidden("ACCOUNTID", params.get("ACCOUNTID").toString());
		}
		if(params.containsKey("CIMCLIENTID")){
			this.fieldHidden("CIMCLIENTID", params.get("CIMCLIENTID").toString());
		}
		this.startRow();
			this.startCell("class=ui-state-error ui-widget");
				this.startTable();
					this.startRow();
						this.startCell();
							this.fieldLabel("PROMISE TO PAY AMMOUNT");
						this.endCell();
						this.startCell();
							this.fieldInput("PTPAMOUNT",(params.containsKey("PTPAMOUNT")?params.get("PTPAMOUNT").toString():"0"), "", true);
						this.endCell();
					this.endRow();
					this.startRow();
						this.startCell();
							this.fieldLabel("PROMISE TO PAY DATE");
						this.endCell();
						this.startCell();
							this.fieldInput("PTPDATE", "", "date", true);//this.respondString("[yyyy-mm-dd]");
							this.action("CONFIRM PTP", "confirmptp", "", "", "confirmedptp", "", "","");
						this.endCell();
					this.endRow();
				this.endTable();	
			this.endCell();
		this.endRow();	
		this.startRow();
			this.startCell("id=confirmedptp");this.endCell();
		this.endRow();
	}
	
	public void confirmptp() throws Exception{
		this.importRequestParametersIntoMap(params, "");
		ArrayList<String> errors=new ArrayList<String>();
		if (params.get("PTPDATE").equals("")){
			errors.add("ENTER PTP DATE");
		}
		if (!params.get("PTPDATE").equals("")){
			Calendar CalPTP=Calendar.getInstance();
			CalPTP.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(params.get("PTPDATE").toString()));
			Calendar CalToday=Calendar.getInstance();
			CalToday.add(Calendar.DATE, 1);
			CalToday.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(CalToday.getTime())));
			if (CalPTP.getTimeInMillis()<CalToday.getTimeInMillis()){
				errors.add("ENTERED PTP DATE  MUST BE (AT LEASED 1 DAYS) IN THE FUTURE ");
			}
		}
		if (params.get("PTPAMOUNT").equals("")){
			errors.add("ENTER PTP AMOUNT");
		} else if (!params.get("PTPAMOUNT").equals("")){
			try{
				if (Double.parseDouble(params.get("PTPAMOUNT").toString())<=0){
					errors.add("ENTERED PTP AMOUNT CAN NOT BE 0");
				}
			} catch(Exception e){
				errors.add("ENTER VALID PTP AMOUNT");
			}
		}
		
		if (errors.isEmpty()){
			
			//UPDATE OMNI_ACCOUNTS SET PTPDATE=:PTPDATE, PTPAMOUNT=:PTPAMOUND WHERE DATASET=:DATASET AND ACCOUNTNUMBER=:ACCOUNTNUMBER
			//this.respondString(params.toString());
			
			Ptp ptp=new Ptp("http://134.65.204.111/AddOnModules/CaptureNewPTP");
			int amountPromisedInCents=((Double)(Double.parseDouble(params.get("PTPAMOUNT").toString())*100)).intValue();
			
			try {
				if (ptp.CaptureNewPTP(params.get("CIMCLIENTID").toString(), params.get("PTPDATE").toString(),String.valueOf(amountPromisedInCents), "",params.containsKey("LOGIN")?params.get("LOGIN").toString():"0", params.containsKey("UCID")?params.get("UCID").toString():"", "", params.get("ACCOUNTID").toString())){
					this.respondString("CONFIRMED");
				} else {
					this.respondString("PTP NOT CAPTURED:"+ptp.lastError);
				}
			} catch (Exception e) {
				this.respondString("PTP NOT CAPTURED:"+e.getMessage());
			}
			
			//Database.dballias("COLLECTIONS").executeDBRequest(null, "EXECUTE [COLLECTIONS].[UPDATE_ACC_COLLECTIONS_INFO] @DATASET=:DATASET,@ACCOUNTNUMBER=:ACCOUNTNUMBER,@PTPAMOUNT=:PTPAMOUNT,@PTPDATE=:PTPDATE,@UCID=:UCID,@LOGIN=:LOGIN,@SERVICEID=:SERVICEID,@CONTACTID=:CONTACTID", params, null, (String)null);
		} else {
			this.startComplexElement("div", "class=ui-state-error ui-widget");
				this.startComplexElement("ul");
				while(!errors.isEmpty()){
					this.startComplexElement("li");this.respondString(errors.remove(0));this.endComplexElement("li");
				}
				this.endComplexElement("ul");
			this.endComplexElement("div");
		}
	}
	
	public void customerInfoData(boolean foundRecs,long rowindex,ArrayList<Object> data,ArrayList<String> columns) throws Exception{
		if (rowindex>0){
			if (params.get("INFOLOAYOUT").equals("")){
				params.put("INFOLOAYOUT","HAS INFO");
			}
			if (data.get(0).equals("OVERDUE BALANCE")){
				if(!params.containsKey("PTPAMOUNT")){
					params.put("PTPAMOUNT",data.get(1).toString());
				}
			}
			if (data.get(2).toString().equals("0")){
				this.startRow();
					this.startColumn();this.respondString(data.get(0).toString());this.endColumn();
					this.startCell();this.respondString(data.get(1).toString());this.endCell();
				this.endRow();
			} else if (data.get(2).toString().equals("1")){
				//if (data.get(0).equals("ACCOUNTID")){
					params.put(data.get(0).toString(),data.get(1).toString());
				//}
			}
		}
	}
	
	public void customerActivityData(boolean foundRecs,long rowindex,ArrayList<Object> data,ArrayList<String> columns) throws Exception{
		if (rowindex>0){
			this.startTable("class=ui-widget|style=font-size:0.8em");
				this.startRow();
					this.startCell("style=display:inline;font-weight:bold");this.respondString(data.get(0).toString()+" "+data.get(1).toString()); /*this.respondString(data.get(1).toString());*/this.endCell();
				this.endRow();
				this.startRow();
					this.startCell("");
						this.respondString(data.get(2).toString());
					this.endCell();
				this.endRow();
			this.endTable();
		}
	}
}

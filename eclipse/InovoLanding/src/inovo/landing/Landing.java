package inovo.landing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Landing extends InovoHTMLPageWidget {

	public Landing(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}

	private HashMap<String, Object> params=new HashMap<String, Object>();
	
	@Override
	public void pageContent() throws Exception {
		this.startTable();
			this.startRow();
				this.startCell("class=ui-widget");this.respondString("CUSTOMER ACTIVITY"); this.endCell();
			this.endRow();
			this.importRequestParametersIntoMap(params, "ACCOUNTNUMBER,CUSTOMERNUMBER,SOURCEID,CONTACTID");
			if (params.get("SOURCEID").equals("")){
				params.put("SOURCEID","0");
				if(!params.get("CONTACTID").equals("")){
					Database.dballias("LANDING").executeDBRequest(null, "SELECT SOURCEID FROM [dbo].[OutboundLogSourceID](:CONTACTID)", params, null,null);
				}
			}
						
			if (params.containsKey("ACCOUNTNUMBER")&&params.containsKey("CUSTOMERNUMBER")&&params.containsKey("SOURCEID")){
				this.startRow();
					this.startColumn();
						this.respondString("[ACCOUNTNUMBER:"+params.get("ACCOUNTNUMBER").toString()+" CUSTOMERNUMBER:"+params.get("CUSTOMERNUMBER").toString()+" SOURCEID:"+params.get("SOURCEID").toString()+"]");
					this.endColumn();
				this.endRow();
				this.startRow();
					this.startCell();
						this.startTable();
							this.startRow();
								this.startCell("style=vertical-align:top;border:solid 1px");
								Database.dballias("LANDING").executeDBRequest(null, "SELECT FIELD,DATA FROM [dbo].[CustomerInfo](:CUSTOMERNUMBER,:ACCOUNTNUMBER,:SOURCEID)", params, this, "customerInfoData");
								if (this.hasClient){
									this.endTable();
								}
								this.endCell();
								this.startCell("style=vertical-align:top;border:solid 1px");
								Database.dballias("LANDING").executeDBRequest(null, "SELECT Account FROM [dbo].[CustomerAccounts](:CUSTOMERNUMBER,:ACCOUNTNUMBER,:SOURCEID)", params, this, "customerAccountsData");
								if (this.hasAccounts){
									this.endTable();
								}
								this.endCell();
							this.endRow();
						this.endTable();
					this.endCell();
				this.endRow();
				this.startRow();
					this.startCell("class=ui-widget");
					Database.dballias("LANDING").executeDBRequest(null, "SELECT CONVERT(VARCHAR(16),ENTRYDATE,121) AS ENTRYDATE,ACTIVITY,ADDITIONAL_INFO FROM dbo.AccountInteraction(:CUSTOMERNUMBER,:ACCOUNTNUMBER,:SOURCEID) ORDER BY ENTRYDATE", params, this, "customerActivityData");
					this.endCell();
				this.endRow();
			}
		this.endTable();
	}
	
	private boolean hasClient=false;
	public void customerInfoData(long rowindex,ArrayList<Object> data,ArrayList<String> columns) throws Exception{
		if (rowindex>0){
			if (!hasClient){
				hasClient=true;
				this.startTable("class=ui-widget|style=font-size:0.8em");
			}
			
				this.startRow();
					this.startCell("style=display:inline;font-weight:bold");this.respondString(data.get(0).toString()); /*this.respondString(data.get(1).toString());*/this.endCell();
					this.startCell("");
					this.respondString(data.get(1).toString());
				this.endCell();
				this.endRow();
			
		}
	}
	
	private boolean hasAccounts=false;
	public void customerAccountsData(long rowindex,ArrayList<Object> data,ArrayList<String> columns) throws Exception{
		if (rowindex>0){
			if (!hasAccounts){
				hasAccounts=true;
				this.startTable("class=ui-widget|style=font-size:0.8em");
			}
			
				this.startRow();
					this.startCell("style=display:inline;font-weight:bold");this.respondString(data.get(0).toString()); /*this.respondString(data.get(1).toString());*/this.endCell();
				this.endRow();
			
		}
	}
	
	public void customerActivityData(long rowindex,ArrayList<Object> data,ArrayList<String> columns) throws Exception{
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

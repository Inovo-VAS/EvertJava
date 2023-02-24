package inovo.automation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Automation extends InovoHTMLPageWidget {
	public Automation(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		ArrayList<String[]> automationsections=new ArrayList<String[]>();
		automationsections.add(new String[]{"caption=DATABASE AUTOMATION","command=database_Automation"});
		this.actions_tabs(automationsections, "automationsections", false);
	}
	
	public void database_Automation() throws Exception{
		ArrayList<String[]> automationsections=new ArrayList<String[]>();
		for(String registeredDBAllias:Database.registeredDBAlliasses()){
			automationsections.add(new String[]{"caption="+registeredDBAllias,"command=database_SQL_Automation",/*"actiontarget=dbautomationsections",*/"urlparams=DBALLIASSELECTED="+registeredDBAllias});
    	}
		
		this.actions_tabs(automationsections, "dbautomationsections",  false);
		//this.startElement("div", new String[]{"id=dbautomationsections"},true);this.endElement("div", true);
		
	}
	
	public void database_SQL_Automation() throws Exception{
		
		HashMap<String,Object> dbAlliasRequestparams=new HashMap<String,Object>();
		
		String dbAlliasSelected=this.requestParameter("DBALLIASSELECTED");
		dbAlliasSelected=(dbAlliasSelected.equals("")?this.requestParameter("AUTOMATIONDBALLIAS"):dbAlliasSelected);
		
		this.setRequestParameter("AUTOMATIONDBALLIAS", dbAlliasSelected, true);
		
		this.fieldHidden("AUTOMATIONDBALLIAS", dbAlliasSelected);
		
		this.importRequestParametersIntoMap(dbAlliasRequestparams, "AUTOMATIONDBALLIAS,DB_ALLIAS_REQUEST_ID");
		if(dbAlliasRequestparams.get("DB_ALLIAS_REQUEST_ID").equals("")){
			dbAlliasRequestparams.put("DB_ALLIAS_REQUEST_ID", "0");
			Database.executeDBRequest(null,dbAlliasSelected, "SELECT ID AS DB_ALLIAS_REQUEST_ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE UPPER(REQUEST_NAME)=UPPER(:AUTOMATIONDBALLIAS)", dbAlliasRequestparams,null);
			
		}
		this.setRequestParameter("DB_ALLIAS_REQUEST_ID", dbAlliasRequestparams.get("DB_ALLIAS_REQUEST_ID").toString(), true);
		
		this.fieldHidden("DB_ALLIAS_REQUEST_ID",dbAlliasRequestparams.get("DB_ALLIAS_REQUEST_ID").toString());
		
		this.startElement("div", new String[]{"id=dbautomationsections", "class=ui-widget-content"},true);this.endElement("div", true);
		
			this.startElement("div", new String[]{"class=ui-widget-header"}, true);
				this.respondString(dbAlliasSelected);
			this.endElement("div", true);
			this.startTable("");
				this.startRow("");
					this.startCell("");
						this.fieldLabel("NEW DB REQUEST NAME");
					this.endCell();
					this.startCell("");
						this.fieldInput("NEW_DB_REQUEST", "", "text", true);
					this.endCell();
					this.startCell("");
						this.action("ADD SQL REQUEST", "database_new_SQL_Request", "", "","","",  "ui-icon-check", "");
					this.endCell();
				this.endRow();
			this.endTable();
			
			this.startElement("div", new String[]{"class=ui-widget-content","id=database_SQL_Requests"}, true);
		this.endElement("div", true);
		this.startElement("div", new String[]{"id=dballiasrequests"}, true);
			this.dballiasrequests();
		this.endElement("div", true);
	}
	
	public void dballiasrequests() throws Exception{
		if(!this.requestParameter("DB_ALLIAS_REQUEST_ID").equals("0")){
			TreeMap<Integer,ArrayList<Object>> dbrequestsset=new TreeMap<Integer, ArrayList<Object>>();
			Database.executeDBRequest(dbrequestsset,this.requestParameter("AUTOMATIONDBALLIAS"), "SELECT ID AS REQUEST_ID,REQUEST_NAME FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE PARENT_REQUEST_ID="+this.requestParameter("DB_ALLIAS_REQUEST_ID") +" AND REQUEST_EDITABLE=1", null,null);
			this.startTable("");
				this.startRow("");
					this.startColumn(null);
						this.respondString("DB REQUESTS");
					this.endColumn();
					this.startColumn(null);
						this.respondString("DB REQUEST AUTOMATION");
					this.endColumn();
				this.endRow();
				this.startRow("");
					this.startCell(new String[]{"style=vertical-align:top"});
					ArrayList<String[]> automationdbrequests=new ArrayList<String[]>();
					
					
						for(int rowindex:dbrequestsset.keySet()){
							if(rowindex==0) continue;
							HashMap<String,Object> dbrequestdata=Database.rowData(dbrequestsset, rowindex);
							automationdbrequests.add(new String[]{"caption="+dbrequestdata.get("REQUEST_NAME"),"actiontarget=dbrequestflow","command=dbrequestflow","endicon=ui-icon-wrench","urlparams=DB_REQUEST_ID="+dbrequestdata.get("REQUEST_ID")});
						}
						
						this.actions(automationdbrequests, true);
					this.endCell();
					this.startCell(new String[]{"id=dbrequestflow","style=vertical-align:top;text-align:left"});
					this.endCell();
				this.endRow();
			this.endTable();
		}
	}

	public void dbrequestflow() throws Exception{
		this.fieldHidden("DB_REQUEST_ID", this.requestParameter("DB_REQUEST_ID"));
		HashMap<String,Object> latestdbrequestSettings=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(latestdbrequestSettings, null);
		latestdbrequestSettings.put("DB_REQUEST_ACTIVE", "0");
		Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"), "SELECT REQUEST_ACTIVE AS DB_REQUEST_ACTIVE FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE ID=:DB_REQUEST_ID", latestdbrequestSettings,null);
		this.fieldHidden("DB_REQUEST_ACTIVE", latestdbrequestSettings.get("DB_REQUEST_ACTIVE").toString());
		this.action((latestdbrequestSettings.get("DB_REQUEST_ACTIVE").equals("0")?"ENABLE DB REQUEST":"DISABLE DB REQUEST"),"enabledisabledbrequestflow","","","","",(latestdbrequestSettings.get("DB_REQUEST_ACTIVE").equals("0")?"ui-icon-play":"ui-icon-stop"),"");
		ArrayList<String[]> automationdbrequestflowsections=new ArrayList<String[]>();
		automationdbrequestflowsections.add(new String[]{"caption=SETTINGS","command=dbrequestflow_flow_section","starticon=ui-icon-gear"});
		automationdbrequestflowsections.add(new String[]{"caption=CALENDAR","command=dbrequestflow_calendar","starticon=ui-icon-calendar"});
		this.actions_tabs(automationdbrequestflowsections, "dbrequestflowsettingssection", false);
	}
	
	public void dbrequestflow_flow_section() throws Exception{
		
		HashMap<String,Object> flowsettingsfound=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(flowsettingsfound, null);
		flowsettingsfound.put("DB_REQUEST_PROPERTIESCOUNT", "0");
		Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"SELECT COUNT(*) AS DB_REQUEST_PROPERTIESCOUNT FROM <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES WHERE REQUEST_ID=:DB_REQUEST_ID",flowsettingsfound,null);
		
		if(flowsettingsfound.get("DB_REQUEST_PROPERTIESCOUNT").equals("0")){
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'DAILY_SCHEDULE',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'PRE_SQL_SELECT_COMMAND',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'SQL_SELECT_COMMAND',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'POST_SQL_SELECT_COMMAND',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'CSV_EXPORT_FILENAME_MASK',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'MAIL_EXPORT_ADDRESSES',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'MAIL_EXPORT_SMTP_ADDRESS',''",flowsettingsfound,null);
			Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"),"INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES (REQUEST_ID,PROPERTY_NAME,PROPERTY_VALUE) SELECT :DB_REQUEST_ID,'MAIL_EXPORT_SMTP_ACCOUNT',''",flowsettingsfound,null);
		}
		TreeMap<Integer,ArrayList<Object>> flowsettingsset=new TreeMap<Integer, ArrayList<Object>>();
		Database.executeDBRequest(flowsettingsset,this.requestParameter("AUTOMATIONDBALLIAS"), "SELECT * FROM <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES WHERE REQUEST_ID="+this.requestParameter("DB_REQUEST_ID")+" ORDER BY ID", null,null);
		this.startTable("");
			for(int rowindex:flowsettingsset.keySet()){
				this.startRow("");
					if(rowindex==0){
						this.startColumn("class=label");this.respondString("PROPERTY"); this.endColumn();
						
					}
					else{
						HashMap<String,Object> flowsettingsdata=Database.rowData(flowsettingsset, rowindex);
						this.startCell("");
							this.startTable("");
								this.startRow("");
									this.startCell(new String[]{"style=font-size:0.8em", "class=ui-widget-header"});this.respondString(flowsettingsdata.get("PROPERTY_NAME").toString()); this.endCell();
								this.endRow();
								this.startRow("");
									this.startCell("");this.fieldInput(flowsettingsdata.get("PROPERTY_NAME").toString(), flowsettingsdata.get("PROPERTY_VALUE").toString(),"multiline",this.requestParameter("DB_REQUEST_ACTIVE").equals("0"),new String[]{"cols=80"}); this.endCell();
								this.endRow();
							this.endTable();
						this.endCell();
					}
				
				this.endRow();
			}
		this.endTable();
		if(this.requestParameter("DB_REQUEST_ACTIVE").equals("0")) this.action("APPLY CHANGES", "updatedbrequestflowsettings", "", "", "", "ui-icon-save", "", "");
	}
	
	public void updatedbrequestflowsettings() throws Exception{
		HashMap<String,Object> dbrequestflowsettingstoupdate=new HashMap<String,Object>();
		this.importRequestParametersIntoMap(dbrequestflowsettingstoupdate, null);
		TreeMap<Integer,ArrayList<Object>> dbrequestpropertiesset=new TreeMap<Integer, ArrayList<Object>>();
		Database.executeDBRequest(dbrequestpropertiesset,this.requestParameter("AUTOMATIONDBALLIAS"), "SELECT * FROM <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES WHERE REQUEST_ID=:DB_REQUEST_ID", dbrequestflowsettingstoupdate,null);
		
		String propertiesThatChanged="";
		
		HashMap<String,Object> dbrequestflowproperties=new HashMap<String,Object>();
		for(int rowindex:dbrequestpropertiesset.keySet()){
			if(rowindex==0) continue;
			HashMap<String,Object> dbrequestproperty=Database.rowData(dbrequestpropertiesset, rowindex);
			
			
			
			if(!dbrequestproperty.get("PROPERTY_VALUE").equals(dbrequestflowsettingstoupdate.get(dbrequestproperty.get("PROPERTY_NAME")))){
				propertiesThatChanged="PROPERTY_VALUE=:"+dbrequestproperty.get("PROPERTY_NAME");
				dbrequestflowproperties.put(dbrequestproperty.get("PROPERTY_NAME").toString(),dbrequestflowsettingstoupdate.get(dbrequestproperty.get("PROPERTY_NAME")));
			}
			
			if(!propertiesThatChanged.equals("")){
				dbrequestflowproperties.put("ID", dbrequestproperty.get("ID"));
				propertiesThatChanged="UPDATE <DBUSER>.INOVO_AUTOMATION_REQUEST_PROPERTIES SET "+propertiesThatChanged+" WHERE ID=:ID";
				Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"), propertiesThatChanged, dbrequestflowproperties,null);
			}
			
			propertiesThatChanged="";
			dbrequestflowproperties.clear();
			
		}
		
		this.replaceComponentContent("dbrequestflowsettingssection");
		dbrequestflow_flow_section();
		this.endReplaceComponentContent();
	}
	
	public void enabledisabledbrequestflow() throws Exception{
		Database.executeDBRequest(null,this.requestParameter("AUTOMATIONDBALLIAS"), "UPDATE <DBUSER>.INOVO_AUTOMATION_REQUEST SET REQUEST_ACTIVE="+(this.requestParameter("DB_REQUEST_ACTIVE").equals("0")?"1":"0")+" WHERE ID="+this.requestParameter("DB_REQUEST_ID"), null,null);
		this.replaceComponentContent("dbrequestflow");
			this.dbrequestflow();
		this.endReplaceComponentContent();
	}
	
	public void dbrequestflow_calendar() throws Exception{
		
	}
	
	public void database_new_SQL_Request() throws Exception{
		String AUTOMATIONDBALLIAS=this.requestParameter("AUTOMATIONDBALLIAS");
		String NEW_DB_REQUEST=this.requestParameter("NEW_DB_REQUEST");
		if(NEW_DB_REQUEST.equals("")){
			this.showDialog("", new String []{"title=ADDING NEW DB REQUEST","content=NO DB REQUEST NAME PROVIDED"});
		}
		else{
			HashMap<String,Object> newDBAutomationRequest=new HashMap<String,Object>();
			
			this.importRequestParametersIntoMap(newDBAutomationRequest, "AUTOMATIONDBALLIAS,DB_ALLIAS_REQUEST_ID,NEW_DB_REQUEST");
			
			if(newDBAutomationRequest.get("DB_ALLIAS_REQUEST_ID").equals("0")){
			
				Database.executeDBRequest(null,AUTOMATIONDBALLIAS, "SELECT TOP 1 ID AS DB_ALLIAS_REQUEST_ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE UPPER(REQUEST_NAME)=UPPER(:AUTOMATIONDBALLIAS)", newDBAutomationRequest,null);
				
				if(newDBAutomationRequest.get("DB_ALLIAS_REQUEST_ID").equals("0")){
					newDBAutomationRequest.put("NEW_DBALLIAS_REQUEST_TYPE", "DBALLIASREQUEST");
					Database.executeDBRequest(null,AUTOMATIONDBALLIAS, "INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST (REQUEST_NAME,REQUEST_TYPE) SELECT UPPER(:AUTOMATIONDBALLIAS),:NEW_DBALLIAS_REQUEST_TYPE", newDBAutomationRequest,null);
					Database.executeDBRequest(null,AUTOMATIONDBALLIAS, "SELECT TOP 1 ID AS DB_ALLIAS_REQUEST_ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE UPPER(REQUEST_NAME)=UPPER(:AUTOMATIONDBALLIAS)", newDBAutomationRequest,null);
					
				}
			}
			
			newDBAutomationRequest.put("DB_REQUEST_ID", "0");
			
			Database.executeDBRequest(null,AUTOMATIONDBALLIAS, "SELECT TOP 1 ID AS DB_REQUEST_ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE UPPER(REQUEST_NAME)=UPPER(:NEW_DB_REQUEST) AND PARENT_REQUEST_ID=:DB_ALLIAS_REQUEST_ID", newDBAutomationRequest,null);
			
			if(newDBAutomationRequest.get("DB_REQUEST_ID").equals("0")){
				newDBAutomationRequest.put("NEW_REQUEST_TYPE", "DBREQUEST");
				Database.executeDBRequest(null,AUTOMATIONDBALLIAS, "INSERT INTO <DBUSER>.INOVO_AUTOMATION_REQUEST (REQUEST_NAME,REQUEST_TYPE,PARENT_REQUEST_ID) SELECT UPPER(:NEW_DB_REQUEST),:NEW_REQUEST_TYPE,:DB_ALLIAS_REQUEST_ID", newDBAutomationRequest,null);
				Database.executeDBRequest(null,AUTOMATIONDBALLIAS, "SELECT TOP 1 ID AS DB_REQUEST_ID FROM <DBUSER>.INOVO_AUTOMATION_REQUEST WHERE UPPER(REQUEST_NAME)=UPPER(:NEW_DB_REQUEST) AND PARENT_REQUEST_ID=:DB_ALLIAS_REQUEST_ID", newDBAutomationRequest,null);
				this.showDialog("", new String []{"title=ADDING NEW DB REQUEST","content=DB REQUEST CREATED"});
			}
			else{
				this.showDialog("", new String []{"title=ADDING NEW DB REQUEST","content=DB REQUEST ALREADY CREATED"});
			}
			
			newDBAutomationRequest.clear();
		}
	}

}

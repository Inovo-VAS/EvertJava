package inovo.call.info;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class ConfigureCallerDetails extends InovoHTMLPageWidget {

	public ConfigureCallerDetails(InovoWebWidget parentWidget,
			InputStream inStream) {
		super(parentWidget, inStream);
	}

	@Override
	public void pageContent() throws Exception {
		ArrayList<String[]> actionsProperties=new ArrayList<String[]>();
		actionsProperties.add("caption=CONFIGURE REGION(s)|actiontarget=configure_sections|command=configure_regions|urlparams=master_action=configure-region".split("[|]"));
		actionsProperties.add("caption=CONFIGURE CALLER(s)|actiontarget=configure_sections|command=configure_callers|urlparams=master_action=configure-caller".split("[|]"));
		this.actions(actionsProperties, false);
		
		this.startElement("div", "id=configure_sections|class=ui-widget-content".split("[|]"), true);this.endElement("div", true);
	}
	
	public void configure_regions() throws Exception{
		this.startElement("div", "class=ui-widget-header|style=font-size:0.8em", true);
			this.respondString("CONFIGURED REGION(s)");
		this.endElement("div", true);
		this.action("ADD REGION", "add_region_details", "", "", "gridview", "", "", "");
		this.startElement("div", "class=ui-widget-content|id=gridview", true);
			this.configuredRegionsView();
		this.endElement("div", true);
	}
	
	public void configuredRegionsView() throws Exception{
		this.startTable();
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT [ID] ,[REGIONNAME] ,[SERVICEID] ,[SERVICENAME] FROM <DBUSER>.[REGIONLOOKUP] ORDER BY REGIONNAME", null, this,"configuredRegionsViewData");
		this.endTable();
	}
	
	public void configuredRegionsViewData(int rowIndex,ArrayList<String> data,ArrayList<String> cols) throws Exception{
		this.startRow();
		boolean doneFirstCell=false;
		if(rowIndex==0){
			for(String confdata:cols){
				if(!doneFirstCell){
					doneFirstCell=true;
					this.startCell("style=font-size:0.8em".split("[|]"));
					this.endCell();
				}
				else{
					this.startColumn("font-size:0.8em");
						this.respondString(confdata);
					this.endColumn();
				}
			}
			this.startCell("style=font-size:0.8em".split("[|]"));
			this.endCell();
		}
		else{
			for(String confdata:data){
				if(!doneFirstCell){
					doneFirstCell=true;
					this.startCell("style=font-size:0.8em".split("[|]"));
						this.action("...", "edit_configured_region", "", "", "gridview", "", "", "SEL_REGION_ID="+data.get(0));
					this.endCell();
				}
				else{
					this.startCell("style=font-size:0.8em;border:solid 1px".split("[|]"));
						this.respondString(confdata);
					this.endCell();
				}
			}
			this.startCell("style=font-size:0.8em".split("[|]"));
				this.action("del", "delete_configured_region", "", "", "gridview", "", "", "SEL_REGION_ID="+data.get(0));
			this.endCell();
		}
		this.endRow();
	}
	
	public void add_region_details() throws Exception{
		this.startTable();
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respondString("REGION");
				this.endColumn();
				this.startCell();
					this.fieldInput("NEW_REGION", "", "text", true, "size=100".split("[|]"));
				this.endCell();
			this.endRow();
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respondString("PRESENCE SERVICE");
				this.endColumn();
				Database.executeDBRequest(null, "PRESENCEVIEW", "SELECT * FROM <DBUSER>.SERVICE ORDER BY ID", null, this,"presenceCampaignsData");
				this.startCell();
					this.fieldInput("SERVICEID","","select",true,"".split("[|]"),this.presenceCampaigns);
				this.endCell();
			this.endRow();
		this.endTable();
		this.action("CONFIRM ADDING REGION", "confirm_adding_region", "", "", "", "", "", "");
		this.action("CANCEL", "configuredRegionsView", "", "", "gridview", "", "", "");
	}
	
	public void edit_configured_region() throws Exception{
		HashMap<String,Object> regionInfoToEdit=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(regionInfoToEdit, "SEL_REGION_ID");
		regionInfoToEdit.put("REGIONNAME", "");
		regionInfoToEdit.put("SERVICEID", "0");
		regionInfoToEdit.put("SERVICENAME", "");
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 REGIONNAME,SERVICEID,SERVICENAME FROM <DBUSER>.REGIONLOOKUP WHERE ID=:SEL_REGION_ID", regionInfoToEdit, null);
		this.fieldHidden("SEL_REGION_ID",(String) regionInfoToEdit.get("SEL_REGION_ID"));
		this.startTable();
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respondString("REGION");
				this.endColumn();
				this.startCell();
					this.fieldInput("REGIONNAME",(String) regionInfoToEdit.get("REGIONNAME"), "text", true, "size=100".split("[|]"));
				this.endCell();
			this.endRow();
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respondString("PRESENCE SERVICE");
				this.endColumn();
				Database.executeDBRequest(null, "PRESENCEVIEW", "SELECT * FROM <DBUSER>.SERVICE ORDER BY ID", null, this,"presenceCampaignsData");
				this.startCell();
					this.fieldInput("SERVICEID",(String)regionInfoToEdit.get("SERVICEID"),"select",true,"".split("[|]"),this.presenceCampaigns);
				this.endCell();
			this.endRow();
		this.endTable();
		this.action("CONFIRM CHANGING REGION", "confirm_changing_region", "", "", "", "", "", "");
		this.action("CANCEL", "configuredRegionsView", "", "", "gridview", "", "", "");
	}
	
	public void delete_configured_region() throws Exception{
		HashMap<String,Object> regionInfoToEdit=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(regionInfoToEdit, "SEL_REGION_ID");
		regionInfoToEdit.put("REGIONNAME", "");
		regionInfoToEdit.put("SERVICEID", "0");
		regionInfoToEdit.put("SERVICENAME", "");
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 REGIONNAME,SERVICEID,SERVICENAME FROM <DBUSER>.REGIONLOOKUP WHERE ID=:SEL_REGION_ID", regionInfoToEdit, null);
		this.fieldHidden("SEL_REGION_ID",(String) regionInfoToEdit.get("SEL_REGION_ID"));
		this.startTable();
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respondString("REGION");
				this.endColumn();
				this.startCell();
					this.fieldInput("REGIONNAME",(String) regionInfoToEdit.get("REGIONNAME"), "text", false, "size=100".split("[|]"));
				this.endCell();
			this.endRow();
			this.startRow();
				this.startColumn("font-size:0.8em");
					this.respondString("PRESENCE SERVICE");
				this.endColumn();
				Database.executeDBRequest(null, "PRESENCEVIEW", "SELECT * FROM <DBUSER>.SERVICE ORDER BY ID", null, this,"presenceCampaignsData");
				this.startCell();
					this.fieldInput("SERVICEID",(String)regionInfoToEdit.get("SERVICEID"),"select",false,"".split("[|]"),this.presenceCampaigns);
				this.endCell();
			this.endRow();
		this.endTable();
		this.action("CONFIRM DELETING REGION", "confirm_deleting_region", "", "", "", "", "", "");
		this.action("CANCEL", "configuredRegionsView", "", "", "gridview", "", "", "");
	}
	
	private TreeMap<Object,String> presenceCampaigns=new TreeMap<Object, String>();

	public void presenceCampaignsData(int rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			
		}
		else{
			presenceCampaigns.put(Long.parseLong(data.get(0)),"["+data.get(0)+"] "+data.get(1)+" ("+data.get(2)+")");
		}
	}
	
	public void confirm_adding_region() throws Exception{
		ArrayList<String> errors=new ArrayList<String>();
		if(this.requestParameter("NEW_REGION").equals("")){
			errors.add("NO REGION ENTERED");
		}
		if(this.requestParameter("SERVICEID").equals("")){
			errors.add("NO PRESENCE SERVICE (CAMPAIGN) SELECTED");
		}
		if(errors.isEmpty()){
			HashMap<String,Object> validationParams=new HashMap<String, Object>();
			this.importRequestParametersIntoMap(validationParams, "NEW_REGION,SERVICEID");
			
			validationParams.put("NEW_REGION",((String) validationParams.get("NEW_REGION")).toUpperCase());
			
			validationParams.put("EXISTING_REGION_ID", "0");
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS EXISTING_REGION_ID FROM  <DBUSER>.REGIONLOOKUP  WHERE REGIONNAME LIKE UPPER(:NEW_REGION)", validationParams, null);
			
			if(validationParams.get("EXISTING_REGION_ID").equals("0")){
				validationParams.put("ALLOCATED_SERVICE_ID", "0");
				validationParams.put("SERVICENAME","");
				validationParams.put("ALLOCATED_REGION","");
				
				Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(SERVICEID,0) AS ALLOCATED_SERVICE_ID,ISNULL(SERVICENAME,'') AS SERVICENAME,UPPER(ISNULL(REGIONNAME,'')) AS ALLOCATED_REGION FROM  <DBUSER>.REGIONLOOKUP  WHERE SERVICEID = :SERVICEID", validationParams, null);
				if(validationParams.get("ALLOCATED_SERVICE_ID").equals("0")){
					Database.executeDBRequest(null, "PRESENCEVIEW", "SELECT NAME AS SERVICENAME FROM <DBUSER>.SERVICE WHERE ID=:SERVICEID", validationParams, null);
					Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "INSERT INTO <DBUSER>.REGIONLOOKUP (REGIONNAME,SERVICEID,SERVICENAME) SELECT UPPER(:NEW_REGION),:SERVICEID,:SERVICENAME ", validationParams, null);
					this.replaceComponentContent("gridview");
						this.configuredRegionsView();
					this.endReplaceComponentContent();
				}
				else{
					errors.add("PRESENCE SERVICE ["+validationParams.get("ALLOCATED_SERVICE_ID")+" "+validationParams.get("SERVICENAME")+"] ALREADY ALLOCATED TO REGION ["+validationParams.get("ALLOCATED_REGION"));
				}
			}
			else{
				errors.add("REGION EXIST WITH THE SAME NAME");
			}
			validationParams.clear();
		}
		if(!errors.isEmpty()){
			this.showDialog("", "title=INVALID REGION DETAIL(s)|contentid=dlgConfigRegionDetails");
			this.replaceComponentContent("dlgConfigRegionDetails");
				this.startComplexElement("ul");
				while(!errors.isEmpty()){
					this.startComplexElement("li");
						this.respondString(errors.remove(0));
					this.endComplexElement("li");
				}
				this.endComplexElement("ul");
			this.endReplaceComponentContent();
		}
	}
	
	public void confirm_changing_region() throws Exception{
		ArrayList<String> errors=new ArrayList<String>();
		if(this.requestParameter("REGIONNAME").equals("")){
			errors.add("NO REGION ENTERED");
		}
		if(this.requestParameter("SERVICEID").equals("")){
			errors.add("NO PRESENCE SERVICE (CAMPAIGN) SELECTED");
		}
		if(errors.isEmpty()){
			HashMap<String,Object> validationParams=new HashMap<String, Object>();
			this.importRequestParametersIntoMap(validationParams, "REGIONNAME,SERVICEID,SEL_REGION_ID");
			
			validationParams.put("EXISTING_REGION_ID", "0");
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS EXISTING_REGION_ID FROM  <DBUSER>.REGIONLOOKUP  WHERE REGIONNAME LIKE UPPER(:REGIONNAME) AND ID<>:SEL_REGION_ID", validationParams, null);
			
			if(validationParams.get("EXISTING_REGION_ID").equals("0")){
				validationParams.put("ALLOCATED_SERVICE_ID", "0");
				validationParams.put("ALLOC_SERVICENAME","");
				validationParams.put("ALLOCATED_REGION","");
				
				Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(SERVICEID,0) AS ALLOCATED_SERVICE_ID,ISNULL(SERVICENAME,'') AS ALLOC_SERVICENAME,ISNULL(REGIONNAME,'') AS ALLOCATED_REGION FROM  <DBUSER>.REGIONLOOKUP  WHERE SERVICEID = :SERVICEID AND ID<>:SEL_REGION_ID", validationParams, null);
				if(validationParams.get("ALLOCATED_SERVICE_ID").equals("0")){
					validationParams.put("SERVICENAME","");
					Database.executeDBRequest(null, "PRESENCEVIEW", "SELECT NAME AS SERVICENAME FROM <DBUSER>.SERVICE WHERE ID=:SERVICEID", validationParams, null);
					Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "UPDATE <DBUSER>.REGIONLOOKUP SET REGIONNAME=:REGIONNAME,SERVICEID=:SERVICEID,SERVICENAME=:SERVICENAME WHERE ID=:SEL_REGION_ID", validationParams, null);
					Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "UPDATE <DBUSER>.CUSTOMERLOOKUP SET REGION=:REGIONNAME WHERE ID=:SEL_REGION_ID", validationParams, null);
					this.replaceComponentContent("gridview");
						this.configuredRegionsView();
					this.endReplaceComponentContent();
				}
				else{
					errors.add("PRESENCE SERVICE ["+validationParams.get("ALLOCATED_SERVICE_ID")+" "+validationParams.get("SERVICENAME")+"] ALREADY ALLOCATED TO REGION ["+validationParams.get("ALLOCATED_REGION"));
				}
			}
			else{
				errors.add("REGION EXIST WITH THE SAME NAME");
			}
			
			validationParams.clear();
		}
		if(!errors.isEmpty()){
			this.showDialog("", "title=INVALID REGION DETAIL(s)|contentid=dlgConfigRegionDetails");
			this.replaceComponentContent("dlgConfigRegionDetails");
				this.startComplexElement("ul");
				while(!errors.isEmpty()){
					this.startComplexElement("li");
						this.respondString(errors.remove(0));
					this.endComplexElement("li");
				}
				this.endComplexElement("ul");
			this.endReplaceComponentContent();
		}
	}
	
	public void confirm_deleting_region() throws Exception{
		ArrayList<String> errors=new ArrayList<String>();
		HashMap<String,Object> validationParams=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(validationParams, "SEL_REGION_ID,REGIONNAME");
		validationParams.put("ALLOCATED_CALLER", "0");
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS ALLOCATED_CALLER FROM <DBUSER>.CUSTOMERLOOKUP WHERE REGIONID=:SEL_REGION_ID", validationParams, null);
		if(!validationParams.get("ALLOCATED_CALLER").equals("0")){
			errors.add("REGION IS STILL IN USE");
		}
		if(errors.isEmpty()){
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "DELETE FROM <DBUSER>.REGIONLOOKUP WHERE ID=:SEL_REGION_ID", validationParams, null);
			this.replaceComponentContent("gridview");
				this.configuredRegionsView();
			this.endReplaceComponentContent();
		}
		if(!errors.isEmpty()){
			this.showDialog("", "title=UNABLE TO DELETE REGION DETAIL(s)|contentid=dlgConfigRegionDetails");
			this.replaceComponentContent("dlgConfigRegionDetails");
				this.startComplexElement("ul");
				while(!errors.isEmpty()){
					this.startComplexElement("li");
						this.respondString(errors.remove(0));
					this.endComplexElement("li");
				}
				this.endComplexElement("ul");
			this.endReplaceComponentContent();
		}
	}
	
	public void configure_callers() throws Exception{
		this.startElement("div", "class=ui-widget-header|style=font-size:0.8em", true);
			this.respondString("CONFIGURED CALLER(s)");
		this.endElement("div", true);
		this.startElement("div", "class=ui-widget-content|id=gridview", true);
			this.configuredCallersView();
		this.endElement("div", true);
	}
	
	public void configuredCallersView() throws Exception{
		String selRegionID=this.requestParameter("SEL_REGION_ID");
		selRegionID=selRegionID.equals("")?"0":selRegionID;
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT ID REGIONID,REGIONNAME FROM <DBUSER>.REGIONLOOKUP ORDER BY REGIONNAME",null, this,"regionsData");
		this.startTable();
			this.startRow();
				this.startCell();
					this.fieldLabel("REGION");
				this.endCell();
				this.startCell();
					this.fieldInput("SEL_REGION_ID",selRegionID,"select",true,"".split("[|]"),this.regions);
				this.endCell();
				this.startCell();
					action("VIEW MAPPED CONTACT(s)", "configuredCallersView", "", "", "gridview", "", "", "");
				this.endCell();
			this.endRow();
		this.endTable();
		if(!selRegionID.equals("0")){
			action("ADD NEW CONTACT", "add_contact_details", "", "", "callersgrid", "", "", "");
		}
		this.startComplexElement("div", "id=callersgrid");
			this.callersgrid();
		this.endComplexElement("div");
	}
	
	public void callersgrid() throws Exception {
		String selRegionID=this.requestParameter("SEL_REGION_ID");
		selRegionID=selRegionID.equals("")?"0":selRegionID;
		this.startTable();
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT [ID] ,[NUMBER],[CALLABLENUMBER] ,[NAME] ,[COMPANYNAME],[REGION] FROM <DBUSER>.[CUSTOMERLOOKUP] WHERE REGIONID="+selRegionID+" ORDER BY REGION, NAME,COMPANYNAME", null, this,"configuredCallersViewData");
		this.endTable();
	}

	public void regionsData(int rowIndex,ArrayList<String> data,ArrayList<String> columns) throws Exception{
		if(rowIndex==0){
			
		}
		else{
			regions.put(Long.parseLong(data.get(0)),"["+data.get(0)+"] "+data.get(1));
		}
	}
	
	private TreeMap<Object,String> regions=new TreeMap<Object, String>();
	
	public void configuredCallersViewData(int rowIndex,ArrayList<String> data,ArrayList<String> cols) throws Exception{
		this.startRow();
		boolean doneFirstCell=false;
		if(rowIndex==0){
			for(String confdata:cols){
				if(!doneFirstCell){
					doneFirstCell=true;
					this.startCell("style=font-size:0.8em".split("[|]"));
					this.endCell();
				}
				else{
					this.startColumn("font-size:0.8em");
						this.respondString(confdata);
					this.endColumn();
				}
			}
			this.startCell("style=font-size:0.8em".split("[|]"));
			this.endCell();
		}
		else{
			for(String confdata:data){
				if(!doneFirstCell){
					doneFirstCell=true;
					this.startCell("style=font-size:0.8em".split("[|]"));
						this.action("...", "edit_configured_caller", "", "", "callersgrid", "", "", "SEL_CONTACT_ID="+data.get(0));
					this.endCell();
				}
				else{
					this.startCell("style=font-size:0.8em;border:solid 1px".split("[|]"));
						this.respondString(confdata);
					this.endCell();
				}
			}
			this.startCell("style=font-size:0.8em".split("[|]"));
				this.action("del", "delete_configured_caller", "", "", "callersgrid", "", "", "SEL_CONTACT_ID="+data.get(0));
			this.endCell();
		}
		this.endRow();
	}
	
	public void add_contact_details() throws Exception{
		HashMap<String,Object> validCallerDetails=new HashMap<String, Object>();
		validCallerDetails.put("REGIONNAME", "");
		this.importRequestParametersIntoMap(validCallerDetails, "SEL_REGION_ID,REGIONNAME");
		if(validCallerDetails.get("REGIONNAME").equals("")){
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT REGIONNAME FROM <DBUSER>.REGIONLOOKUP WHERE ID=:SEL_REGION_ID", validCallerDetails, null);
		}
		this.startComplexElement("div","class=ui-widget-header|style=font-size:0.8em");
			this.fieldHidden("SEL_REGION_ID",(String) validCallerDetails.get("SEL_REGION_ID"));
			this.fieldHidden("REGIONNAME",(String) validCallerDetails.get("REGIONNAME"));
			this.respondString((String)validCallerDetails.get("REGIONNAME"));
		this.endComplexElement("div");
		
		this.startTable();
			for(String detailCol:"NUMBER|NAME|COMPANYNAME".split("[|]")){
				this.startRow();
					this.startColumn("font-size:0.6em;text-align:right");
						this.respondString(detailCol);
					this.endColumn();
					this.startCell();
						this.fieldInput("NEW_"+detailCol, "", "text", true,"style=font-size:0.8em");
					this.endCell();
				this.endRow();
			}
		this.endTable();
		
		this.action("CONFIRM ADDING CONTACT", "confirm_adding_contact", "", "", "", "", "", "");
		this.action("CANCEL", "callersgrid", "", "", "callersgrid", "", "", "");
	}
	
	public void confirm_adding_contact() throws Exception{
		ArrayList<String> errors=new ArrayList<String>();
		HashMap<String,Object> validContactDetails=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(validContactDetails, "SEL_REGION_ID,REGIONNAME,NEW_NUMBER,NEW_NAME,NEW_COMPANYNAME");
		if(((String)validContactDetails.get("NEW_NAME")).trim().equals("")){
			errors.add("NO CONTACT NAME PROVIDED");
		}
		else{
			validContactDetails.put("NEW_NAME",((String)validContactDetails.get("NEW_NAME")).trim());
		}
		
		String number=((String)validContactDetails.get("NEW_NUMBER")).trim();
		String callableNumber=this.formatNumber(number);
		if(number.equals("")){
			errors.add("NO CONTACT NUMBER PROVIDED");
		}
		else{
			if(callableNumber.length()<10||(callableNumber.length()>20||number.length()>20)){
				errors.add("CONTACT NUMBER INVALID ["+number+" -> ["+callableNumber+"]");
			}
			else{
				validContactDetails.put("CALLABLENUMBER", callableNumber);
			}
		}
		
		if(errors.isEmpty()){
			validContactDetails.put("EXISTING_CONTACT_ID", "0");
			
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS EXISTING_CONTACT_ID FROM <DBUSER>.CUSTOMERLOOKUP WHERE NAME LIKE UPPER(:NEW_NAME) AND COMPANYNAME=UPPER(:NEW_COMPANYNAME) AND REGIONID=:SEL_REGION_ID", validContactDetails, null);
			if(!validContactDetails.get("EXISTING_CONTACT_ID").equals("0")){
				errors.add("CONTACT ALLREADY ALLOCATED TO REGION");
			}
			else{
				validContactDetails.put("ALLOCATED_CONTACT_ID", "0");
				validContactDetails.put("ALLOCATED_CONTACT_NAME","");
				validContactDetails.put("ALLOCATED_CONTACT_COMPANYNAME","");
				Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS ALLOCATED_CONTACT_ID,NAME AS ALLOCATED_CONTACT_NAME,COMPANYNAME AS ALLOCATED_CONTACT_COMPANYNAME FROM <DBUSER>.CUSTOMERLOOKUP WHERE NOT (NAME LIKE UPPER(:NEW_NAME) AND COMPANYNAME=UPPER(:NEW_COMPANYNAME)) AND CALLABLENUMBER=:CALLABLENUMBER AND REGIONID=:SEL_REGION_ID", validContactDetails, null);
				if(!validContactDetails.get("ALLOCATED_CONTACT_ID").equals("0")){
					errors.add("NUMBER ALREADY ALLOCATED TO A DIFFERENT CONTACT ["+validContactDetails.get("ALLOCATED_CONTACT_NAME")+" ("+validContactDetails.get("ALLOCATED_CONTACT_COMPANYNAME")+")]: ["+number+" -> "+callableNumber+"]");
				}
			}
		}
		if(!errors.isEmpty()){
			this.showDialog("", "title=INVALID CONTACT DETAIL(s)|contentid=dlgConfigCallerDetails");
			this.replaceComponentContent("dlgConfigCallerDetails");
				this.startComplexElement("ul");
				while(!errors.isEmpty()){
					this.startComplexElement("li");
						this.respondString(errors.remove(0));
					this.endComplexElement("li");
				}
				this.endComplexElement("ul");
			this.endReplaceComponentContent();
		}
		else{
			validContactDetails.put("NEW_COMPANYNAME",((String)validContactDetails.get("NEW_COMPANYNAME")).trim());
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "INSERT INTO <DBUSER>.CUSTOMERLOOKUP (NAME,NUMBER,CALLABLENUMBER,COMPANYNAME,REGIONID,REGION) SELECT UPPER(:NEW_NAME),:NEW_NUMBER,:CALLABLENUMBER,UPPER(:NEW_COMPANYNAME),:SEL_REGION_ID,UPPER(:REGIONNAME)", validContactDetails, null);
			this.replaceComponentContent("callersgrid");
				this.callersgrid();
			this.endReplaceComponentContent();
		}
	}

	private String formatNumber(String number) {
		if((number=number==null?"":number.trim()).equals("")) return "";
		String formatNumber="";
		for(char cn:number.toCharArray()){
			if(cn>='0'&&cn<='9'){
				formatNumber+=(cn+"");
			}
		}
		
		if(formatNumber.startsWith("027")){
			formatNumber="0"+formatNumber.substring(3,formatNumber.length());
		}
		else if(formatNumber.startsWith("27")){
			formatNumber="0"+formatNumber.substring(2,formatNumber.length());
		}
		
		return formatNumber;
	}
	
	public void edit_configured_caller() throws Exception{
		HashMap<String,Object> currentContactDetails=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(currentContactDetails, "SEL_REGION_ID,SEL_CONTACT_ID");
		currentContactDetails.put("REGIONNAME", "");
		currentContactDetails.put("NUMBER", "");
		currentContactDetails.put("NAME", "");
		currentContactDetails.put("COMPANYNAME", "");
		
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT NUMBER,NAME,COMPANYNAME,REGION AS REGIONNAME,REGIONID AS SEL_REGION_ID FROM <DBUSER>.CUSTOMERLOOKUP WHERE ID=:SEL_CONTACT_ID AND REGIONID=:SEL_REGION_ID", currentContactDetails,null);
		this.startComplexElement("div","class=ui-widget-header|style=font-size:0.8em");
			this.fieldHidden("SEL_REGION_ID",(String) currentContactDetails.get("SEL_REGION_ID"));
			this.fieldHidden("REGIONNAME",(String) currentContactDetails.get("REGIONNAME"));
			this.respondString((String)currentContactDetails.get("REGIONNAME"));
		this.endComplexElement("div");
		
		
		this.fieldHidden("SEL_CONTACT_ID",(String) currentContactDetails.get("SEL_CONTACT_ID"));
		this.startTable();
			for(String detailCol:"NUMBER|NAME|COMPANYNAME".split("[|]")){
				this.startRow();
					this.startColumn("font-size:0.6em;text-align:right");
						this.respondString(detailCol);
					this.endColumn();
					this.startCell();
						this.fieldInput(detailCol,(String) (currentContactDetails.containsKey(detailCol)?currentContactDetails.get(detailCol):""), "text", true,"style=font-size:0.8em");
					this.endCell();
				this.endRow();
			}
		this.endTable();
		
		this.action("CONFIRM CHANGING CONTACT", "confirm_changing_contact", "", "", "", "", "", "");
		this.action("CANCEL", "callersgrid", "", "", "callersgrid", "", "", "");
	}
	
	public void delete_configured_caller() throws Exception{
		HashMap<String,Object> currentContactDetails=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(currentContactDetails, "SEL_REGION_ID,SEL_CONTACT_ID");
		currentContactDetails.put("REGIONNAME", "");
		currentContactDetails.put("NUMBER", "");
		currentContactDetails.put("NAME", "");
		currentContactDetails.put("COMPANYNAME", "");
		
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT NUMBER,NAME,COMPANYNAME,REGION AS REGIONNAME,REGIONID AS SEL_REGION_ID FROM <DBUSER>.CUSTOMERLOOKUP WHERE ID=:SEL_CONTACT_ID AND REGIONID=:SEL_REGION_ID", currentContactDetails,null);
		this.startComplexElement("div","class=ui-widget-header|style=font-size:0.8em");
			this.fieldHidden("SEL_REGION_ID", (String)currentContactDetails.get("SEL_REGION_ID"));
			this.fieldHidden("REGIONNAME",(String) currentContactDetails.get("REGIONNAME"));
			this.respondString((String)currentContactDetails.get("REGIONNAME"));
		this.endComplexElement("div");
		
		
		this.fieldHidden("SEL_CONTACT_ID", (String)currentContactDetails.get("SEL_CONTACT_ID"));
		this.startTable();
			for(String detailCol:"NUMBER|NAME|COMPANYNAME".split("[|]")){
				this.startRow();
					this.startColumn("font-size:0.6em;text-align:right");
						this.respondString(detailCol);
					this.endColumn();
					this.startCell();
						this.fieldInput(detailCol,(String) (currentContactDetails.containsKey(detailCol)?currentContactDetails.get(detailCol):""), "text", false,"style=font-size:0.8em");
					this.endCell();
				this.endRow();
			}
		this.endTable();
		
		this.action("CONFIRM DELETING CONTACT", "confirm_deleting_contact", "", "", "", "", "", "");
		this.action("CANCEL", "callersgrid", "", "", "callersgrid", "", "", "");
	}
	
	public void confirm_changing_contact() throws Exception{
		ArrayList<String> errors=new ArrayList<String>();
		HashMap<String,Object> validContactDetails=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(validContactDetails, "SEL_REGION_ID,REGIONNAME,SEL_CONTACT_ID,NUMBER,NAME,COMPANYNAME");
		if(((String)validContactDetails.get("NAME")).trim().equals("")){
			errors.add("NO CONTACT NAME PROVIDED");
		}
		else{
			validContactDetails.put("NAME",((String)validContactDetails.get("NAME")).trim());
		}
		
		String number=((String)validContactDetails.get("NUMBER")).trim();
		String callableNumber=this.formatNumber(number);
		if(number.equals("")){
			errors.add("NO CONTACT NUMBER PROVIDED");
		}
		else{
			if(callableNumber.length()<10||(callableNumber.length()>20||number.length()>20)){
				errors.add("CONTACT NUMBER INVALID ["+number+" -> ["+callableNumber+"]");
			}
			else{
				validContactDetails.put("CALLABLENUMBER", callableNumber);
			}
		}
		
		if(errors.isEmpty()){
			validContactDetails.put("EXISTING_CONTACT_ID", "0");
			
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS EXISTING_CONTACT_ID FROM <DBUSER>.CUSTOMERLOOKUP WHERE NAME LIKE UPPER(:NAME) AND COMPANYNAME=:COMPANYNAME AND REGIONID=:SEL_REGION_ID AND ID<>:SEL_CONTACT_ID", validContactDetails, null);
			if(!validContactDetails.get("EXISTING_CONTACT_ID").equals("0")){
				errors.add("DIFFERENT CONTACT WITH THE SAME NAME ALLREADY ALLOCATED TO REGION");
			}
			else{
				validContactDetails.put("ALLOCATED_CONTACT_ID", "0");
				validContactDetails.put("ALLOCATED_CONTACT_NAME","");
				validContactDetails.put("ALLOCATED_CONTACT_COMPANYNAME","");
				Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "SELECT TOP 1 ISNULL(ID,0) AS ALLOCATED_CONTACT_ID,NAME AS ALLOCATED_CONTACT_NAME,COMPANYNAME AS ALLOCATED_CONTACT_COMPNYNAME FROM <DBUSER>.CUSTOMERLOOKUP WHERE NAME NOT LIKE UPPER(:NAME) AND CALLABLENUMBER=:CALLABLENUMBER AND REGIONID=:SEL_REGION_ID AND ID<>:SEL_CONTACT_ID", validContactDetails, null);
				if(!validContactDetails.get("ALLOCATED_CONTACT_ID").equals("0")){
					errors.add("NUMBER ALREADY ALLOCATED TO A DIFFERENT CONTACT ["+validContactDetails.get("ALLOCATED_CONTACT_NAME")+" ("+validContactDetails.get("ALLOCATED_CONTACT_COMPANYNAME")+")]: ["+number+" -> "+callableNumber+"]");
				}
			}
		}
		if(!errors.isEmpty()){
			this.showDialog("", "title=INVALID CONTACT DETAIL(s)|contentid=dlgConfigCallerDetails");
			this.replaceComponentContent("dlgConfigCallerDetails");
				this.startComplexElement("ul");
				while(!errors.isEmpty()){
					this.startComplexElement("li");
						this.respondString(errors.remove(0));
					this.endComplexElement("li");
				}
				this.endComplexElement("ul");
			this.endReplaceComponentContent();
		}
		else{
			validContactDetails.put("COMPANYNAME",((String)validContactDetails.get("COMPANYNAME")).trim());
			Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "UPDATE <DBUSER>.CUSTOMERLOOKUP SET NAME=UPPER(:NAME),NUMBER=:NUMBER,CALLABLENUMBER=:CALLABLENUMBER,COMPANYNAME=:COMPANYNAME,REGIONID=:SEL_REGION_ID,REGION=UPPER(:REGIONNAME) WHERE ID=:SEL_CONTACT_ID", validContactDetails, null);
			this.replaceComponentContent("callersgrid");
				this.callersgrid();
			this.endReplaceComponentContent();
		}
	}
	
	public void confirm_deleting_contact() throws Exception{
		HashMap<String,Object> validationParams=new HashMap<String, Object>();
		this.importRequestParametersIntoMap(validationParams, "SEL_REGION_ID,SEL_CONTACT_ID");
		
		Database.executeDBRequest(null, "CONFIGURECALLERDETAILS", "DELETE FROM <DBUSER>.CUSTOMERLOOKUP WHERE ID=:SEL_CONTACT_ID AND REGIONID=:SEL_REGION_ID", validationParams, null);
		this.replaceComponentContent("callersgrid");
			this.callersgrid();
		this.endReplaceComponentContent();
		
	}
}

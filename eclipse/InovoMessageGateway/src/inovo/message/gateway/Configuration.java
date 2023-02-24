package inovo.message.gateway;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import inovo.db.Database;
import inovo.web.InovoHTMLPageWidget;
import inovo.web.InovoWebWidget;

public class Configuration extends InovoHTMLPageWidget {

	public Configuration(InovoWebWidget parentWidget, InputStream inStream) {
		super(parentWidget, inStream);
	}
	
	@Override
	public void pageContent() throws Exception {
		this.startTable(null);
			this.startCell(null);
				this.fieldLabel("KEY PHRASE");
			this.endCell();
			this.startCell(null);
				this.fieldInput("ADHOCKKEYPHRASE", "", "text", true, null);
			this.endCell();
			this.startCell(null);
				this.action("ADD KEY PHRASE", "addkeyphrase", "", "", "","", "ui-icon-plus", "");
			this.endCell();
			this.startCell(null);
				this.action("SEARCH KEY PHRASE", "searchkeyphrase", "", "", "","", "ui-icon-search", "");
			this.endCell();
		this.endTable();
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("DEFAULT SERVICE AND LOAD");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(new String[]{"id=defaultserviceloadinfo"});
					this.defaultserviceloadinfo();
				this.endCell();
			this.endRow();
		this.endTable();
		this.startTable(null);
			this.startCell(new String[]{"id=registeredkeyphrases"});this.endCell();
		this.endTable();
	}
	
	public void defaultserviceloadinfo() throws Exception {
		HashMap<String,String> defaultServiceLoadInfoMap=new HashMap<String,String>();
		defaultServiceLoadInfoMap.put("DEFAULTKEYPHRASEID", "0");
		defaultServiceLoadInfoMap.put("DEFAULTSERVICEID", "0");
		defaultServiceLoadInfoMap.put("DEFAULTLOADID", "0");
		defaultServiceLoadInfoMap.put("DEFAULTPRIORITY", "100");
		defaultServiceLoadInfoMap.put("ENABLEDEFAULTKEYPHRASE", "FALSE");
		Database.executeDBRequest(null,"MESSAGEGATEWAY", "SELECT TOP 1 ID AS DEFAULTKEYPHRASEID, ISNULL(SERVICEID,0) AS DEFAULTSERVICEID,ISNULL(LOADID,0) AS DEFAULTLOADID,ISNULL(PRIORITY,100) AS DEFAULTPRIORITY,UPPER(ISNULL(ENABLEKEYPHRASE,'FALSE')) AS ENABLEDEFAULTKEYPHRASE  FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE ISNULL(KEYPHRASE,'')=''", defaultServiceLoadInfoMap,null);
		boolean enableFields=(!defaultServiceLoadInfoMap.get("ENABLEDEFAULTKEYPHRASE").toUpperCase().equals("TRUE"));
		if(!enableFields&&defaultServiceLoadInfoMap.get("DEFAULTKEYPHRASEID").equals("0")){
			enableFields=true;
		}
		this.fieldHidden("DEFAULTKEYPHRASEID", defaultServiceLoadInfoMap.get("DEFAULTKEYPHRASEID"));
		this.startTable(null);
			this.startRow(null);
				this.startCell(new String[]{"style=vertical-align:top"});
					this.fieldLabel("DEFAULT SERVICE NR");
				this.endCell();
				this.startCell(new String[]{"style=vertical-align:top"});
					this.fieldInput("DEFAULTSERVICEID", defaultServiceLoadInfoMap.get("DEFAULTSERVICEID"), "text", enableFields, null);
				this.endCell();
				this.startCell(new String[]{"style=vertical-align:top"});
					this.fieldLabel("DEFAULT SERVICE LOAD NR");
				this.endCell();
				this.startCell(new String[]{"style=vertical-align:top"});
					this.fieldInput("DEFAULTLOADID",  defaultServiceLoadInfoMap.get("DEFAULTLOADID"), "text", enableFields, null);
				this.endCell();
				this.startCell(new String[]{"style=vertical-align:top"});
					this.fieldLabel("DEFAULT CALL PRIORITY NR");
				this.endCell();
				this.startCell(new String[]{"style=vertical-align:top"});
					this.fieldInput("DEFAULTPRIORITY",  defaultServiceLoadInfoMap.get("DEFAULTPRIORITY"), "text", enableFields, null);
				this.endCell();
			this.endRow();
			this.startRow(null);
				this.startCell(new String[]{"colspan=6"});
					if(enableFields){
						this.action("UPDATE DEFAULT(S)", "updatedefaultserviceload", "mainform", this.getClass().getName(), "defaultserviceloadinfo", "", "ui-icon-check", "");
					}
					if(enableFields&&!defaultServiceLoadInfoMap.get("DEFAULTKEYPHRASEID").equals("0")){
						this.action("ENABLE DEFAULT", "enabledefaultserviceload", "mainform", this.getClass().getName(), "defaultserviceloadinfo", "", "ui-icon-play", "");
					}
					else if(!enableFields){
						this.action("DISABLE DEFAULT", "disabledefaultserviceload", "mainform", this.getClass().getName(), "defaultserviceloadinfo", "", "ui-icon-stop", "");
					}
				this.endCell();
			this.endRow();
		this.endTable();
	}
	
	public void updatedefaultserviceload() throws Exception{
		HashMap<String,String> defaultSettings=new HashMap<String,String>();
		defaultSettings.put("DEFAULTKEYPHRASEID", "0");
		this.importRequestParametersIntoMap(defaultSettings, "DEFAULTSERVICEID,DEFAULTLOADID,ENABLEDEFAULTKEYPHRASE,DEFAULTPRIORITY");
		defaultSettings.put("DEFAULTSERVICEID", (defaultSettings.get("DEFAULTSERVICEID").equals("")?"0":defaultSettings.get("DEFAULTSERVICEID")));
		defaultSettings.put("DEFAULTLOADID", (defaultSettings.get("DEFAULTLOADID").equals("")?"0":defaultSettings.get("DEFAULTLOADID")));
		defaultSettings.put("DEFAULTPRIORITY", (defaultSettings.get("DEFAULTPRIORITY").equals("")?"100":defaultSettings.get("DEFAULTPRIORITY")));
		defaultSettings.put("ENABLEDEFAULTKEYPHRASE", (defaultSettings.get("ENABLEDEFAULTKEYPHRASE").equals("")?"FALSE":defaultSettings.get("ENABLEDEFAULTKEYPHRASE")));
		Database.executeDBRequest(null,"MESSSAGEGATEWAY", "SELECT TOP 1 ID AS DEFAULTKEYPHRASEID FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE ISNULL(KEYPHRASE,'')=''", defaultSettings,null);
		
		defaultSettings.put("KEYPHRASECOUNT", "0");
		Database.executeDBRequest(null,"MESSAGEGATEWAY", "SELECT COUNT(*) AS KEYPHRASECOUNT FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE SERVICEID=:DEFAULTSERVICEID AND LOADID=:DEFAULTLOADID AND ID NOT IN(:DEFAULTKEYPHRASEID)", defaultSettings,null);
		if(defaultSettings.get("KEYPHRASECOUNT").equals("0")){
			if(defaultSettings.get("DEFAULTKEYPHRASEID").equals("0")){
				Database.executeDBRequest(null,"MESSAGEGATEWAY", "INSERT INTO <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] ([KEYPHRASE],[CREATEDATETIME],[SERVICEID],[LOADID],[PARENTKEYPHRASEID],[PRIORITY],[ENABLEKEYPHRASE]) VALUES('',NOW(),:DEFAULTSERVICEID,:DEFAULTLOADID,0,:DEFAULTPRIORITY,:ENABLEDEFAULTKEYPHRASE)", defaultSettings,null);
			}
			else{
				Database.executeDBRequest(null,"MESSAGEGATEWAY", "UPDATE <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] SET SERVICEID=:DEFAULTSERVICEID, LOADID=:DEFAULTLOADID WHERE ID=:DEFAULTKEYPHRASEID",defaultSettings,null);
			}
		}
		else{
			this.showDialog("", new String[]{"title=DEFUALT SERVICE AND LOAD","content=SERVICE AND LOAD NR ALREADY ALLOCATED TO ANOTHER KEYPHRASE"});
		}
		defaultserviceloadinfo();
	}
	
	public void enabledefaultserviceload() throws Exception{
		Database.executeDBRequest(null,"MESSAGEGATEWAY", "UPDATE <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] SET [ENABLEKEYPHRASE]='TRUE' WHERE [ID]="+this.requestParameter("DEFAULTKEYPHRASEID"), null,null);
		defaultserviceloadinfo();
	}
	
	public void disabledefaultserviceload() throws Exception{
		Database.executeDBRequest(null,"MESSAGEGATEWAY", "UPDATE <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] SET [ENABLEKEYPHRASE]='FALSE' WHERE [ID]="+this.requestParameter("DEFAULTKEYPHRASEID"), null,null);
		defaultserviceloadinfo();
	}

	public void addkeyphrase() throws Exception{
		HashMap<String,String> addedParams=new HashMap<String,String>();
		importRequestParametersIntoMap(addedParams, "ADHOCKKEYPHRASE");
		if(addedParams.get("ADHOCKKEYPHRASE").equals("")){
			this.showDialog("", new String[]{"title=ADD NEW KEYPHRASE","content=NO KEY PHRASE ENTERED"});
		}
		else{
			addedParams.put("ADHOCKKEYPHRASECOUNT", "0");
			Database.executeDBRequest(null,"MESSAGEGATEWAY", "SELECT COUNT(*) AS ADHOCKKEYPHRASECOUNT FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE UPPER([KEYPHRASE]) LIKE UPPER(:ADHOCKKEYPHRASE)", addedParams,null);
			if(addedParams.get("ADHOCKKEYPHRASECOUNT").equals("0")){
				this.showDialog("", new String[]{"title=ADD NEW KEYPHRASE","content=CONFIRM ADDING NEW KEYPHRASE","BUTTON:CONFIRM=command=confirmAddingKeyphrase\r\nform=mainform","BUTTON:CANCEL="});
			}
			else{
				this.showDialog("", new String[]{"title=ADD NEW KEYPHRASE","content=KEY PHRASE ENTERED ALREADY EXIST"});
				searchkeyphrase();
			}
		}
	}
	
	public void confirmAddingKeyphrase() throws Exception{
		HashMap<String,String> addedParams=new HashMap<String,String>();
		importRequestParametersIntoMap(addedParams, "ADHOCKKEYPHRASE");
		Database.executeDBRequest(null,"MESSAGEGATEWAY","INSERT INTO <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] ([KEYPHRASE]) VALUES(UPPER(:ADHOCKKEYPHRASE))",addedParams,null);
		searchkeyphrase();
	}
	
	public void searchkeyphrase() throws Exception{
		this.replaceComponentContent("registeredkeyphrases");
		HashMap<String,String> searchParams=new HashMap<String,String>();
		importRequestParametersIntoMap(searchParams, "ADHOCKKEYPHRASE");
		TreeMap<Integer,ArrayList<String>> registeredkeyphrasesset=new TreeMap<Integer, ArrayList<String>>(); 
		Database.executeDBRequest(registeredkeyphrasesset, "MESSAGEGATEWAY", "SELECT [ID],[KEYPHRASE] FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE UPPER(ISNULL(KEYPHRASE,''))<>'' AND UPPER([KEYPHRASE]) LIKE (UPPER(:ADHOCKKEYPHRASE)+'%')", searchParams,null);
		
		this.startTable(null);
			this.startRow(null);
				this.startColumn(null);
					this.respondString("REGISTERED KEY PHRASES");
				this.endColumn();
			this.endRow();
			this.startRow(null);
				this.startCell(null);
					this.startTable(null);
						this.startRow(null);
							this.startCell(new String[]{"style=vertical-align:top;height:500px;overflow:scroll;background-color:#A9A9A9"});
								this.startTable(null);
									for(int rowindex:registeredkeyphrasesset.keySet()){
										if(rowindex==0) continue;
										this.startRow(null);
											this.startCell(new String[]{"nowrap=nowrap"});
												this.action(Database.rowField(registeredkeyphrasesset, rowindex, "KEYPHRASE"), "selectkeyphrase", "mainform",this.getClass().getName(), "", "", "ui-icon-note", "SELECTEDKEYPHRASEID="+Database.rowField(registeredkeyphrasesset, rowindex, "ID"));
											this.endCell();
										this.endRow();
									}
								this.endTable();
							this.endCell();
							this.startCell(new String[]{"style=vertical-align:top","id=selectedkeyphrase"});
							this.endCell();
						this.endRow();
					this.endTable();
				this.endCell();
			this.endRow();
		this.endTable();
				
		Database.cleanupDataset(registeredkeyphrasesset);
		registeredkeyphrasesset=null;
		this.endReplaceComponentContent();
	}
	
	public void selectkeyphrase() throws Exception{
		String keyphraseid=this.requestParameter("SELECTEDKEYPHRASEID");
		if(keyphraseid.equals("")) keyphraseid=this.requestParameter("KEYPHRASEID");
		TreeMap<Integer, ArrayList<String>> keyphrasesettingsSet=new TreeMap<Integer, ArrayList<String>>();
		Database.executeDBRequest(keyphrasesettingsSet,"MESSAGEGATEWAY", "SELECT * FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE [ID]="+keyphraseid, null,null);
		HashMap<String,String> keyphrasesettings=Database.rowData(keyphrasesettingsSet, 1);
		Database.cleanupDataset(keyphrasesettingsSet);
		keyphrasesettingsSet=null;
		
		keyphrasesettings.put("ENABLEKEYPHRASE", keyphrasesettings.get("ENABLEKEYPHRASE").toUpperCase());
		if(!keyphrasesettings.get("ENABLEKEYPHRASE").toUpperCase().equals("TRUE")) keyphrasesettings.put("ENABLEKEYPHRASE","FALSE");
		boolean enabledfields=(!keyphrasesettings.get("ENABLEKEYPHRASE").equals("TRUE"));
		
		this.replaceComponentContent("selectedkeyphrase");
		this.fieldHidden("KEYPHRASEID", keyphraseid);
			this.startTable(null);
				this.startRow(null);
					this.startCell(null);
						this.fieldLabel("KEYPHRASE");
					this.endCell();
					this.startCell(null);
						this.fieldInput("KEYPHRASE", keyphrasesettings.get("KEYPHRASE").toUpperCase(), "text", enabledfields, null);
					this.endCell();
				this.endRow();
				this.startRow(null);
					this.startCell(null);
						this.fieldLabel("SERVICE NR");
					this.endCell();
					this.startCell(null);
						this.fieldInput("SERVICEID", keyphrasesettings.get("SERVICEID").toUpperCase(), "text", enabledfields, null);
					this.endCell();
				this.endRow();
				this.startRow(null);
					this.startCell(null);
						this.fieldLabel("LOAD NR");
					this.endCell();
					this.startCell(null);
						this.fieldInput("LOADID", keyphrasesettings.get("LOADID").toUpperCase(), "text", enabledfields, null);
					this.endCell();
				this.endRow();
				this.startRow(null);
					this.startCell(null);
						this.fieldLabel("QUEUED PRIORITY");
					this.endCell();
					this.startCell(null);
						this.fieldInput("PRIORITY",(keyphrasesettings.get("PRIORITY").equals("")?"100": keyphrasesettings.get("PRIORITY").toUpperCase()), "text", enabledfields, null);
					this.endCell();
				this.endRow();
			this.endTable();
			this.fieldHidden("ENABLEKEYPHRASE", keyphrasesettings.get("ENABLEKEYPHRASE"));
			this.startTable(null);
				this.startRow(null);
					if(enabledfields){
						this.startCell(null);
							this.action("APPLY CHANGES", "applykeyphrasechanges", "mainform", "", "", "", "ui-icon-check", "");
						this.endCell();
					}
					this.startCell(null);
						this.action((enabledfields?"ENABLE KEY PHRASE":"DISABLE KEY PHRASE"),(enabledfields?"enablekeyphrase":"disablekeyphrase"), "mainform", "", "", "",(enabledfields?"ui-icon-play":"ui-icon-stop"), "");
					this.endCell();
				this.endRow();
			this.endTable();
		this.endReplaceComponentContent();
		keyphrasesettings.clear();
	}
	
	public void applyKeyphraseChanges() throws Exception{
		HashMap<String,String> keyphrasesettings=new HashMap<String,String>();
		this.importRequestParametersIntoMap(keyphrasesettings, "KEYPHRASEID,KEYPHRASE,SERVICEID,LOADID,PRIORITY,ENABLEKEYPHRASE");
		keyphrasesettings.put("KEYPHRASE", keyphrasesettings.get("KEYPHRASE").toUpperCase());
		keyphrasesettings.put("PRIORITY", (keyphrasesettings.get("PRIORITY").equals("")?"100":keyphrasesettings.get("PRIORITY")));
		keyphrasesettings.put("SERVICEID", (keyphrasesettings.get("SERVICEID").equals("")?"0":keyphrasesettings.get("SERVICEID")));
		keyphrasesettings.put("LOADID", (keyphrasesettings.get("LOADID").equals("")?"0":keyphrasesettings.get("LOADID")));
		keyphrasesettings.put("ENABLEKEYPHRASE",(keyphrasesettings.get("ENABLEKEYPHRASE").equals("")?"FALSE":("TRUE,FALSE".contains(keyphrasesettings.get("ENABLEKEYPHRASE").toUpperCase())?keyphrasesettings.get("ENABLEKEYPHRASE").toUpperCase():"FALSE")));
		keyphrasesettings.put("KEYPHRASECOUNT", "0");
		
		ArrayList<String> updateErrors=new ArrayList<String>();
		
		if(!keyphrasesettings.get("SERVICEID").equals("0")||!keyphrasesettings.get("LOADID").equals("0")){
			Database.executeDBRequest(null,"MESSAGEGATEWAY", "SELECT COUNT(*) AS KEYPHRASECOUNT FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE [ID] NOT IN (:KEYPHRASEID) AND UPPER(KEYPHRASE) LIKE UPPER(:KEYPHRASE)", keyphrasesettings,null);
			if(!keyphrasesettings.get("KEYPHRASECOUNT").equals("0")){
				updateErrors.add("ANOTHER KEYPHRASE ["+keyphrasesettings.get("KEYPHRASE")+"] ARLEADY EXIST");
			}
			else{
				keyphrasesettings.put("KEYPHRASECOUNT", "0");
				Database.executeDBRequest(null,"MESSAGEGATEWAY", "SELECT COUNT(*) AS KEYPHRASECOUNT FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE [ID] NOT IN (:KEYPHRASEID) AND UPPER(KEYPHRASE) <> UPPER(:KEYPHRASE) AND SERVICEID=:SERVICEID AND LOADID=:LOADID", keyphrasesettings,null);
				if(!keyphrasesettings.get("KEYPHRASECOUNT").equals("0")){
					updateErrors.add("ANOTHER KEYPHRASE ARLEADY ALLOCATED TO THE SAME SERVICE NR (SERVICEID) AND LOAD NR(LOADID)");
				}
			}
		}
		if(updateErrors.isEmpty()){
			Database.executeDBRequest(null,"MESSAGEGATEWAY", "UPDATE <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] SET KEYPHRASE=UPPER(:KEYPHRASE),SERVICEID=:SERVICEID,LOADID=:LOADID,PRIORITY=:PRIORITY WHERE ID=:KEYPHRASEID", keyphrasesettings,null);
			selectkeyphrase();
		}
		else{
			String updatekeyphraseerrorid="updatekeyphraseerror"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			this.showDialog("", new String[]{"title=CHANGE KEYPHRASE DETAILS","contentid="+updatekeyphraseerrorid});
			this.replaceComponentContent(updatekeyphraseerrorid);
				this.startElement("ul",new String[]{"style=font-weight:bold;color:#DE5A5A"}, true);
					while(!updateErrors.isEmpty()){
						this.startElement("li", null, true);this.respondString(encodeHTML(updateErrors.remove(0)));this.endElement("li", true);
					}
				this.endElement("ul", true);
			this.endReplaceComponentContent();
		}
	}
	
	public void enableKeyphrase() throws Exception{
		HashMap<String,String> keyphrasesettings=new HashMap<String,String>();
		
		TreeMap<Integer,ArrayList<String>> keyphrasesettingsSet=new TreeMap<Integer, ArrayList<String>>();
		Database.executeDBRequest(keyphrasesettingsSet, "MESSAGEGATEWAY", "SELECT * FROM <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] WHERE ID="+this.requestParameter("KEYPHRASEID"), null,null);
		keyphrasesettings.putAll(Database.rowData(keyphrasesettingsSet,1));
		Database.cleanupDataset(keyphrasesettingsSet);
		keyphrasesettingsSet=null;
		this.importRequestParametersIntoMap(keyphrasesettings, "KEYPHRASEID,KEYPHRASE,SERVICEID,LOADID,PRIORITY,ENABLEKEYPHRASE");
		keyphrasesettings.put("KEYPHRASE", keyphrasesettings.get("KEYPHRASE").toUpperCase());
		keyphrasesettings.put("PRIORITY", (keyphrasesettings.get("PRIORITY").equals("")?"100":keyphrasesettings.get("PRIORITY")));
		keyphrasesettings.put("SERVICEID", (keyphrasesettings.get("SERVICEID").equals("")?"0":keyphrasesettings.get("SERVICEID")));
		keyphrasesettings.put("LOADID", (keyphrasesettings.get("LOADID").equals("")?"0":keyphrasesettings.get("LOADID")));
		keyphrasesettings.put("ENABLEKEYPHRASE",(keyphrasesettings.get("ENABLEKEYPHRASE").equals("")?"FALSE":("TRUE,FALSE".contains(keyphrasesettings.get("ENABLEKEYPHRASE").toUpperCase())?keyphrasesettings.get("ENABLEKEYPHRASE").toUpperCase():"FALSE")));
		keyphrasesettings.put("KEYPHRASECOUNT", "0");
		
		if(keyphrasesettings.get("SERVICEID").equals("0")||keyphrasesettings.get("LOADID").equals("0")){
			showDialog("", new String[]{"title=ENABLE KEYPHRASE","content=INVALID SERVICE NR(SERVICEID) AND LOADNR(LOADID)"});
		}
		else{
			Database.executeDBRequest(null,"MESSAGEGATEWAY", "UPDATE <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] SET ENABLEKEYPHRASE='TRUE' WHERE ID=:KEYPHRASEID" , keyphrasesettings,null);
			selectkeyphrase();
		}
	}
	
	public void disableKeyphrase() throws Exception{
		HashMap<String,String> keyphrasesettings=new HashMap<String,String>();
		this.importRequestParametersIntoMap(keyphrasesettings, "KEYPHRASEID");
		Database.executeDBRequest(null,"MESSAGEGATEWAY", "UPDATE <DBUSER>.[MESSAGE_GATEWAY_KEYPHRASE_MAP] SET ENABLEKEYPHRASE='FALSE' WHERE ID=:KEYPHRASEID" , keyphrasesettings,null);
		selectkeyphrase();
	}
}
